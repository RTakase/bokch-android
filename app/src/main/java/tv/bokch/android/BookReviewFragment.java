package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.History;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookReviewListView;

public class BookReviewFragment extends BaseFragment<History> {
	@Override
	protected BaseListView<History> createListView(Context context) {
		return new BookReviewListView(context);
	}
	public static BookReviewFragment newInstance() {
		BookReviewFragment fragment = new BookReviewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}