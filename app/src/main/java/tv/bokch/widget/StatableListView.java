package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.util.Display;

public class StatableListView<Data> extends FrameLayout {

	public enum State {
		Loading,
		Failed,
		OK
	}

	private ProgressBar mProgress;
	private View mEmptyView;
	private BaseListView<Data> mListView;

	public StatableListView(Context context) {
		super(context);
		initialize(context);
	}

	public StatableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public StatableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		mEmptyView = createEmptyView(context);
		mEmptyView.setVisibility(INVISIBLE);
		addView(mEmptyView, createLayoutParams());

		mProgress = new ProgressBar(context);
		mProgress.setVisibility(INVISIBLE);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		);
		params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		params.topMargin = Display.dpToPx(context, 8);
		mProgress.setLayoutParams(params);
		addView(mProgress, params);
	}

	protected View createEmptyView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.partial_empty, this, false);
		return view;
	}

	public void addListView(BaseListView<Data> listview) {
		mListView = listview;
		mListView.setVisibility(INVISIBLE);
		addView(mListView, createLayoutParams());
	}

	public void setData(ArrayList<Data> data) {
		if (mListView != null) {
			mListView.setData(data);
		}
	}
	
	public ArrayList<Data> getData() {
		if (mListView != null) {
			return mListView.getData();
		} else {
			return null;
		}
	}

	private LayoutParams createLayoutParams() {
		LayoutParams params = new LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT
		);
		params.gravity = Gravity.CENTER;
		return params;
	}

	public void setState(State state) {
		switch (state) {
		case Loading:
			mProgress.setVisibility(VISIBLE);
			mEmptyView.setVisibility(INVISIBLE);
			mListView.setVisibility(INVISIBLE);
			break;
		case OK:
			mProgress.setVisibility(INVISIBLE);
			mEmptyView.setVisibility(INVISIBLE);
			mListView.setVisibility(VISIBLE);
			break;
		case Failed:
			mProgress.setVisibility(INVISIBLE);
			mEmptyView.setVisibility(VISIBLE);
			mListView.setVisibility(INVISIBLE);
		}
	}
	
	public boolean onData(ArrayList<Data> data) {
		if (data == null || data.size() == 0) {
			setState(StatableListView.State.Failed);
			return false;
		} else {
			setState(StatableListView.State.OK);
			setData(data);
			return true;
		}
	}
}
