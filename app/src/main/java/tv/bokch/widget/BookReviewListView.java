package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;

public class BookReviewListView extends ReviewListView {
	
	public BookReviewListView(Context context) {
		super(context);
		initialize(context);
	}
	public BookReviewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BookReviewListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.cell_book_review;
	}

	@Override
	protected Cell createCell(View view) {
		return new BookReviewCell(view);
	}

	protected class BookReviewCell extends ReviewCell {
		protected BookViewHolder mBook;
		
		public BookReviewCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
		}
		
		public void bindView(final History history, int position) {
			super.bindView(history, position);
			mBook.bindView(history.book);
			mBook.setOnJacketClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseActivity)getContext()).startBookActivity(history.book);
				}
			});
		}
	}
}
