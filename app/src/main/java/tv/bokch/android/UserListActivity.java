package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.History;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.StatableListView;
import tv.bokch.widget.UserListView;

public class UserListActivity extends BaseActivity {
	private StatableListView<User> mContent;
	private boolean mLoaded;
	private String mBookId;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_list);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.acitivity_title_user_list);
		
		Intent intent = getIntent();
		mBookId = intent.getStringExtra("book_id");
		if (TextUtils.isEmpty(mBookId)) {
			finish();
			return;
		}
		mContent = (StatableListView<User>)findViewById(R.id.content);
		UserListView listview = new UserListView(this);
		mContent.addListView(listview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!mLoaded) {
			ApiRequest request = new ApiRequest();
			request.recent(mBookId, null, mRecentApiListener);
		}
	}
	
	private ApiRequest.ApiListener<JSONObject> mRecentApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("histories");
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<User> users = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						History history = new History(obj);
						//たまに取得できないやつがいるのでスルー
						if (!TextUtils.isEmpty(history.book.title)) {
							users.add(history.user);
						}
					}
				}
				mLoaded = mContent.onData(users);
			} catch (JSONException e) {
				Toast.makeText(UserListActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(UserListActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	};
}
