package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import tv.bokch.android.BaseActivity;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.UserViewHolder;

public abstract class RecentListView extends BaseListView<History> {

	public static final int VIEW_TYPE_COMMENT = 10;
	public static final int VIEW_TYPE_RATING = 11;

	public RecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public RecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public RecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
	}

	@Override
	protected void onCellClick(History history) {
		//((BaseActivity)getContext()).startBookActivity(history.book);
	}

	@Override
	protected Cell createCell(View view) {
		return new RecentCell(view);
	}

	@Override
	protected int getViewType(History history) {
		if (history.review == null) {
			return VIEW_TYPE_CONTENT;
		} else {
			if (!TextUtils.isEmpty(history.review.comment)) {
				return VIEW_TYPE_COMMENT;
			} else if (history.review.rating > 0) {
				return VIEW_TYPE_RATING;
			} else {
				return VIEW_TYPE_CONTENT;
			}
		}
	}

	protected class RecentCell extends Cell {
		protected BookViewHolder mBook;
		protected UserViewHolder mUser;
		protected ReviewViewHolder mReview;
		
		private boolean mDisableBookClick;
		private boolean mDisableUserClick;

		public RecentCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
			mUser = new UserViewHolder(view);
			mReview = new ReviewViewHolder(view);
		}

		public void bindView(final History history, int position) {
			super.bindView(history, position);
			mBook.bindView(history.book);
			if (!mDisableBookClick) {
				mBook.setOnJacketClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((BaseActivity)getContext()).startBookActivity(history.book);
					}
				});
			}
			mUser.bindView(history.user);
			if (!mDisableUserClick) {
				mUser.setOnIconClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((BaseActivity)getContext()).startUserActivity(history.user);
					}
				});
			}
			
			mReview.bindView(history.review);
			mReview.setCommentClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseActivity)getContext()).startReviewActivity(history);
				}
			});
		}
		
		public void disableBookClick() {
			mDisableBookClick = true;
		}
		public void disableUserClick() {
			mDisableUserClick = true;
		}
	}
}
