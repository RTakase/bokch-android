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
import java.util.Collections;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.History;
import tv.bokch.util.ApiRequest;

import tv.bokch.widget.BaseListView;
import tv.bokch.widget.FullRecentListView;

public class RecentActivity extends ListActivity<History> {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_list);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.acitivity_title_recent);
	}
	
	@Override
	protected BaseListView<History> createListView() {
		return new FullRecentListView(this);
	}

	@Override
	protected void request(ApiRequest.ApiListener<JSONObject> listener) {
		ApiRequest r = new ApiRequest();
		r.recent(listener);
	}

	@Override
	protected String getKey() {
		return "histories";
	}

	@Override
	protected ArrayList<History> getData(JSONArray array) throws JSONException {
		ArrayList<History> histories = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			if (obj != null) {
				History history = new History(obj);
				//たまに取得できないやつがいるのでスルー
				if (!TextUtils.isEmpty(history.book.title)) {
					histories.add(history);
				}
			}
		}
		Collections.reverse(histories);
		return histories;
	}
}
