package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;

public class SummarizedBookRankingListView extends RankingListView<Book> {

	public SummarizedBookRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public SummarizedBookRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public SummarizedBookRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setOrientation(Orientation.Horizontal);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_summarized_ranking_book;
	}

	@Override
	protected RankingRow createRow(View view) {
		return new SummarizedBookRankingRow(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.row_dummy;
	}

	@Override
	protected Row createFooterRow(View view) {
		return new DummyRow(view);
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.row_dummy;
	}

	@Override
	protected Row createHeaderRow(View view) {
		return new DummyRow(view);
	}

	protected class SummarizedBookRankingRow extends RankingRow {
		private TextView mTitle;
		private NetworkImageView mJacket;
		
		public SummarizedBookRankingRow(View view) {
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
