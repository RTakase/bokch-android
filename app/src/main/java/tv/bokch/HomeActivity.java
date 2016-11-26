package tv.bokch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import timber.log.Timber;

public class HomeActivity extends BaseActivity {

	private RankingListView mRecentListView;
	private RankingListView mUserRankingListView;
	private RankingListView mBookRankingListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_home);

		super.onCreate(savedInstanceState);

		mRecentListView = initRankingView(R.id.recent, R.string.ranking_recent_title);
		mBookRankingListView = initRankingView(R.id.ranking_book_weekly, R.string.ranking_book_weekly_title);
		mUserRankingListView = initRankingView(R.id.ranking_user_weekly, R.string.ranking_user_weekly_title);

		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					mRecentListView.addData(1);
					mUserRankingListView.addData(1);
					mBookRankingListView.addData(1);
				} catch (NullPointerException e) {
					Timber.w(e, null);
				}
			}
		});
	}

	private RankingListView initRankingView(int layoutResId, int titleResId) {
		View partial = findViewById(layoutResId);
		if (partial == null) {
			return null;
		}
		RankingListView listview = (RankingListView)partial.findViewById(R.id.listview);
		TextView titleView = (TextView)partial.findViewById(R.id.header_text);

		String title = getString(titleResId);
		titleView.setText(title);
		return listview;
	}
}
