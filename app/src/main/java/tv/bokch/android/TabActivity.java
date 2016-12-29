package tv.bokch.android;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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

public abstract class TabActivity extends FabActivity {
	protected BaseFragment[] mPages;
	protected boolean[] mLoaded;
	protected int[] mIndexes;
	protected int mTabCount;

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		initTabs();
	}

	protected void initTabs() {
		mTabCount = getTabCount();
		mPages = new BaseFragment[mTabCount];
		mLoaded = new boolean[mTabCount];
		mIndexes = new int[mTabCount];

		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		assert pager != null;

		TabLayout tab = (TabLayout)findViewById(R.id.tab);
		assert tab != null;
		pager.setAdapter(mAdapter);
		tab.setupWithViewPager(pager);

		for (int i = 0; i < mTabCount; i++) {
			mPages[i] = createFragment(i);
			tab.getTabAt(i).setText(getTabTitle(i));
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mLoaded = new boolean[mTabCount];
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLoaded = new boolean[mTabCount];
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mLoaded = new boolean[mTabCount];
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadTabData();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected void loadTabData() {
		for (int i = 0; i < mTabCount; i++) {
			if (!mLoaded[i]) {
				TabApiListener listener = new TabApiListener(i);
				if (requestData(i, listener)) {
					mPages[i].setState(StatableListView.State.Loading);
				}
			}
		}
	}
	
	protected void setData(int index, ArrayList<?> array) {
		mLoaded[index] = mPages[index].onData(array);
	}

	protected abstract int getTabCount();
	protected abstract String getTabTitle(int index);
	protected abstract BaseFragment createFragment(int index);
	protected abstract boolean requestData(int index, TabApiListener listener);
	protected abstract ArrayList<?> getData(int index, JSONArray array) throws JSONException;
	protected abstract String getKey(int index);

	//FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
	FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
				setData(mIndex, getData(mIndex, array));
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
