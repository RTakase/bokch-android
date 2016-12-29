package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.Book;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.BookGridView;

public class BookGridFragment extends BaseFragment<Book> {
	@Override
	protected BaseListView<Book> createListView(Context context) {
		boolean hide = getArguments().getBoolean("hide_book_title");
		BookGridView listview = new BookGridView(context);
		if (hide) {
			listview.hideBookTitle();
		}
		return listview;
	}

	public static BookGridFragment newInstance(boolean hideBookTitle) {
		BookGridFragment fragment = new BookGridFragment();
		Bundle args = new Bundle();
		args.putBoolean("hide_book_title", hideBookTitle);
		fragment.setArguments(args);
		return fragment;
	}
}