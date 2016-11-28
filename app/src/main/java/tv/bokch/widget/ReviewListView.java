package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Review;

public class ReviewListView extends BaseListView<Review> {
	
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
	protected int getLayoutResId() {
		return R.layout.cell_review;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new ReviewCell(view);
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	@Override
	protected Cell createHeader(View view) {
		return new DummyCell(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_dummy;
	}

	@Override
	protected Cell createFooter(View view) {
		return new DummyCell(view);
	}
	
	protected class ReviewCell extends Cell {
		private CircleNetworkImageView mUserIcon;
		private TextView mUserName;
		private RatingBar mRating;
		private TextView mComment;
		
		public ReviewCell(View view) {
			super(view);
			mUserIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
			mUserName = (TextView)view.findViewById(R.id.user_name);
			mRating = (RatingBar)view.findViewById(R.id.rating);
			mComment = (TextView)view.findViewById(R.id.comment);
		}
		
		public void bindView(Review review, int position) {
			super.bindView(review, position);
			mUserIcon.setImageUrl(review.user.iconUrl);
			mUserName.setText(review.user.name);
			Timber.d("tks, name = %s", review.user.name);
			mRating.setRating(review.rating);
			mComment.setText(review.comment);
		}
	}
}
