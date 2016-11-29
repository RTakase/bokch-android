package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import timber.log.Timber;
import tv.bokch.data.Book;
import tv.bokch.widget.FullBookRankingListView;
import tv.bokch.widget.BaseListView;

public class BookRankingFragment extends BaseFragment<Book> {
	@Override
	protected BaseListView<Book> createListView(Context context) {
		return new FullBookRankingListView(context);
	}
	public static BookRankingFragment newInstance() {
		BookRankingFragment fragment = new BookRankingFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}