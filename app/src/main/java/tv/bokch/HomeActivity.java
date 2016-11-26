package tv.bokch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import timber.log.Timber;

public class HomeActivity extends BaseActivity {

	public static final int REQUEST_CODE_CAMERA = 1;

	private RankingListView mRecentListView;
	private RankingListView mUserRankingListView;
	private RankingListView mBookRankingListView;

	private CameraDialog mCameraDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_home);

		super.onCreate(savedInstanceState);

		initViews();
	}

	private void initViews() {
		mRecentListView = initRankingView(R.id.recent, R.string.ranking_recent_title);
		mBookRankingListView = initRankingView(R.id.ranking_book_weekly, R.string.ranking_book_weekly_title);
		mUserRankingListView = initRankingView(R.id.ranking_user_weekly, R.string.ranking_user_weekly_title);
		for (int i = 0; i < 10; i++) {
			mRecentListView.addData(1);
			mUserRankingListView.addData(1);
			mBookRankingListView.addData(1);
		}

		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(mFabClickListener);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCameraDialog != null) {
			mCameraDialog.dismiss();
		}
		mCameraDialog = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String isbn = data.getStringExtra("barcode");
				Timber.d("tks, isbn = %s", isbn);
			}
		}
	}

	private View.OnClickListener mFabClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mCameraDialog = CameraDialog.newInstance();
			mCameraDialog.setTargetFragment(null, REQUEST_CODE_CAMERA);
			mCameraDialog.show(getFragmentManager(), "CameraDialog");
		}
	};
}
