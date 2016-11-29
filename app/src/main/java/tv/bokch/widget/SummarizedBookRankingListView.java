package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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
		return R.layout.cell_summarized_ranking_book;
	}

	@Override
	protected RankingCell createCell(View view) {
		return new SummarizedBookRankingCell(view);
	}

	protected class SummarizedBookRankingCell extends RankingCell {
		private TextView mTitle;
		private NetworkImageView mJacket;
		
		public SummarizedBookRankingCell(View view) {
			super(view);
			mTitle = (TextView)view.findViewById(R.id.title);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
		}
		
		public void bindView(Book book, int position) {
			super.bindView(book, position);
			mTitle.setText(book.title);
			ViewGroup.LayoutParams params = mJacket.getLayoutParams();
			if (params != null) {
				params.width = mDisplay.toPixels(book.largeImageWidth);
				params.height = mDisplay.toPixels(book.largeImageHeight);
				mJacket.setLayoutParams(params);
			}
			mJacket.setImageUrl(book.largeImageUrl);
		}
	}
}
