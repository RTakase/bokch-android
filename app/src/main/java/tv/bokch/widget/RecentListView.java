package tv.bokch.widget;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.UserViewHolder;

public abstract class RecentListView extends BaseListView<History> {

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

	protected class RecentCell extends Cell {
		protected BookViewHolder mBook;
		protected UserViewHolder mUser;
		protected ReviewViewHolder mReview;
		protected TextView mCreated;
		

		public RecentCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
			mUser = new UserViewHolder(view);
			mReview = new ReviewViewHolder(view);
			mCreated = (TextView)view.findViewById(R.id.created);
		}

		public void bindView(History history, int position) {
			super.bindView(history, position);
			mBook.bindView(history.book);
			mUser.bindView(history.user);
			mReview.bindView(history.review);
			if (mCreated != null) {
				mCreated.setText(DateFormat.format("yyyy/MM/dd", history.created * 1000));
			}
		}
	}
}
