package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;

public class BookRecentListView extends RecentListView {
	
	public BookRecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public BookRecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BookRecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
	}

	@Override
	protected int getLayoutResId(int viewType) {
		switch (viewType) {
		case VIEW_TYPE_RATING:
			return R.layout.cell_recent_book_rating;
		case VIEW_TYPE_COMMENT:
			return R.layout.cell_recent_book_comment;
		default:
			return super.getLayoutResId(viewType);
		}
	}

	@Override
	protected int getLayoutResId() {
		return 0;
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
	protected void onCellClick(int viewType, History history) {
		switch(viewType) {
		case VIEW_TYPE_RATING:
		case VIEW_TYPE_COMMENT:
			((BaseActivity)getContext()).startBookActivity(history.book);
			break;
		default:
			super.onCellClick(viewType, history);
			break;
		}
	}

}
