package tv.bokch.widget;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.android.UserListActivity;
import tv.bokch.data.History;

public class FullRecentListView extends RecentListView {

	public FullRecentListView(Context context) {
		super(context);
		initialize(context);
	}
	public FullRecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public FullRecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		setOrientation(Orientation.Vertical);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.cell_full_recent;
	}

	@Override
	protected Cell createCell(View view) {
		return new FullRecentCell(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected Cell createFooter(View view) {
		return new DummyCell(view);
	}
	
	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}
	
	@Override
	protected Cell createHeader(View view) {
		return new DummyCell(view);
	}
	
	protected class FullRecentCell extends RecentCell {
		private TextView mComment;
		private TextView mRating;
		private Button mMoreButton;
		private TextView mCreated;
		private TextView mUserName;
		
		public FullRecentCell(View view) {
			super(view);
			mComment = (TextView)view.findViewById(R.id.comment);
			mRating = (TextView)view.findViewById(R.id.rating);
			mCreated = (TextView)view.findViewById(R.id.created);
			mUserName = (TextView)view.findViewById(R.id.user_name);
			mMoreButton = (Button)view.findViewById(R.id.more_btn);
		}
		
		public void bindView(final History history, int position) {
			super.bindView(history, position);
			mComment.setText(String.format("コメント：%s", history.review.comment));
			mRating.setText(String.format("評価：%s", history.review.rating));
			mCreated.setText(String.format("登録日時：%s", DateFormat.format("yy/MM kk", history.created)));
			mUserName.setText(String.format("ユーザ名：%s", history.user.name));
			mMoreButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), UserListActivity.class);
					intent.putExtra("data", getData());
					intent.putExtra("book_id", history.book.bookId);
					getContext().startActivity(intent);
				}
			});
		}
	}
}
