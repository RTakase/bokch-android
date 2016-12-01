package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import tv.bokch.R;
import tv.bokch.data.History;

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
		return R.layout.cell_summarized_recent;
	}

	@Override
	protected void onCellClick(History history) {
		startBookActivity(history.book);
	}

	@Override
	protected Cell createCell(View view) {
		RecentCell cell = new RecentCell(view);
//		cell.disableBookClick();
//		cell.disableUserClick();
		return cell;
	}
}
