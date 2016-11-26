package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.User;

public class HomeUserRankingListView extends RankingListView<User> {

	public HomeUserRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public HomeUserRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public HomeUserRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}

	public void setData(ArrayList<User> data) {
		super.setData(data);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_home_ranking_user;
	}

	@Override
	protected RankingRow createRow(View view) {
		return new HomeUserRankingRankingRow(view);
	}

	protected class HomeUserRankingRankingRow extends RankingRow {
		private TextView mName;
		private NetworkImageView mUserIcon;
		
		public HomeUserRankingRankingRow(View view) {
			super(view);
			mName = (TextView)view.findViewById(R.id.name);
			mUserIcon = (NetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
		}
		
		public void bindView(User user, int position) {
			super.bindView(user, position);
			mName.setText(user.name);
			mUserIcon.setImageUrl(user.iconUrl);
		}
	}
}
