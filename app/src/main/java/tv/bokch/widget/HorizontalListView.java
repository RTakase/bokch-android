package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class HorizontalListView extends RecyclerView {
	private LinearLayoutManager mManager;
	
	public HorizontalListView(Context context) {
		super(context);
		initialize(context);
	}
	
	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public HorizontalListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		mManager = new LinearLayoutManager(context);
		mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		setLayoutManager(mManager);
	}
}
