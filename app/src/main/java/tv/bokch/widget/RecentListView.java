package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.History;

public class RecentListView extends HorizontalListView<History> {

	public RecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public RecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public RecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
	}
	
	public void setData(ArrayList<History> histories) {
		super.setData(histories);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_home_recent;
	}

	@Override
	protected HorizontalListView<History>.Row createRow(View view) {
		return new HomeRecentRow(view);
	}
	
	protected class HomeRecentRow extends Row {
		private TextView mTitle;
		private CircleNetworkImageView mUserIcon;
		private NetworkImageView mJacket;
		
		public HomeRecentRow(View view) {
			super(view);
			mTitle = (TextView)view.findViewById(R.id.title);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
			mUserIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
		}
		
		public void bindView(History history, int position) {
			super.bindView(history, position);
			mTitle.setText(history.book.title);
			mJacket.setImageUrl(history.book.largeImageUrl);
			mUserIcon.setImageUrl(history.user.iconUrl);
		}
	}
}
