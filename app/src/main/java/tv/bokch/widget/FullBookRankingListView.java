package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;

public class FullBookRankingListView extends RankingListView<Book> {
	
	public FullBookRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public FullBookRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public FullBookRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setOrientation(Orientation.Vertical);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_full_ranking_book;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new FullBookRankingCell(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	protected class FullBookRankingCell extends RankingCell {
		protected BookViewHolder mBook;
		private TextView mScore;
		
		public FullBookRankingCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
			mScore = (TextView)view.findViewById(R.id.score);
		}
		
		public void bindView(Book book, int position) {
			super.bindView(book, position);
			mBook.bindView(book);

			if (mScore != null && book.score > 0) {
				mScore.setText(String.valueOf(book.score));
			}
		}
	}
}
