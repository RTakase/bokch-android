package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.User;

public class FullUserRankingListView extends RankingListView<User> {

	public FullUserRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public FullUserRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public FullUserRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		setOrientation(Orientation.Vertical);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.row_full_user_ranking;
	}

	@Override
	protected Row createRow(View view) {
		return new FullUserRankingRow(view);
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

	protected class FullUserRankingRow extends RankingRow {
		private TextView mUserName;
		private NetworkImageView mUserIcon;
		private TextView mTag;
		private TextView mScore;
		private Button mMoreButton;

		public FullUserRankingRow(View view) {
			super(view);
			mUserName = (TextView)view.findViewById(R.id.user_name);
			mUserIcon = (NetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
			mTag = (TextView)view.findViewById(R.id.tag);
			mScore = (TextView)view.findViewById(R.id.score);
			mMoreButton = (Button)view.findViewById(R.id.more_btn);
		}

		public void bindView(User user, int position) {
			super.bindView(user, position);
			mUserName.setText(String.format("ユーザ名：%s", user.name));
			mUserIcon.setImageUrl(user.iconUrl);
			if (TextUtils.isEmpty(user.tag)) {
				mTag.setVisibility(View.GONE);
			} else {
				mTag.setText(user.tag);
			}
			if (user.score <= 0) {
				mScore.setVisibility(View.GONE);
			} else {
				mScore.setText(String.format("スコア：%d", user.score));
			}
			mMoreButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Timber.d("tks, hoge");
				}
			});
		}
	}
}
