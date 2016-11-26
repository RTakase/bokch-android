package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import tv.bokch.R;

public class SummarizedRecentListView extends RecentListView {

	public SummarizedRecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public SummarizedRecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public SummarizedRecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		setOrientation(Orientation.Horizontal);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_summarized_recent;
	}

	@Override
	protected Row createRow(View view) {
		return new RecentRow(view);
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
}
