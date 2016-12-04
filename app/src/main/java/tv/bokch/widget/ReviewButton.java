package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import info.hoang8f.widget.FButton;
import tv.bokch.R;

public class ReviewButton extends FButton {

	public interface ClickListener {
		void onClick(State state);
	}

	public enum State {
		POST(1), UPDATE(2), CONFIRM(3), LOADING(4);

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
				//とりあえず
				buttonColor = 0xff444444;
				break;
			case 3:
				buttonColor = 0xffa18320;
				break;
			case 4:
				buttonColor = 0xff444444;
				break;
			}
		}
	}

	private State mCurrentState;
	private ClickListener mListener;
	
	public ReviewButton(Context context) {
		super(context);
		initialize(context);
	}

	public ReviewButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public ReviewButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		State.POST.label = context.getString(R.string.label_post_review);
		State.UPDATE.label = context.getString(R.string.label_update_review);
		State.CONFIRM.label = context.getString(R.string.label_confirm_review);
		State.LOADING.label = context.getString(R.string.loading_review);
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

		//とりあえず
		if (state == State.UPDATE) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
}
