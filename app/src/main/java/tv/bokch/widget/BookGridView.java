package tv.bokch.widget;

import android.content.Context;

import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;

public class BookGridView extends BaseListView<Book> {
	private boolean mHideBookTitle;
	public BookGridView(Context context) {
		super(context);
		initialize(context);
	}
	public BookGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BookGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setClipChildren(false);
		setClipToPadding(false);
	}

	public void hideBookTitle() {
		mHideBookTitle = true;
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_book_grid;
	}
	
	@Override
	protected Cell createCell(View view) {
		BookCell cell = new BookCell(view);
		if (mHideBookTitle) {
			cell.hideBookTitle();
		}
		return cell;
	}

	@Override
	protected LayoutManager createLayoutManager(Context context) {
		return new GridLayoutManager(context, 3);
	}

	@Override
	protected void onCellClick(Book book) {
		((BaseActivity)getContext()).startBookActivity(book);
	}

	public void setData(ArrayList<Book> data) {
		super.setData(data, false, false);
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

		public void hideBookTitle() {
			mBook.hideBookTitle();
		}
	}
}
