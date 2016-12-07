package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.UserRecentListView;

public class UserRecentFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new UserRecentListView(context);
	}
	public static UserRecentFragment newInstance() {
		UserRecentFragment fragment = new UserRecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}