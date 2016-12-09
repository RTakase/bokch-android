package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;

import tv.bokch.R;

public class BookStackListView extends StackListView {
	
	public BookStackListView(Context context) {
		super(context);
		initialize(context);
	}
	public BookStackListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BookStackListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_book_stack;
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}
	
	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}
}
