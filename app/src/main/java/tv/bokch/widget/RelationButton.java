package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import info.hoang8f.widget.FButton;
import timber.log.Timber;
import tv.bokch.R;

public class RelationButton extends FButton {
	
	private State mCurrentState;
	private ClickListener mListener;
	
	public interface ClickListener {
		void onClick(State state);
	}
	
	public enum State {
		FOLLOWERS(1), FOLLOWEES(2), LOADING(3);
		
		public int buttonColor;
		public int labelColor;
		public String label;
		
		//コンストラクタ
		State(int id) {
			switch (id) {
			case 1:
			case 2:
				buttonColor = 0xffa18320;
				labelColor = 0xddfafafa;
				break;
			case 3:
				buttonColor = 0xff444444;
				labelColor = 0xa0fafafa;
				break;
			}
		}
	}
	
	public RelationButton(Context context) {
		super(context);
		initialize(context);
	}
	
	public RelationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public RelationButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}
	
	private void initialize(Context context) {
		//本当は逆！
		State.FOLLOWERS.label = context.getString(R.string.label_follower);
		State.FOLLOWEES.label = context.getString(R.string.label_followee);
		State.LOADING.label = context.getString(R.string.loading_button);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentState != null) {
					mListener.onClick(mCurrentState);
				}
			}
		});
	}
	
	public void setClickListener(ClickListener listener) {
		mListener = listener;
	}

	public void setState(State state) {
		setState(state, -1);
	}
	public void setState(State state, int count) {
		mCurrentState = state;
		setButtonColor(state.buttonColor);
		Timber.d("tks, %s, %s", state.label, String.format(state.label, count));
		setText(String.format(state.label, count));
		setTextColor(state.labelColor);
		
		if (state == State.LOADING) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}

}
