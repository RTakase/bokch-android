package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.util.ApiRequest;

public class BookRankingActivity extends BaseActivity {
	
	public static final int INDEX_WEEKLY = 0;
	public static final int INDEX_TOTAL = 1;
	public static final int NUMBER_OF_RANKINGS = 2;
	
	private int[] mKinds;
	private BookRankingFragment[] mPages;
	private boolean[] mLoaded;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_ranking);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.acitivity_title_book_ranking);
		
		mPages = new BookRankingFragment[NUMBER_OF_RANKINGS];
		mLoaded = new boolean[NUMBER_OF_RANKINGS];
		mKinds = new int[NUMBER_OF_RANKINGS];
		
		mKinds[0] = INDEX_WEEKLY;
		mKinds[1] = INDEX_TOTAL;
		
		Intent intent = getIntent();
		ArrayList<Book> data = intent.getParcelableArrayListExtra("data");
		initViews(data);
	}
	
	private void initViews(ArrayList<Book> data) {
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		assert pager != null;
		pager.setAdapter(mAdapter);
		
		TabLayout tab = (TabLayout)findViewById(R.id.tab);
		assert tab != null;
		tab.setupWithViewPager(pager);
		
		for (int i = 0; i < NUMBER_OF_RANKINGS; i++) {
			switch (i) {
			case INDEX_WEEKLY:
				mPages[i] = BookRankingFragment.newInstance(data);
				tab.getTabAt(i).setText(R.string.ranking_weekly);
				mLoaded[i] = true;
				break;
			case INDEX_TOTAL:
				mPages[i] = BookRankingFragment.newInstance();
				tab.getTabAt(i).setText(R.string.ranking_total);
				mLoaded[i] = false;
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData() {
		for (int i = 0; i < mKinds.length; i++) {
			int kind = mKinds[i];
			if (!mLoaded[kind]) {
				requestRanking(kind, new RankingApiListener(kind));
			}
		}
	}
	
	private void requestRanking(int kind, RankingApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (kind) {
		case INDEX_WEEKLY:
			request.ranking_book_weekly(listener);
			break;
		case INDEX_TOTAL:
			request.ranking_book_total(listener);
			break;
		}
	}
	
	private class RankingApiListener implements ApiRequest.ApiListener<JSONObject> {
		private int mKind;
		
		public RankingApiListener(int kind) {
			mKind = kind;
		}
		
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("books");
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<Book> books = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						Book book = new Book(obj);
						//たまに取得できないやつがいるのでスルー
						if (!TextUtils.isEmpty(book.title)) {
							books.add(book);
						}
					}
				}
				mLoaded[mKind] = mPages[mKind].onData(books);
			} catch (JSONException e) {
				Toast.makeText(BookRankingActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(BookRankingActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	}
	
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
}
