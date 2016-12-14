package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import info.hoang8f.widget.FButton;
import tv.bokch.R;

public class LendButton extends FButton {
	
	public interface ClickListener {
		void onClick(State state);
	}
	
	public enum State {
		BEFORE(1), AFTER(2), LOADING(3);
		
		public int buttonColor;
		public int labelColor;
		public String label;
		
		//コンストラクタ
		State(int id) {
			labelColor = 0xddfafafa;
			switch (id) {
			case 1:
				buttonColor = 0xff225b1e;
				break;
			case 2:
				buttonColor = 0xffa18320;
				break;
			case 3:
				buttonColor = 0xff444444;
				break;
			}
		}
	}
	
	private State mCurrentState;
	private ClickListener mListener;
	
	public LendButton(Context context) {
		super(context);
		initialize(context);
	}
	
	public LendButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public LendButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}
	
	private void initialize(Context context) {
		State.BEFORE.label = context.getString(R.string.label_lend_before);
		State.AFTER.label = context.getString(R.string.label_lend_after);
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
		mCurrentState = state;
		setButtonColor(state.buttonColor);
		setText(state.label);
		setTextColor(state.labelColor);
		
		if (state == State.LOADING) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
}
