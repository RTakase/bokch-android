package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.RecentListView;
import tv.bokch.widget.ReviewListView;

public class RecentFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return null;
	}
	public static RecentFragment newInstance() {
		RecentFragment fragment = new RecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}