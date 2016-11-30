package tv.bokch.widget;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.android.UserListActivity;
import tv.bokch.data.History;

public class UserRecentListView extends RecentListView {
	
	public UserRecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public UserRecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public UserRecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setOrientation(Orientation.Vertical);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_full_ranking_user;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new UserRecentCell(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}
	
	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}
	
	protected class UserRecentCell extends RecentCell {

		public UserRecentCell(View view) {
			super(view);
		}
		
		public void bindView(final History history, int position) {
			super.bindView(history, position);
		}
	}
}
