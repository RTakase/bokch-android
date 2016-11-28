package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.User;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.UserListView;

public class UserFragment extends BaseFragment<User> {
	@Override
	protected BaseListView<User> createListView(Context context) {
		return new UserListView(context);
	}
	
	public static UserFragment newInstance() {
		UserFragment fragment = new UserFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}