package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.StatableListView;

public abstract class ListActivity<Data> extends BaseActivity {
	private StatableListView<Data> mContent;
	private boolean mLoaded;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContent = (StatableListView<Data>)findViewById(R.id.content);
		mContent.addListView(createListView());
	}

	protected abstract BaseListView<Data> createListView();
	protected abstract boolean request(ApiRequest.ApiListener<JSONObject> listener);
	protected abstract String getKey();
	protected abstract ArrayList<Data> getData(JSONArray array) throws JSONException;
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!mLoaded) {
			if (request(mApiListener)) {
				mContent.setState(StatableListView.State.Loading);
			}
		}
	}

	protected void setData(ArrayList<Data> data) {
		mLoaded = mContent.onData(data);
	}

	private ApiRequest.ApiListener<JSONObject> mApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray(getKey());
				if (array == null) {
					return;
				}
				setData(getData(array));
			} catch (JSONException e) {
				mContent.setState(StatableListView.State.Failed);
				Toast.makeText(ListActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			mContent.setState(StatableListView.State.Failed);
			Toast.makeText(ListActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	};
}
