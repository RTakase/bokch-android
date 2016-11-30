package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.UserReviewListView;

public class UserReviewFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new UserReviewListView(context);
	}
	public static UserReviewFragment newInstance() {
		UserReviewFragment fragment = new UserReviewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}