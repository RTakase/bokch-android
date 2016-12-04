package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.Book;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookGridView;
import tv.bokch.widget.BookListView;

public class BookGridFragment extends BaseFragment<Book> {
	@Override
	protected BaseListView<Book> createListView(Context context) {
		return new BookGridView(context);
	}

	public static BookGridFragment newInstance() {
		BookGridFragment fragment = new BookGridFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}