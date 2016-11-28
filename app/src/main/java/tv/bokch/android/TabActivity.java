package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.StatableListView;

public abstract class TabActivity extends BaseActivity {
	protected BaseFragment[] mPages;
	protected boolean[] mLoaded;
	protected int[] mIndexes;
	protected int mTabCount;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTabs();
	}

	protected void initTabs() {
		mTabCount = getTabCount();
		mPages = new BaseFragment[mTabCount];
		mLoaded = new boolean[mTabCount];
		mIndexes = new int[mTabCount];

		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		assert pager != null;
		pager.setAdapter(mAdapter);
		
		TabLayout tab = (TabLayout)findViewById(R.id.tab);
		assert tab != null;
		tab.setupWithViewPager(pager);

		for (int i = 0; i < mTabCount; i++) {
			mPages[i] = createFragment(i);
			tab.getTabAt(i).setText(getTabTitle(i));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadTabData();
	}

	protected void loadTabData() {
		for (int i = 0; i < mTabCount; i++) {
			if (!mLoaded[i]) {
				TabApiListener listener = new TabApiListener(i);
				requestData(i, listener);
			}
		}
	}

	protected abstract int getTabCount();
	protected abstract String getTabTitle(int index);
	protected abstract BaseFragment createFragment(int index);
	protected abstract void requestData(int index, TabApiListener listener);
	protected abstract ArrayList<?> getData(int index, JSONArray array) throws JSONException;
	protected abstract String getKey(int index);

	FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int position) {
			return mPages[position];
		}
		@Override
		public int getCount() {
			return mPages.length;
		}
	};
	
	protected class TabApiListener implements ApiRequest.ApiListener<JSONObject> {
		private int mIndex;
		public TabApiListener(int index) {
			this.mIndex = index;
		}

		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray(getKey(mIndex));
				if (array == null) {
					return;
				}
				ArrayList<?> results = getData(mIndex, array);
				mLoaded[mIndex] = mPages[mIndex].onData(results);
			} catch (JSONException e) {
				mPages[mIndex].setState(StatableListView.State.Failed);
				Toast.makeText(TabActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			mPages[mIndex].setState(StatableListView.State.Failed);
			Toast.makeText(TabActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	}	
}
