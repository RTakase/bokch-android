package tv.bokch.android;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.ViewUtils;
import tv.bokch.widget.UserView;

public class UserActivity extends TabActivity {
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_STACK = 1;
	
	private User mUser;
	private UserView mUserView;
	private boolean mFollowed;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_user);
		super.onCreate(savedInstanceState);

		setActionBarTitle(getString(R.string.title_books_with_this_user));
		
		Intent intent = getIntent();
		mUser = intent.getParcelableExtra("data");
		if (mUser == null) {
			finish();
			return;
		}

		mMyUser = ((App)getApplicationContext()).getMyUser();

		mFollowed = intent.getBooleanExtra("follow", false);
		
		initialize();
	}
	
	private void initialize() {
		mUserView = (UserView)findViewById(R.id.user);
		assert mUserView != null;
		mUserView.bindView(mUser);
		mUserView.setFollowed(mFollowed);
		mUserView.setFollowClickListener(true, mFollowClickListener);
		mUserView.setFollowClickListener(false, mUnfollowClickListener);
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return BookReviewFragment.newInstance();
		case INDEX_STACK:
			return BookFragment.newInstance();
		default:
			return null;
		}
	}
	
	@Override
	protected int getTabCount() {
		return 2;
	}
	
	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return getString(R.string.title_reviews);
		case INDEX_STACK:
			return getString(R.string.title_books_with_this_user);
		default:
			return null;
		}
	}
	
	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_REVIEW:
			request.recent(null, mUser.userId, null, listener);
			return true;
		case INDEX_STACK:
			request.recent(null, mUser.userId, null, listener);
			return true;
		default:
			return false;
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return "histories";
		case INDEX_STACK:
			return "histories";
		default:
			return null;
		}
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_REVIEW:
			ArrayList<History> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null && !TextUtils.isEmpty(history.book.title)) {
						history.user = mUser;
						histories.add(history);
					}
				}
			}
			Collections.reverse(histories);
			return histories;
		case INDEX_STACK:
			ArrayList<Book> books = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null && !TextUtils.isEmpty(history.book.title)) {
						books.add(history.book);
					}
				}
			}
			return books;
		default:
			return null;
		}
	}
	
	
	private View.OnClickListener mFollowClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ApiRequest request = new ApiRequest();
			try {
				showSpinner();
				request.follow(mUser.userId, mMyUser.userId, mFollowApiListener);
			} catch (JSONException e) {
				Timber.w(e, null);
			}
		}
	};
	
	private View.OnClickListener mUnfollowClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ApiRequest request = new ApiRequest();
			try {
				showSpinner();
				request.unfollow(mUser.userId, mMyUser.userId, mUnfollowApiListener);
			} catch (JSONException e) {
				Timber.w(e, null);
			}
		}
	};

	private ApiRequest.ApiListener<JSONObject> mFollowApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			mUserView.setFollowed(true);
			ViewUtils.showSuccessToast(UserActivity.this, R.string.succeed_follow);
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			dismissSpinner();
			Timber.w(error, null);
			ViewUtils.showErrorToast(UserActivity.this, R.string.failed_load);
		}
	};
	
	private ApiRequest.ApiListener<JSONObject> mUnfollowApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			mUserView.setFollowed(false);
			ViewUtils.showSuccessToast(UserActivity.this, R.string.succeed_unfollow);
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			dismissSpinner();
			Timber.w(error, null);
			ViewUtils.showErrorToast(UserActivity.this, R.string.failed_load);
		}
	};
}
