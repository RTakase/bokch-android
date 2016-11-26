package tv.bokch.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import tv.bokch.util.TextFilter;

public class TextView extends android.widget.TextView {
	
	private CharSequence mOrgText = "";
	private BufferType mOrgBufferType = BufferType.NORMAL;
	
	public TextView(Context context) {
		super(context);
		setFilters(new InputFilter[] {new TextFilter(this) });
	}
	
	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFilters(new InputFilter[] { new TextFilter(this) });
	}
	
	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFilters(new InputFilter[] { new TextFilter(this) });
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		setText(mOrgText, mOrgBufferType);
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		mOrgText = text;
		mOrgBufferType = type;
		super.setText(text, type);
	}
	
	@Override
	public CharSequence getText() {
		return mOrgText;
	}
	
	@Override
	public int length() {
		return mOrgText.length();
	}
}
