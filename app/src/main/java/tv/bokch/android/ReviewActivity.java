package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.History;
import tv.bokch.data.UserViewHolder;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.ReviewView;

public class ReviewActivity extends TabActivity {

	public static final int INDEX_BOOKS = 0;
	public static final int INDEX_USERS = 1;

	private ReviewView mContent;
	private History mHistory;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_review);
		super.onCreate(savedInstanceState);

		setActionBarTitle(getString(R.string.title_reviews));
		Intent intent = getIntent();
		mHistory = intent.getParcelableExtra("data");
		if (mHistory == null) {
			finish();
			return;
		}

		initialize();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mHistory = intent.getParcelableExtra("data");
		if (mHistory == null || mHistory.book == null || mHistory.user == null) {
			finish();
			return;
		}
		mContent.bindView(mHistory);
	}

	private void initialize() {
		mContent = (ReviewView)findViewById(R.id.review);
		assert mContent != null;
		mContent.bindView(mHistory);
		BookViewHolder book = new BookViewHolder(mContent);
		book.setOnJacketClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBookActivity(mHistory.book);
			}
		});
		UserViewHolder user = new UserViewHolder(mContent);
		user.setOnIconClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startUserActivity(mHistory.user);
			}
		});
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_BOOKS:
			return UserReviewFragment.newInstance();
		case INDEX_USERS:
			return BookReviewFragment.newInstance();
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
		case INDEX_BOOKS:
			return getString(R.string.title_users_with_this_book);
		case INDEX_USERS:
			return getString(R.string.title_books_with_this_user);
		default:
			return null;
		}
	}
	
	@Override
	protected void requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_BOOKS:
			request.recent(mHistory.book.bookId, null, listener);
			break;
		case INDEX_USERS:
			request.recent(null, mHistory.user.userId, listener);
			break;
		default:
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_BOOKS:
		case INDEX_USERS:
			return "histories";
		default:
			return null;
		}
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_BOOKS:
			ArrayList<History> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null) {
						history.book = mHistory.book;
						histories.add(history);
					}
				}
			}
			Collections.reverse(histories);
			return histories;
		case INDEX_USERS:
			ArrayList<History> historiess = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null && !TextUtils.isEmpty(history.book.title)) {
						history.user = mHistory.user;
						historiess.add(history);
					}
				}
			}
			Collections.reverse(historiess);
			return historiess;
		default:
			return null;
		}
	}
}
