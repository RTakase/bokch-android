package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;

public class UserListView extends RecyclerView<User> {
	
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
		return R.layout.row_user;
	}

	@Override
	protected Row createRow(View view) {
		return new UserRow(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.row_footer;
	}

	@Override
	protected Row createFooterRow(View view) {
		return new DummyRow(view);
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.row_header;
	}

	@Override
	protected Row createHeaderRow(View view) {
		return new DummyRow(view);
	}

	protected class UserRow extends Row {
		private TextView mUserName;
		private NetworkImageView mUserIcon;
		private TextView mTag;
		
		public UserRow(View view) {
			super(view);
			mUserName = (TextView)view.findViewById(R.id.user_name);
			mUserIcon = (NetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
			mTag = (TextView)view.findViewById(R.id.tag);
		}
		
		public void bindView(User user, int position) {
			super.bindView(user, position);
			mUserName.setText(user.name);
			mUserIcon.setImageUrl(user.iconUrl);
			mTag.setText(user.tag);
		}
	}
}
