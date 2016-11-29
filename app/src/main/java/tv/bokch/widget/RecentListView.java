package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.History;

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
		protected BookView.BookViewHolder mBook;
		private CircleNetworkImageView mUserIcon;

		public RecentCell(View view) {
			super(view);
			mBook = new BookView.BookViewHolder(view);
			mUserIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
		}

		public void bindView(History history, int position) {
			super.bindView(history, position);
			mBook.bindView(history.book);
			mUserIcon.setImageUrl(history.user.iconUrl);
		}
	}
}
