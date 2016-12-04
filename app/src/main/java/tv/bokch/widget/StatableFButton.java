package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import info.hoang8f.widget.FButton;
import tv.bokch.R;

public class StatableFButton extends FButton {

	public enum State {
		BEFORE,
		AFTER,
		LOADING
	}
	public StatableFButton(Context context) {
		super(context);
		initialize(context);
	}

	public StatableFButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public StatableFButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
	}

	protected String getLabel(State state) {
		switch (state) {
		case LOADING:
			return getContext().getString(R.string.loading);
		default:
			return "";
		}
	}

	protected int getLabelColor(State state) {
		return 0xddfafafa;
	}

	protected int getButtonColor(State state) {
		switch (state) {
		case BEFORE:
			return 0xff225b1e;
		case AFTER:
			return 0xffa18320;
		case LOADING:
			return 0xff444444;
		default:
			return 0x00000000;
		}
	}

	protected void onButtonClick(State state) {
	}

	public void setState(final State state) {
		setText(getLabel(state));
		setTextColor(getLabelColor(state));
		setButtonColor(getButtonColor(state));

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonClick(state);
			}
		});

		if (state == State.LOADING) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}
}
