package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.UserListView;

public class UserListActivity extends ListActivity<User> {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_list);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.title_all_users);
		
		Intent intent = getIntent();
	}

	@Override
	protected BaseListView<User> createListView() {
		return new UserListView(this);
	}

	@Override
	protected void request(ApiRequest.ApiListener<JSONObject> listener) {
		ApiRequest r = new ApiRequest();
		r.users(listener);
	}

	@Override
	protected String getKey() {
		return "users";
	}

	@Override
	protected ArrayList<User> getData(JSONArray array) throws JSONException {
		ArrayList<User> users = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject user = array.optJSONObject(i);
			if (user != null) {
				users.add(new User(user));
			}
		}
		return users;
	}
}
