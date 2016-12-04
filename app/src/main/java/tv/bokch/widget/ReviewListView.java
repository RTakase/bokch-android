package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.History;
import tv.bokch.data.ReviewViewHolder;

public abstract class ReviewListView extends BaseListView<History> {
	
	public ReviewListView(Context context) {
		super(context);
		initialize(context);
	}
	public ReviewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public ReviewListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected void onCellClick(History history) {
		((BaseActivity)getContext()).startReviewActivity(history);
	}

	protected class ReviewCell extends Cell {
		protected ReviewViewHolder mReview;

		public ReviewCell(View view) {
			super(view);
			mReview = new ReviewViewHolder(view);
		}
		
		public void bindView(History history, int position) {
			super.bindView(history, position);
			mReview.bindView(history.review);
		}
	}
}
