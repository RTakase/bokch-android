package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookUserRecentListView;

public class RecentFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new BookUserRecentListView(context);
	}
	public static RecentFragment newInstance() {
		RecentFragment fragment = new RecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}