package tv.bokch.widget;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import timber.log.Timber;
import tv.bokch.R;
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
		return R.layout.row_full_recent;
	}

	@Override
	protected Row createRow(View view) {
		return new FullRecentRow(view);
	}

	@Override
	protected int getFooterResId() {
		return R.layout.row_footer;
	}

	@Override
	protected Row createFooterRow(View view) {
		return new DummyRow(view);
	}
	
	@Override
	protected int getHeaderResId() {
		return R.layout.row_header;
	}
	
	@Override
	protected Row createHeaderRow(View view) {
		return new DummyRow(view);
	}
	
	protected class FullRecentRow extends RecentRow {
		private TextView mComment;
		private TextView mRating;
		private Button mMoreButton;
		private TextView mCreated;
		private TextView mUserName;
		
		public FullRecentRow(View view) {
			super(view);
			mComment = (TextView)view.findViewById(R.id.comment);
			mRating = (TextView)view.findViewById(R.id.rating);
			mCreated = (TextView)view.findViewById(R.id.created);
			mUserName = (TextView)view.findViewById(R.id.user_name);
			mMoreButton = (Button)view.findViewById(R.id.more_btn);
		}
		
		public void bindView(History history, int position) {
			super.bindView(history, position);
			mComment.setText(String.format("コメント：%s", history.review.comment));
			mRating.setText(String.format("評価：%s", history.review.rating));
			mCreated.setText(String.format("登録日時：%s", DateFormat.format("yy/MM kk", history.created)));
			mUserName.setText(String.format("ユーザ名：%s", history.user.name));
			mMoreButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Timber.d("tks, hoge");
				}
			});
		}
	}
}
