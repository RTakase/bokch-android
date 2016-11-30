package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.data.UserViewHolder;

public class SummarizedUserRankingListView extends RankingListView<User> {

	public SummarizedUserRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public SummarizedUserRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public SummarizedUserRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setOrientation(Orientation.Horizontal);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.cell_summarized_ranking_user;
	}

	@Override
	protected RankingCell createCell(View view) {
		return new SummarizedUserRankingCell(view);
	}

	protected class SummarizedUserRankingCell extends RankingCell {
		private UserViewHolder mUser;

		public SummarizedUserRankingCell(View view) {
			super(view);
			mUser = new UserViewHolder(view);
		}
		
		public void bindView(User user, int position) {
			super.bindView(user, position);
			mUser.bindView(user);
		}
	}
}
