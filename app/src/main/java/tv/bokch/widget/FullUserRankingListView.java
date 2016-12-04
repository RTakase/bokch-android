package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.data.UserViewHolder;

public class FullUserRankingListView extends RankingListView<User> {

	public FullUserRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public FullUserRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public FullUserRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
		return new FullUserRankingCell(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	protected class FullUserRankingCell extends RankingCell {
		private UserViewHolder mUser;
		private TextView mScore;

		public FullUserRankingCell(View view) {
			super(view);
			mUser = new UserViewHolder(view);
			mScore = (TextView)view.findViewById(R.id.score);
		}

		public void bindView(final User user, int position) {
			super.bindView(user, position);
			mUser.bindView(user);

			if (mScore != null && user.score > 0) {
				mScore.setText(String.valueOf(user.score));
			}
		}
	}
}
