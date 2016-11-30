package tv.bokch.widget;

import android.content.Context;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;

public class BookListView extends BaseListView<Book> {
	
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
		return R.layout.cell_book;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new BookCell(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	@Override
	protected void onCellClick(Book book) {
		startBookActivity(book);
	}

	protected class BookCell extends Cell {
		private BookViewHolder mBook;

		public BookCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
		}
		
		public void bindView(Book book, int position) {
			if (!TextUtils.isEmpty(book.title)) {
				mBook.bindView(book);
			}
			super.bindView(book, position);
		}
	}
}
