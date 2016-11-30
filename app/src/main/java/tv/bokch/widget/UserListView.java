package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.data.UserViewHolder;

public class UserListView extends BaseListView<User> {
	
	public UserListView(Context context) {
		super(context);
		initialize(context);
	}
	public UserListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public UserListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}


	@Override
	protected int getLayoutResId() {
		return R.layout.cell_user;
	}

	@Override
	protected Cell createCell(View view) {
		return new UserCell(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	protected class UserCell extends Cell {
		private UserViewHolder mUser;
		public UserCell(View view) {
			super(view);
			mUser = new UserViewHolder(view);
		}

		public void bindView(User user, int position) {
			super.bindView(user, position);
			mUser.bindView(user);
		}
	}
}
