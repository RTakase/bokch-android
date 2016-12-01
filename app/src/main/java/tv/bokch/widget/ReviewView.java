package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.Review;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.UserViewHolder;

public class ReviewView extends RelativeLayout {
	private ReviewViewHolder mReview;
	private BookViewHolder mBook;
	private UserViewHolder mUser;
	public ReviewView(Context context) {
		super(context);
		initialize(context);
	}
	
	public ReviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		inflate(context, R.layout.view_review, this);
		mReview = new ReviewViewHolder(this);
		mBook = new BookViewHolder(this);
		mUser = new UserViewHolder(this);
	}
	
	public void bindView(History history) {
		if (mReview != null) {
			mReview.bindView(history.review);
			mBook.bindView(history.book);
			mUser.bindView(history.user);
		}
	}
}
