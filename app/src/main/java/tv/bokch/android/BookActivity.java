package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.Review;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.NetworkImageView;
import tv.bokch.widget.StatableListView;

public class BookActivity extends BaseActivity {
	
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_USERS = 1;
	public static final int NUMBER_OF_PAGES = 2;

	private Fragment[] mPages;
	private boolean[] mLoaded;
	private int[] mKinds;

	private Book mBook;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		initialize();
	}

	private void initialize() {
		NetworkImageView jacket = (NetworkImageView)findViewById(R.id.jacket);
		assert jacket != null;
		jacket.setDefaultImageResId(R.drawable.mysteryman);
		ViewGroup.LayoutParams params = jacket.getLayoutParams();
		if (params != null) {
			params.width = mDisplay.toPixels(mBook.largeImageWidth);
			params.height = mDisplay.toPixels(mBook.largeImageHeight);
			Timber.d("tks, w = %d, h = %d", params.width, params.height);
			jacket.setLayoutParams(params);
		}
		jacket.setImageUrl(mBook.largeImageUrl);

		TextView title = (TextView)findViewById(R.id.title);
		assert title != null;
		title.setText(mBook.title);

		TextView author = (TextView)findViewById(R.id.author);
		assert author != null;
		author.setText(mBook.author);

		TextView publisher = (TextView)findViewById(R.id.publisher);
		assert publisher != null;
		publisher.setText(mBook.publisher);

		RatingBar ratingAverage = (RatingBar)findViewById(R.id.rating_average);
		assert ratingAverage != null;
		ratingAverage.setRating(mBook.ratingAverage * 100);

		TextView tag = (TextView)findViewById(R.id.tag);
		assert tag != null;
		tag.setText(mBook.tag);

		Button add = (Button)findViewById(R.id.add_btn);
		assert add != null;
		add.setOnClickListener(mAddClickListener);

		mPages = new Fragment[NUMBER_OF_PAGES];
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		assert pager != null;
		pager.setAdapter(mAdapter);

		TabLayout tab = (TabLayout)findViewById(R.id.tab);

		assert tab != null;
		tab.setupWithViewPager(pager);

		mKinds = new int[NUMBER_OF_PAGES];
		mLoaded = new boolean[NUMBER_OF_PAGES];

		for (int i = 0; i < NUMBER_OF_PAGES; i++) {
			switch (i) {
			case INDEX_REVIEW:
				mPages[i] = ReviewFragment.newInstance();
				tab.getTabAt(i).setText(R.string.title_reviews);
				break;
			case INDEX_USERS:
				mPages[i] = BookRankingFragment.newInstance();
				tab.getTabAt(i).setText(R.string.title_read_users);
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_book, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_amazon:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBook.detailPageUrl));
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadData() {
		ApiRequest request = null;
		if (!mLoaded[INDEX_REVIEW]) {
			request = new ApiRequest();
			request.review(mBook.bookId, null, new ApiRequest.ApiListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						JSONArray array = response.optJSONArray("reviews");
						if (array == null) {
							return;
						}
						int length = array.length();
						ArrayList<Review> reviews = new ArrayList<>();
						for (int i = 0; i < length; i++) {
							JSONObject obj = array.optJSONObject(i);
							if (obj != null) {
								Review review = new Review(obj);
								reviews.add(review);
							}
						}
						mLoaded[INDEX_REVIEW] = ((ReviewFragment)mPages[INDEX_REVIEW]).onData(reviews);
					} catch (JSONException e) {
						((ReviewFragment)mPages[INDEX_REVIEW]).setState(StatableListView.State.Failed);
						Toast.makeText(BookActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
						Timber.w(e, null);
					}
				}
				
				@Override
				public void onError(ApiRequest.ApiError error) {
					Timber.w(error, null);
					((ReviewFragment)mPages[INDEX_REVIEW]).setState(StatableListView.State.Failed);
					Toast.makeText(BookActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				}
			});
		}

		if (!mLoaded[INDEX_USERS]) {
			request = new ApiRequest();
			request.recent(mBook.bookId, null, new ApiRequest.ApiListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject response) {

				}

				@Override
				public void onError(ApiRequest.ApiError error) {

				}
			});
		}
	}

	private View.OnClickListener mAddClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(BookActivity.this, "登録しちゃいましょう。", Toast.LENGTH_SHORT).show();
		}
	};

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
