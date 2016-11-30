package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import tv.bokch.R;
import tv.bokch.data.UserViewHolder;
import tv.bokch.data.History;


public class UserReviewListView extends ReviewListView {
	
	public UserReviewListView(Context context) {
		super(context);
		initialize(context);
	}
	public UserReviewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public UserReviewListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_user_review;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new UserReviewCell(view);
	}
	
	protected class UserReviewCell extends ReviewCell {
		protected UserViewHolder mUser;
		
		public UserReviewCell(View view) {
			super(view);
			mUser = new UserViewHolder(view);
		}
		
		public void bindView(History history, int position) {
			super.bindView(history, position);
			mUser.bindView(history.user);
		}
	}
}
