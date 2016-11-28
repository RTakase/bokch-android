package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;

public class UserRankingActivity extends TabActivity {

	public static final int INDEX_WEEKLY = 0;
	public static final int INDEX_TOTAL = 1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_ranking);
		super.onCreate(savedInstanceState);
		setActionBarTitle(R.string.acitivity_title_user_ranking);
	}
	
	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_WEEKLY:
			return getString(R.string.ranking_weekly);
		case INDEX_TOTAL:
			return getString(R.string.ranking_total);
		default:
			return null;
		}
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			return UserRankingFragment.newInstance();
		default:
			return null;
		}
	}
	
	@Override
	protected void requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_WEEKLY:
			request.ranking_user_weekly(listener);
		case INDEX_TOTAL:
			request.ranking_user_total(listener);
		default:
		}
	}
	
	@Override
	protected int getTabCount() {
		return 2;
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			ArrayList<User> res = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					User user = new User(obj);
					if (user != null) {
						res.add(user);
					}
				}
			}
			return res;
		default:
			return null;
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			return "users";
		default:
			return null;
		}
	}
}
