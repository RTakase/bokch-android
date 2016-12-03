package tv.bokch.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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

public class ReviewEditDialog extends BaseDialog {

	private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

	private BookViewHolder mBookHolder;
	private EditText mEditor;
	private RatingBar mRatingBar;
	private Book mBook;
	private User mUser;
	private Review mReview;
	private History mHistory;

	private boolean mSavedReview;

	public static ReviewEditDialog newInstance(Book book, User user, Review review, History history) {
		ReviewEditDialog dialog = new ReviewEditDialog();
		Bundle args = new Bundle();
		args.putParcelable("users", user);
		args.putParcelable("book", book);
		args.putParcelable("review", review);
		args.putParcelable("history", history);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mUser = getArguments().getParcelable("users");
		mBook = getArguments().getParcelable("book");
		mReview = getArguments().getParcelable("review");
		mHistory = getArguments().getParcelable("history");

		View root = mParentActivity.getLayoutInflater().inflate(R.layout.dialog_review_edit, null);

		mBookHolder = new BookViewHolder(root);
		mBookHolder.bindView(mBook);

		mEditor = (EditText)root.findViewById(R.id.review_editor);
		mEditor.setHint(R.string.prompt_review);

		mRatingBar = (RatingBar)root.findViewById(R.id.rating);

		if (mReview != null) {
			mEditor.setText(mReview.comment);
			mRatingBar.setRating(mReview.rating);
		}

		View submit = root.findViewById(R.id.submit_btn);
		submit.setOnClickListener(mSubmitClickListener);

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setContentView(root);
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (mSavedReview) {
			Intent intent = new Intent();
			intent.putExtra("review", mReview);
			sendActivityResultCallback(intent);
		}
		super.onDismiss(dialog);
	}

	private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			final int rating = (int)mRatingBar.getRating();
			final String comment = mEditor.getText().toString();

			if (rating == 0 && TextUtils.isEmpty(comment)) {
				Toast.makeText(getActivity(), getString(R.string.empty_review), Toast.LENGTH_SHORT).show();
				return;
			}

			if (mReview == null) {
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
							request.put_review(mReview.id, rating, comment, mReviewApiListener);
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
				mReview = new Review(obj);
				Toast.makeText(getActivity(), getString(R.string.succeed_post_review), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				Timber.w(e, null);
			}
			mSavedReview = true;
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
			mReview.rating = (int)mRatingBar.getRating();
			mReview.comment = mEditor.getText().toString();
			mSavedReview = true;
			Toast.makeText(getActivity(), getString(R.string.succeed_put_review), Toast.LENGTH_SHORT).show();
			dismiss();
		}

		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			Timber.w(error, null);
		}
	};

}
