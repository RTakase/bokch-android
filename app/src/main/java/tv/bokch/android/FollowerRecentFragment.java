package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.*;

public class FollowerRecentFragment extends RecentFragment {

	public static FollowerRecentFragment newInstance() {
		FollowerRecentFragment fragment = new FollowerRecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected StatableListView<History> createStatableListView(Context context) {
		return new FollowerStatableListView(context);
	}
}