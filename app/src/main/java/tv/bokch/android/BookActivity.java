package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import tv.bokch.data.MyBook;
import tv.bokch.data.Review;
import tv.bokch.data.Stack;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.JsonUtils;
import tv.bokch.util.ViewUtils;
import tv.bokch.widget.BookView;
import tv.bokch.widget.ShareButton;
import tv.bokch.widget.WishButton;

public class BookActivity extends TabActivity {
	
	public static final int REQUEST_REVIEW_EDIT = 1;
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_USERS = 1;
	
	private Book mBook;
	private Review mPostingReview;
	private MyBook mMyBook;
	private User mLoginUser;
	
	private WishButton mWishButton;
	private BookView mBookView;
	private ShareButton mShareButton;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_book);
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(getString(R.string.activity_book));
		
		mLoginUser = ((App)getApplication()).getMyUser();
		
		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		
//		mPostedReview = intent.getParcelableExtra("review");
//		mHistory = intent.getParcelableExtra("history");
//		mStack = intent.getParcelableExtra("stack");

		boolean withReviewEdit = intent.getBooleanExtra("with_review_edit", false);
		if (withReviewEdit) {
			editReview();
		}
		initialize();
	}
	
	private void initialize() {
		mBookView = (BookView)findViewById(R.id.book);
		assert mBookView != null;
		mBookView.bindView(mBook);
		
		//情報取得中なのでビューを変える
		mBookView.setEmpty(TextUtils.isEmpty(mBook.title));
		
		mShareButton = (ShareButton)findViewById(R.id.share_btn);
		mShareButton.setClickListener(mShareClickListener);

		mWishButton = (WishButton)findViewById(R.id.wish_btn);
		mWishButton.setClickListener(mWishClickListener);

		setButtonState();
	}
	
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return UserRecentFragment.newInstance();
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
			int ratingCount = 0;
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null) {
						history.book = mBook;
						//						for (int j = 0; j < 20; j++) {
						//						histories.add(history);
						//						}
						histories.add(history);
						if (history.review.rating > 0) {
							ratingCount++;
						}
					}
				}
			}
			
			Collections.reverse(histories);
			if (mBookView != null) {
				mBookView.setRatingAverageSuffix(String.format(getString(R.string.suffix_rating_average), ratingCount));
			}
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_REVIEW_EDIT:
			if (resultCode == RESULT_OK) {
				Review posted = data.getParcelableExtra("posted_review");
				Review posting = data.getParcelableExtra("posting_review");
				if (posted != null) {
					mMyBook.review = posted;
					mPostingReview = null;
					if (mShareButton != null) {
						mShareButton.setState(ShareButton.State.AFTER);
					}
					//この後 TabActivityのonResumeが呼ばれるのでフラグをセットするだけ
					mLoaded[INDEX_REVIEW] = false;
				} else if (posting != null) {
					mPostingReview = posting;
				}
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
	
	private void editReview() {
		App app = (App)getApplication();
		ReviewEditDialog dialog = ReviewEditDialog.newInstance(mBook, app.getMyUser(), mMyBook.review, mPostingReview, mMyBook.history);
		dialog.setTargetFragment(null, REQUEST_REVIEW_EDIT);
		dialog.show(getFragmentManager(), "REVIEW_EDIT_DIALOG");
	}
	
	private ShareButton.ClickListener mShareClickListener = new ShareButton.ClickListener() {
		@Override
		public void onClick(ShareButton.State state) {
			switch (state) {
			case BEFORE:
			case AFTER:
				editReview();
				break;
			}
		}
	};
	
	private WishButton.ClickListener mWishClickListener = new WishButton.ClickListener() {
		@Override
		public void onClick(WishButton.State state) {
			switch (state) {
			case BEFORE:
				addToWishList();
				break;
			case AFTER:
				deleteFromWishList();
				break;
			}
		}
	};
	
	private void addToWishList() {
		ApiRequest request = new ApiRequest();
		try {
			request.post_stack(mBook.bookId, mLoginUser.userId, new ApiRequest.ApiListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						mMyBook.stack = new Stack(response);
						mWishButton.setState(WishButton.State.AFTER);
						ViewUtils.showSuccessToast(BookActivity.this, R.string.message_posted_wish);
					} catch (JSONException e) {
						Timber.w(e, null);
					}
				}
				
				@Override
				public void onError(ApiRequest.ApiError error) {
					ViewUtils.showErrorToast(BookActivity.this, R.string.failed_load);
					Timber.w(error, null);
				}
			});
		} catch (JSONException e) {
			Timber.w(e, null);
		}
	}
	
	private void deleteFromWishList() {
		ApiRequest request = new ApiRequest();
		request.delete_stack(mMyBook.stack.id, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				mMyBook.stack = null;
				mWishButton.setState(WishButton.State.BEFORE);
				ViewUtils.showSuccessToast(BookActivity.this, R.string.message_deleted_wish);
			}
			
			@Override
			public void onError(ApiRequest.ApiError error) {
				ViewUtils.showErrorToast(BookActivity.this, R.string.failed_load);
				Timber.w(error, null);
			}
		});
	}

	private void setButtonState() {
		mShareButton.setState(ShareButton.State.LOADING);
		mWishButton.setState(WishButton.State.LOADING);
		getMyBookStatus(mBook.bookId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				JsonUtils.dump(response);
				dismissSpinner();
				try {
					mMyBook = new MyBook(response);
					mShareButton.setState(mMyBook.review == null ? ShareButton.State.BEFORE : ShareButton.State.AFTER);
					mWishButton.setState(mMyBook.stack == null ? WishButton.State.BEFORE : WishButton.State.AFTER);
				} catch (JSONException e) {
					Toast.makeText(BookActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}

			@Override
			public void onError(ApiRequest.ApiError error) {
				dismissSpinner();
				Toast.makeText(BookActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				Timber.w(error, null);
			}
		});
	}
}
