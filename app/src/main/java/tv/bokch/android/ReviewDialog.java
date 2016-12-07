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
import tv.bokch.util.JsonUtils;

public class ReviewDialog extends BaseDialog {

	private View mReviewButton;
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
		mReviewButton = root.findViewById(R.id.review_btn);
		mReviewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mMyBook != null) {
					((BaseActivity)getActivity()).startBookActivity(mMyBook, true);
				}
			}
		});

		//レビュー状態を確認する
		setReviewButtonState(mHistory);

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
		baseAct.getMyBookStatus(history.book.bookId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					JsonUtils.dump(response);
					mMyBook = new MyBook(response);
					if (mMyBook.review == null) {
						//まだレビューを書いていない
						mReviewButton.setVisibility(View.VISIBLE);
					} else {
						//もう書いている
						mReviewButton.setVisibility(View.GONE);
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
}
