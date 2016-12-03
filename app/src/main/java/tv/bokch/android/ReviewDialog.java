package tv.bokch.android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.UserViewHolder;

public class ReviewDialog extends BaseDialog {

	public static ReviewDialog newInstance(History history) {
		Bundle args = new Bundle();
		ReviewDialog fragment = new ReviewDialog();
		args.putParcelable("history", history);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final History history = getArguments().getParcelable("history");

		View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_review, null);

		UserViewHolder user = new UserViewHolder(root);
		user.bindView(history.user);
		user.setOnIconClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getActivity()).startUserActivity(history.user);
				dismiss();
			}
		});

		BookViewHolder book = new BookViewHolder(root);
		book.bindView(history.book);
		book.setOnJacketClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getActivity()).startBookActivity(history.book);
				dismiss();
			}
		});

		ReviewViewHolder review = new ReviewViewHolder(root);
		review.bindView(history.review);
		if (TextUtils.isEmpty(history.review.comment)) {
			if (review.comment != null) {
				review.comment.setText(getString(R.string.empty_comment));
				review.comment.setTextColor(0x60000000);
			}
		}

		View view = root.findViewById(R.id.more_btn);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getActivity()).startBookActivity(history.book);
				dismiss();
			}
		});

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setContentView(root);
		return dialog;
	}
}
