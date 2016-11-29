package tv.bokch.android;

import android.app.Dialog;
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

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.Review;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BookView;

public class ReviewEditDialog extends BaseDialog {

	private BookView.BookViewHolder mBookHolder;
	private EditText mEditor;
	private RatingBar mRatingBar;
	private Book mBook;
	private User mUser;
	private Review mReview;
	private History mHistory;

	public static ReviewEditDialog newInstance(Book book, User user, Review review, History history) {
		ReviewEditDialog dialog = new ReviewEditDialog();
		Bundle args = new Bundle();
		args.putParcelable("user", user);
		args.putParcelable("book", book);
		args.putParcelable("review", review);
		args.putParcelable("history", history);
		dialog.setArguments(args);
		return dialog;
	}

//	@Override
//	protected int getWidth(int orientation) {
//		return (int)(super.getWidth(orientation) * 1.2f);
//	}
//
//	@Override
//	protected int getHeight(int orientation) {
//		if (mBookHolder == null) {
//			return super.getHeight(orientation);
//		} else {
//			return (int)(((float)mBookHolder.jacketHeight * getWidth(orientation)) / mBookHolder.jacketWidth);
//		}
//	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mUser = getArguments().getParcelable("user");
		mBook = getArguments().getParcelable("book");
		mReview = getArguments().getParcelable("review");
		mHistory = getArguments().getParcelable("history");

		View root = mParentActivity.getLayoutInflater().inflate(R.layout.dialog_review_edit, null);

		mBookHolder = new BookView.BookViewHolder(root);
		mBookHolder.bindView(mBook);

		mEditor = (EditText)root.findViewById(R.id.review_editor);
		mEditor.setHint(R.string.prompt_review);

		mRatingBar = (RatingBar)root.findViewById(R.id.rating);

		View submit = root.findViewById(R.id.submit_btn);
		submit.setOnClickListener(mSubmitClickListener);

//		View back = root.findViewById(R.id.back_btn);
//		back.setOnClickListener(mBackClickListener);

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(root);
		return dialog;
	}

	private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int rating = (int)mRatingBar.getRating();
			String comment = mEditor.getText().toString();
			if (mReview == null) {
				ApiRequest request = new ApiRequest();
				try {
					request.post_history(mBook.bookId, mUser.userId, rating, comment, mHistoryApiListener);
				} catch (JSONException e) {
					Timber.w(e, null);
					Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				}
			} else {

			}
		}
	};

	private View.OnClickListener mBackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			confirmDismiss();
		}
	};

	private ApiRequest.ApiListener<JSONObject> mHistoryApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			Intent intent = new Intent();
			intent.putExtra("review", mReview);
			sendActivityResultCallback(intent);
			dismiss();
		}

		@Override
		public void onError(ApiRequest.ApiError error) {
			try {
				String message = "";
				JSONObject obj = new JSONObject(error.getResponseBodyString());
				Iterator<String> iterator = obj.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					JSONArray errors = obj.optJSONArray(key);
					for (int i = 0; i < errors.length(); i++) {
						String e = errors.optString(i);
						message += TextUtils.join(":", new String[]{key, e});
						message += "\n";
					}
				}
				showAlertDialog(getString(R.string.error), message);
			} catch (JSONException e) {
				Timber.w(e, null);
			}
			Timber.w(error, null);
		}
	};

}
