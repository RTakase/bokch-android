package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BookRankingListView;
import tv.bokch.widget.RecentListView;
import tv.bokch.widget.UserRankingListView;

public class HomeActivity extends BaseActivity {

	protected static final int MENU_ID_SEARCH = Menu.FIRST + 1;
	protected static final int MENU_ID_MYPAGE = Menu.FIRST + 2;
	public static final int REQUEST_CODE_CAMERA = 1;

	private RecentListView mRecentListView;
	private UserRankingListView mUserRankingListView;
	private BookRankingListView mBookRankingListView;

	private CameraDialog mCameraDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_home);

		super.onCreate(savedInstanceState);

		initViews();
	}

	private void initViews() {
		View partial;
		partial = findViewById(R.id.recent);
		mRecentListView = (RecentListView)partial.findViewById(R.id.listview);
		initHeader(partial, R.string.ranking_recent_title, mRecentMoreClickListener);
		
		partial = findViewById(R.id.ranking_user_weekly);
		mUserRankingListView = (UserRankingListView)partial.findViewById(R.id.listview);
		initHeader(partial, R.string.ranking_user_weekly_title, null);
		
		partial = findViewById(R.id.ranking_book_weekly);
		mBookRankingListView = (BookRankingListView)partial.findViewById(R.id.listview);
		initHeader(partial, R.string.ranking_book_weekly_title, null);
		
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(mFabClickListener);
	}

	private void initHeader(View partial, int resId, View.OnClickListener listener) {
		if (resId > 0) {
			TextView titleView = (TextView)partial.findViewById(R.id.header_text);
			titleView.setText(getString(resId));
		}
		if (listener != null) {
			View button = partial.findViewById(R.id.header_more_btn);
			button.setOnClickListener(listener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadRankings();
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
				String str = String.format("ISBNコードをよみとりました！（%s）", isbn);
				Toast.makeText(this,  str, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		addMenuItem(menu, MENU_ID_MYPAGE, R.string.action_mypage, R.drawable.mypage, MenuItem.SHOW_AS_ACTION_ALWAYS);
		addMenuItem(menu, MENU_ID_SEARCH, R.string.action_search, R.drawable.search, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	private void loadRankings() {
		ApiRequest request = new ApiRequest();
		request.recent(mRecentApiListener);
		request.ranking_user_weekly(mUserRankingApiListener);
		request.ranking_book_weekly(mBookRankingApiListener);		
	}

	private View.OnClickListener mFabClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mCameraDialog = CameraDialog.newInstance();
			mCameraDialog.setTargetFragment(null, REQUEST_CODE_CAMERA);
			mCameraDialog.show(getFragmentManager(), "CameraDialog");
		}
	};

	private View.OnClickListener mRecentMoreClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//star
		}
	};


	private ApiRequest.ApiListener<JSONObject> mRecentApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("histories");
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<History> histories = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						History history = new History(obj);
						//たまに取得できないやつがいるのでスルー
						if (!TextUtils.isEmpty(history.book.title)) {
							histories.add(history);
						}
					}
				}
				mRecentListView.setData(histories);
			} catch (JSONException e) {
				Toast.makeText(HomeActivity.this, getString(R.string.unexpected_data_found), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Timber.d("tks, api error, %s", error.getLocalizedMessage());
		}
	};


	private ApiRequest.ApiListener<JSONObject> mBookRankingApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("books");
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<Book> books = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						Book book = new Book(obj);
						//たまに取得できないやつがいるのでスルー
						if (!TextUtils.isEmpty(book.title)) {
							books.add(book);
						}
					}
				}
				mBookRankingListView.setData(books);
			} catch (JSONException e) {
				Toast.makeText(HomeActivity.this, getString(R.string.unexpected_data_found), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Timber.d("tks, api error, %s", error.getLocalizedMessage());
		}
	};
	
	private ApiRequest.ApiListener<JSONObject> mUserRankingApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("users");
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<User> users = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						User user = new User(obj);
						users.add(user);
					}
				}
				mUserRankingListView.setData(users);
			} catch (JSONException e) {
				Toast.makeText(HomeActivity.this, getString(R.string.unexpected_data_found), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Timber.d("tks, api error, %s", error.getLocalizedMessage());
		}
	};
}
