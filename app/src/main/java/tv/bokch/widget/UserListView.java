package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;

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
	protected Cell createFooter(View view) {
		return new DummyCell(view);
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	@Override
	protected Cell createHeader(View view) {
		return new DummyCell(view);
	}

	protected class UserCell extends Cell {
		private TextView mUserName;
		private NetworkImageView mUserIcon;
		private TextView mTag;
		
		public UserCell(View view) {
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
