package tv.bokch;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.android.BaseFragment;
import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.FullRecentListView;

public class RecentFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new FullRecentListView(context);
	}
	public static RecentFragment newInstance() {
		RecentFragment fragment = new RecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}