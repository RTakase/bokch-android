package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.Review;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BookView;

public class BookActivity extends TabActivity {

	public static final int REQUEST_REVIEW_EDIT = 1;
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_USERS = 1;

	private Book mBook;
	private Review mReview;
	private Review mEditingReview;
	private History mHistory;
	
	private Button mNewReviewButton;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_book);
		super.onCreate(savedInstanceState);

		setActionBarTitle(getString(R.string.activity_book));

		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		
		mReview = intent.getParcelableExtra("review");
		mHistory = intent.getParcelableExtra("history");

		boolean withReviewEdit = intent.getBooleanExtra("with_review_edit", false);
		if (withReviewEdit) {
			editReview();
		} else {
			boolean withMyReview = intent.getBooleanExtra("with_my_review", false);
			if (withMyReview) {

			}
		}

		initialize();
	}

	private void initialize() {
		BookView book = (BookView)findViewById(R.id.book);
		assert book != null;
		book.bindView(mBook);

		//情報取得中なのでビューを変える
		book.setEmpty(TextUtils.isEmpty(mBook.title));

		mNewReviewButton = (Button)findViewById(R.id.new_review_btn);
		assert mNewReviewButton != null;
		mNewReviewButton.setOnClickListener(mReviewClickListener);

		setReviewButtonVisibility();
	}

	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return UserReviewFragment.newInstance();
		case INDEX_USERS:
			return UserFragment.newInstance();
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
		case INDEX_REVIEW:
			return getString(R.string.title_reviews);
		case INDEX_USERS:
			return getString(R.string.title_users_with_this_book);
		default:
			return null;
		}
	}

	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_REVIEW:
			request.recent(mBook.bookId, null, null, listener);
			return true;
		case INDEX_USERS:
			request.recent(mBook.bookId, null, null, listener);
			return true;
		default:
			return false;
		}
	}

	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_REVIEW:
		case INDEX_USERS:
			return "histories";
		default:
			return null;
		}
	}

	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_REVIEW:
			ArrayList<History> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null) {
						history.book = mBook;
						histories.add(history);
					}
				}
			}
			Collections.reverse(histories);
			return histories;
		case INDEX_USERS:
			ArrayList<User> users = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					history.book = mBook;
					users.add(history.user);
				}
			}
			Collections.reverse(users);
			return users;
		default:
			return null;
		}
	}

	private void setReviewButtonVisibility() {
		if (mReview == null) {
			mNewReviewButton.setVisibility(View.VISIBLE);
		} else {
			mNewReviewButton.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_REVIEW_EDIT:
			if (resultCode == RESULT_OK) {
				Review review = data.getParcelableExtra("review");
				boolean saved = data.getBooleanExtra("saved", false);
				if (saved) {
					mReview = review;
				} else {
					mEditingReview = review;
				}
				setReviewButtonVisibility();
				//この後 TabActivityのonResumeが呼ばれるのでフラグをセットするだけ
				mLoaded[INDEX_REVIEW] = false;
			} else {
				Toast.makeText(BookActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	private View.OnClickListener mReviewClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			editReview();
		}
	};

	private void editReview() {
		App app = (App)getApplication();
		Review review = mEditingReview != null ? mEditingReview : mReview;
		ReviewEditDialog dialog = ReviewEditDialog.newInstance(mBook, app.getMyUser(), review, mHistory);
		dialog.setTargetFragment(null, REQUEST_REVIEW_EDIT);
		dialog.show(getFragmentManager(), "REVIEW_EDIT_DIALOG");
	}
}
