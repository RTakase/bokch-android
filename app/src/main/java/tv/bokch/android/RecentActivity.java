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
import tv.bokch.widget.FullRecentListView;
import tv.bokch.widget.StatableListView;

public class RecentActivity extends BaseActivity {
	private StatableListView<History> mContent;
	private boolean mLoaded;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		setContentView(R.layout.activity_list);

		super.onCreate(savedInstanceState);

		setActionBarTitle(R.string.acitivity_title_recent);

		Intent intent = getIntent();
		ArrayList<History> data = intent.getParcelableArrayListExtra("data");
		
		mContent = (StatableListView<History>)findViewById(R.id.content);
		FullRecentListView listview = new FullRecentListView(this);
		mContent.addListView(listview);
		mLoaded = mContent.onData(data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!mLoaded) {
			ApiRequest request = new ApiRequest();
			request.recent(mRecentApiListener);
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
				Collections.reverse(histories);
				mLoaded = mContent.onData(histories);
			} catch (JSONException e) {
				Toast.makeText(RecentActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(RecentActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	};
}
