package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.Book;

public class HomeBookRankingListView extends RankingListView<Book> {

	public HomeBookRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public HomeBookRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public HomeBookRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_home_ranking_book;
	}

	@Override
	protected RankingRow createRow(View view) {
		return new HomeBookRankingRow(view);
	}

	public void setData(ArrayList<Book> books) {
		super.setData(books);
	}
	
	protected class HomeBookRankingRow extends RankingRow {
		private TextView mTitle;
		private NetworkImageView mJacket;
		
		public HomeBookRankingRow(View view) {
			super(view);
			mTitle = (TextView)view.findViewById(R.id.title);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
		}
		
		public void bindView(Book book, int position) {
			super.bindView(book, position);
			mTitle.setText(book.title);
			mJacket.setImageUrl(book.largeImageUrl);
		}
	}
}
