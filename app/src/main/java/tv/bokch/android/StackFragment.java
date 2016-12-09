package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.data.Stack;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookStackListView;

public class StackFragment extends BaseFragment<Stack> {
	@Override
	protected BaseListView<Stack> createListView(Context context) {
		return new BookStackListView(context);
	}
	public static StackFragment newInstance() {
		StackFragment fragment = new StackFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}