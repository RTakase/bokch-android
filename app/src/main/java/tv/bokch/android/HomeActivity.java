package tv.bokch.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.History;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;

public class HomeActivity extends TabActivity {
	protected static final int MENU_ID_USERS = Menu.FIRST + 1;
	protected static final int MENU_ID_RANKING = Menu.FIRST + 2;
	protected static final int MENU_ID_MYPAGE = Menu.FIRST + 3;

	protected static final int INDEX_HISTORY = 0;
	protected static final int INDEX_FOLLOW = 1;

	private boolean mDisableLoad;
	private User mLoginUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_home);

		super.onCreate(savedInstanceState);

		SharedPreferences pref = this.getSharedPreferences("bokch", MODE_PRIVATE);

		String myUserId = pref.getString("user_id", null);
		Timber.d("tks, userId = %s", myUserId);

		mToolbar.setNavigationIcon(null);
		setActionBarImage(R.drawable.logo_white7);

		mDisableLoad = true;

		if (TextUtils.isEmpty(myUserId)) {
			//ログイン情報がないので先にログインさせる
			startLoginActivity(HomeActivity.this);
		} else {
			//ログインする
			showSpinner();
			ApiRequest request = new ApiRequest();
			request.login(myUserId, null, mLoginApiListener);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		addMenuItem(menu, MENU_ID_MYPAGE, R.string.action_mypage, R.drawable.ic_action_mypage, MenuItem.SHOW_AS_ACTION_ALWAYS);
		addMenuItem(menu, MENU_ID_USERS, R.string.activity_all_users, R.drawable.ic_action_member, MenuItem.SHOW_AS_ACTION_ALWAYS);
		addMenuItem(menu, MENU_ID_RANKING, R.string.action_ranking, R.drawable.ranking, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_USERS:
			startUserListActivity(getString(R.string.activity_all_users));
			return true;
		case MENU_ID_RANKING:
			startRankingActivity();
			return true;
		case MENU_ID_MYPAGE:
			startMyPageActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void loadTabData() {
		if (!mDisableLoad) {
			super.loadTabData();
		}
	}

	@Override
	protected int getTabCount() {
		return 2;
	}

	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return getString(R.string.title_recent);
		case INDEX_FOLLOW:
			return getString(R.string.title_follower);
		default:
			return "";
		}
	}

	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return RecentFragment.newInstance();
		case INDEX_FOLLOW:
			return FollowerRecentFragment.newInstance();
		default:
			return null;
		}
	}

	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		switch (index) {
		case INDEX_HISTORY:
			ApiRequest request = new ApiRequest();
			request.recent(null, null, mLoginUser.userId, listener);
			return true;
		case INDEX_FOLLOW:
			return false;
		default:
			return false;
		}
	}

	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		if (index == INDEX_HISTORY) {
			ArrayList<History> histories = new ArrayList<>();
			ArrayList<History> followeeHistories = new ArrayList<>();

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (!TextUtils.isEmpty(history.book.title) && history.review != null) {
						//コメントか評価のどちらかがあれば追加する
						if (!TextUtils.isEmpty(history.review.comment) || history.review.rating > 0) {
							histories.add(history);
							boolean myFollowee = obj.optBoolean("my_followee");
							if (myFollowee) {
								followeeHistories.add(history);
							}
						}
					}
				}
			}
			setData(INDEX_FOLLOW, followeeHistories);
			return histories;
		} else {
			return null;
		}
	}

	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_FOLLOW:
		case INDEX_HISTORY:
			return "histories";
		default:
			return "";
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
		case REQUEST_LOGIN:
			if (resultCode == RESULT_OK) {
				App app = (App)getApplication();
				mLoginUser = app.getMyUser();
				mDisableLoad = false;
				loadTabData();
			} else {
				finish();
			}
		}
	}

	private ApiRequest.ApiListener<JSONObject> mLoginApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			try {
				if (response.isNull("user")) {
					//不正なログイン情報だったのでもう１回ログインさせる
					startLoginActivity(HomeActivity.this);
				} else {
					//ログイン成功なのでコンテンツのロードを開始する
					App app = (App)getApplication();
					mLoginUser = new User(response.optJSONObject("user"));
					app.setMyUser(mLoginUser);
					mDisableLoad = false;
					loadTabData();
				}
			} catch (JSONException e) {
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			dismissSpinner();
			Toast.makeText(HomeActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			Timber.w(error, null);
		}
	};
}
