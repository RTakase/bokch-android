package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookRecentListView;

public class BookRecentFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new BookRecentListView(context);
	}
	public static BookRecentFragment newInstance() {
		BookRecentFragment fragment = new BookRecentFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}