package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.data.UserViewHolder;

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
}
