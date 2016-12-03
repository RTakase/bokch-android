package tv.bokch.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.widget.CircleNetworkImageView;
import tv.bokch.widget.FollowButton;
import tv.bokch.widget.StatableFButton;

public class UserViewHolder {
	public CircleNetworkImageView userIcon;
	public TextView userName;
	public TextView tag;
	public FollowButton followButton;

	public UserViewHolder(View view) {
		userIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
		userName = (TextView)view.findViewById(R.id.user_name);
		tag = (TextView)view.findViewById(R.id.tag);
		followButton = (FollowButton)view.findViewById(R.id.follow_btn);
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
			userIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			userIcon.setImageUrl(user.iconUrl);
		}
		if (tag != null) {
			tag.setText(user.tag);
		}
	}

	public void setOnIconClickListener(View.OnClickListener listener) {
		if (userIcon != null) {
			userIcon.setOnClickListener(listener);
		}
	}

	public void setFollowed(boolean followed) {
		if (followButton != null) {
			followButton.setState(followed);
		}
	}

	public void setFollowClickListener(boolean isFollow, View.OnClickListener listener) {
		if (followButton != null) {
			followButton.setFollowClickListner(isFollow, listener);
		}
	}
}
