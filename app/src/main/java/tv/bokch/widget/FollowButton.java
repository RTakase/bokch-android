package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;

import tv.bokch.R;

public class FollowButton extends StatableFButton {
	private OnClickListener mFollowClickListener;
	private OnClickListener mUnfollowClickListener;	
	
	public FollowButton(Context context) {
		super(context);
		initialize(context);
	}

	public FollowButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public FollowButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
	}

	@Override
	protected String getLabel(State state) {
		switch (state) {
		case BEFORE:
			return getContext().getString(R.string.before_follow);
		case AFTER:
			return getContext().getString(R.string.after_follow);
		default:
			return super.getLabel(state);
		}
	}

	public void setState(boolean isFollowed) {
		setState(isFollowed ? State.AFTER : State.BEFORE);
	}

	@Override
	protected void onButtonClick(State state) {
		switch (state) {
		case BEFORE:
			if (mFollowClickListener != null) {
				mFollowClickListener.onClick(FollowButton.this);
			}
			break;
		case AFTER:
			if (mUnfollowClickListener != null) {
				mUnfollowClickListener.onClick(FollowButton.this);
			}
			break;
		 default:
		}
	}

	public void setFollowClickListner(boolean isFollow, OnClickListener listener) {
		if (isFollow) {
			mFollowClickListener = listener;
		} else {
			mUnfollowClickListener = listener;
		}
	}
}
