package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.Review;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.UserViewHolder;

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
