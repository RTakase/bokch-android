package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.User;
import tv.bokch.widget.FullUserRankingListView;
import tv.bokch.widget.BaseListView;

public class UserRankingFragment extends BaseFragment<User> {
	@Override
	protected BaseListView<User> createListView(Context context) {
		return new FullUserRankingListView(context);
	}

	public static UserRankingFragment newInstance() {
		UserRankingFragment fragment = new UserRankingFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}