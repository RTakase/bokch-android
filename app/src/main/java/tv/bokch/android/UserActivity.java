package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.data.Review;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.UserView;

public class UserActivity extends TabActivity {
	
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_STACK = 1;
	
	private User mUser;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_user);
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mUser = intent.getParcelableExtra("data");
		if (mUser == null) {
			finish();
			return;
		}
		
		initialize();
	}
	
	private void initialize() {
		UserView user = (UserView)findViewById(R.id.user);
		assert user != null;
		user.bindView(mUser);
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return ReviewFragment.newInstance();
		case INDEX_STACK:
			return ReviewFragment.newInstance();
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
			return getString(R.string.title_histories);
		case INDEX_STACK:
			return getString(R.string.title_stack);
		default:
			return null;
		}
	}
	
	@Override
	protected void requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_REVIEW:
			request.review(null, mUser.userId, listener);
			break;
		case INDEX_STACK:
			request.review(null, mUser.userId, listener);
			break;
		default:
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return "reviews";
		case INDEX_STACK:
			return "reviews";
		default:
			return null;
		}
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_REVIEW:
			ArrayList<Review> reviews = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Review review = new Review(obj);
					reviews.add(review);
				}
			}
			return reviews;
		case INDEX_STACK:
			ArrayList<Review> reviews2 = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Review review = new Review(obj);
					reviews2.add(review);
				}
			}
			return reviews2;
		default:
			return null;
		}
	}
}
