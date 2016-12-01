package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.Book;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookListView;

public class BookFragment extends BaseFragment<Book> {
	@Override
	protected BaseListView<Book> createListView(Context context) {
		return new BookListView(context);
	}
	
	public static BookFragment newInstance() {
		BookFragment fragment = new BookFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}