package tv.bokch.data;

import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.widget.CircleNetworkImageView;

public class UserViewHolder {
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
