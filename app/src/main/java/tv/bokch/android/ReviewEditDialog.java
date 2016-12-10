package tv.bokch.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.Review;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.JsonUtils;

public class ReviewEditDialog extends BaseDialog {

	private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

	private BookViewHolder mBookHolder;
	private EditText mEditor;
	private RatingBar mRatingBar;
	private Book mBook;
	private User mUser;
	private Review mPostedReview;
	private Review mPostingReview;
	private History mHistory;

	private boolean mSaved;

	public static ReviewEditDialog newInstance(Book book, User user, Review posted, Review posting, History history) {
		ReviewEditDialog dialog = new ReviewEditDialog();
		Bundle args = new Bundle();
		args.putParcelable("users", user);
		args.putParcelable("book", book);
		args.putParcelable("posted_review", posted);
		args.putParcelable("posting_review", posting);
		args.putParcelable("history", history);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mUser = getArguments().getParcelable("users");
		mBook = getArguments().getParcelable("book");
		mPostedReview = getArguments().getParcelable("posted_review");
		mPostingReview = getArguments().getParcelable("posting_review");
		mHistory = getArguments().getParcelable("history");

		View root = mParentActivity.getLayoutInflater().inflate(R.layout.dialog_review_edit, null);

		mBookHolder = new BookViewHolder(root);
		mBookHolder.bindView(mBook);

		mEditor = (EditText)root.findViewById(R.id.review_editor);
		mEditor.setHint(R.string.prompt_comment);

		mRatingBar = (RatingBar)root.findViewById(R.id.rating);

		if (mPostingReview != null) {
			mEditor.setText(mPostingReview.comment);
			mRatingBar.setRating(mPostingReview.rating);
		} else if (mPostedReview != null) {
			mEditor.setText(mPostedReview.comment);
			mRatingBar.setRating(mPostedReview.rating);
		}

		View submit = root.findViewById(R.id.submit_btn);
		submit.setOnClickListener(mSubmitClickListener);

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setContentView(root);
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		Intent intent = new Intent();

		if (!mSaved) {
			if (mPostingReview == null) {
				mPostingReview = new Review();
			}
			mPostingReview.rating = (int)mRatingBar.getRating();
			mPostingReview.comment = mEditor.getText().toString();
			intent.putExtra("posting_review", mPostingReview);
		} else {
			intent.putExtra("posted_review", mPostedReview);
		}
		sendActivityResultCallback(intent);
		super.onDismiss(dialog);
	}

	private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			final int rating = (int)mRatingBar.getRating();
			final String comment = mEditor.getText().toString();

			if (rating == 0) {
				Toast.makeText(getActivity(), getString(R.string.empty_review), Toast.LENGTH_SHORT).show();
				return;
			}

			if (mPostedReview == null) {
				showSpinner();
				final ApiRequest request = new ApiRequest();
				mExecutor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							dismissSpinner();
							request.post_history(mBook.bookId, mUser.userId, rating, comment, mHistoryApiListener);
						} catch (JSONException e) {
							Timber.w(e, null);
							dismissSpinner();
							Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else {
				showSpinner();
				final ApiRequest request = new ApiRequest();
				mExecutor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							dismissSpinner();
							request.put_review(mPostedReview.id, rating, comment, mReviewApiListener);
						} catch (JSONException e) {
							Timber.w(e, null);
							dismissSpinner();
							Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}
	};

	private ApiRequest.ApiListener<JSONObject> mHistoryApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				dismissSpinner();
				JSONObject obj = response.optJSONObject("review");
				mPostedReview = new Review(obj);
				Toast.makeText(getActivity(), getString(R.string.message_posted_review), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Timber.w(e, null);
			}
			mSaved = true;
			dismiss();
		}

		@Override
		public void onError(final ApiRequest.ApiError error) {
			dismissSpinner();
			Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			Timber.d("tks, %s", error.getLocalizedMessage());
			Timber.w(error, null);
			mExecutor.submit(new Runnable() {
				@Override
				public void run() {
					Timber.d("tks, %s", error.getResponseBodyString());
				}
			});
			//					try {
			//						String message = "";
			//						JSONObject obj = new JSONObject(error.getResponseBodyString());
			//						Iterator<String> iterator = obj.keys();
			//						while (iterator.hasNext()) {
			//							String key = iterator.next();
			//							JSONArray errors = obj.optJSONArray(key);
			//							for (int i = 0; i < errors.length(); i++) {
			//								String e = errors.optString(i);
			//								message += TextUtils.join(":", new String[]{key, e});
			//								message += "\n";
			//							}
			//							String value = obj.optString(key);
			//							message += value;
			//						}
			//						showAlertDialog(getString(R.string.error), message);
			//					} catch (JSONException e) {
			//						Timber.w(e, null);
			//					}
			//				}
			//			});
		}
	};

	private ApiRequest.ApiListener<JSONObject> mReviewApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			JsonUtils.dump(response);
			mPostedReview.rating = (int)mRatingBar.getRating();
			mPostedReview.comment = mEditor.getText().toString();
			mSaved = true;
			Toast.makeText(getActivity(), getString(R.string.message_updated_review), Toast.LENGTH_SHORT).show();
			dismiss();
		}

		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			Timber.w(error, null);
		}
	};

}
