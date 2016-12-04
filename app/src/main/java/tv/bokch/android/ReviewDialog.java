package tv.bokch.android;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.MyBook;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.User;
import tv.bokch.data.UserViewHolder;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.ReviewButton;

public class ReviewDialog extends BaseDialog {

	private ReviewButton mReviewButton;
	private MyBook mMyBook;
	private History mHistory;
	private User mMyUser;

	public static ReviewDialog newInstance(History history) {
		Bundle args = new Bundle();
		ReviewDialog fragment = new ReviewDialog();
		args.putParcelable("history", history);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mHistory = getArguments().getParcelable("history");

		mMyUser = ((App)getActivity().getApplication()).getMyUser();

		View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_review, null);

		//ユーザーアイコンをはみださせるためにダイアログ領域を広げているので、枠外タップで終了できるようにする
		root.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		//コンテンツのクリックがルートへ伝搬することを防ぐ
		View content = root.findViewById(R.id.content);
		content.setOnClickListener(null);

		//ユーザビュー
		UserViewHolder user = new UserViewHolder(root);
		user.bindView(mHistory.user);
		user.setOnIconClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getActivity()).startUserActivity(mHistory.user);
				dismiss();
			}
		});

		//本ビュー
		BookViewHolder book = new BookViewHolder(root);
		book.bindView(mHistory.book);
		book.setOnJacketClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMyBook == null) {
					((BaseActivity)getActivity()).startBookActivity(mHistory.book);
				} else {
					((BaseActivity)getActivity()).startBookActivity(mMyBook);
				}
				dismiss();
			}
		});

		//レビュービュー
		ReviewViewHolder review = new ReviewViewHolder(root);
		review.bindView(mHistory.review);
		if (mHistory.review != null && TextUtils.isEmpty(mHistory.review.comment)) {
			if (review.comment != null) {
				review.comment.setText(getString(R.string.empty_comment));
				review.comment.setTextColor(0x60000000);
			}
		}

		//もっと見るボタン
		View view = root.findViewById(R.id.more_btn);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMyBook == null) {
					((BaseActivity)getActivity()).startBookActivity(mHistory.book);
				} else {
					((BaseActivity)getActivity()).startBookActivity(mMyBook);
				}
				dismiss();
			}
		});

		//レビューボタン
		mReviewButton = (ReviewButton)root.findViewById(R.id.review_btn);
		mReviewButton.setClickListener(mReviewClickListener);

		if (TextUtils.equals(mMyUser.userId,mHistory.user.userId)) {
			//自分のレビュー
			mReviewButton.setState(ReviewButton.State.UPDATE);
		} else {
			//他人のレビューなので状態を確認する
			setReviewButtonState(mHistory);
		}

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setContentView(root);
		return dialog;
	}

	@Override
	protected int getWidth(int orientation) {
		return WindowManager.LayoutParams.MATCH_PARENT;
	}

	@Override
	protected int getHeight(int orientation) {
		return WindowManager.LayoutParams.MATCH_PARENT;
	}

	private void setReviewButtonState(History history) {
		BaseActivity baseAct = (BaseActivity)getActivity();
		mReviewButton.setState(ReviewButton.State.LOADING);
		baseAct.getMyBookStatus(history.book.bookId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					mMyBook = new MyBook(response);
					if (mMyBook.review == null) {
						//まだレビューを書いていない
						mReviewButton.setState(ReviewButton.State.POST);
					} else {
						//もう書いている
						mReviewButton.setState(ReviewButton.State.CONFIRM);
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
				Toast.makeText(getActivity(), getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				Timber.w(error, null);
			}
		});
	}

	private ReviewButton.ClickListener mReviewClickListener = new ReviewButton.ClickListener() {
		@Override
		public void onClick(ReviewButton.State state) {
			switch (state) {
			case POST:
				if (mMyBook != null) {
					((BaseActivity)getActivity()).startBookActivity(mMyBook, true);
				}
				dismiss();
				break;
			case UPDATE:
				//TODO implement
				break;
			case CONFIRM:
				((BaseActivity)getActivity()).startUserActivity(mMyUser, mHistory.book.bookId);
				dismiss();
				break;
			}
		}
	};
}
