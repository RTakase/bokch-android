package tv.bokch.widget;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
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
		((BaseActivity)getContext()).startBookActivity(book);
	}

	protected class BookCell extends Cell {
		private BookViewHolder mBook;

		public BookCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
		}
		
		public void bindView(final Book book, int position) {
			super.bindView(book, position);
			mBook.bindView(book);
			mBook.setOnJacketClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseActivity)getContext()).startBookActivity(book);
				}
			});
		}
	}
}
