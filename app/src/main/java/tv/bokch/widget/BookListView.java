package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;

public class BookListView extends RecyclerView<Book> {
	
	public BookListView(Context context) {
		super(context);
		initialize(context);
	}
	public BookListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BookListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}
	
	
	@Override
	protected int getLayoutResId() {
		return R.layout.row_book;
	}
	
	@Override
	protected Row createRow(View view) {
		return new BookRow(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.row_footer;
	}
	
	@Override
	protected Row createFooterRow(View view) {
		return new DummyRow(view);
	}
	
	@Override
	protected int getHeaderResId() {
		return R.layout.row_header;
	}
	
	@Override
	protected Row createHeaderRow(View view) {
		return new DummyRow(view);
	}
	
	protected class BookRow extends Row {
		private NetworkImageView mJacket;
		private TextView mTitle;
		private TextView mAuthor;
		private TextView mRatingAverage;
		private TextView mTag;

		public BookRow(View view) {
			super(view);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
			mTitle = (TextView)view.findViewById(R.id.title);
			mAuthor = (TextView)view.findViewById(R.id.author);
			mTag = (TextView)view.findViewById(R.id.tag);
			mRatingAverage = (TextView)view.findViewById(R.id.rating_average);
		}
		
		public void bindView(Book book, int position) {
			super.bindView(book, position);
			mJacket.setImageUrl(book.largeImageUrl);
			mTitle.setText(book.title);
			mAuthor.setText(book.author);
			if (TextUtils.isEmpty(book.tag)) {
				mTag.setVisibility(GONE);
			} else {
				mTag.setVisibility(VISIBLE);
				mTag.setText(book.tag);
			}
			mRatingAverage.setText(String.valueOf(book.ratingAverage));
		}
	}
}
