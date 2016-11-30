package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.User;

public class UserView extends RelativeLayout {
	private UserViewHolder mHolder;
	public UserView(Context context) {
		super(context);
		initialize(context);
	}
	
	public UserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public UserView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		inflate(context, R.layout.view_user, this);
		mHolder = new UserViewHolder(this);
	}
	
	public void bindView(User user) {
		if (mHolder != null) {
			mHolder.bindView(user);
		}
	}
	
	public static class UserViewHolder {
		public CircleNetworkImageView userIcon;
		public TextView userName;
		public TextView tag;

		public UserViewHolder(View view) {
			userIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
			userName = (TextView)view.findViewById(R.id.user_name);
			tag = (TextView)view.findViewById(R.id.tag);
		}

		public void bindView(User user) {
			if (user == null) {
				return;
			}
			if (userName != null) {
				userName.setText(user.name);
			}
			if (userIcon != null) {
				userIcon.setDefaultImageResId(R.drawable.mysteryman);
				userIcon.setImageUrl(user.iconUrl);
			}
			if (tag != null) {
				tag.setText(user.tag);
			}
		}
	}
}
