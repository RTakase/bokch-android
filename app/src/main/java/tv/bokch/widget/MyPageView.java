package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;

import tv.bokch.R;


public class MyPageView extends UserView {
	public MyPageView(Context context) {
		super(context);
	}
	
	public MyPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyPageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	protected int getLayoutResId() {
		return R.layout.view_mypage;
	}
}
