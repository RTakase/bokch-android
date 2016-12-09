package tv.bokch.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.MyBook;
import tv.bokch.data.MyUser;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.JsonUtils;
import tv.bokch.util.ViewServer;
import tv.bokch.util.ViewUtils;

public class BaseActivity extends AppCompatActivity {
	
	public static final int REQUEST_LOGIN = 1;

	protected ProgressDialog mProgressDialog;
	protected ProgressDialog mSpinner;
	protected Toolbar mToolbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewServer.get(this).addWindow(this);
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		mToolbar = (Toolbar)findViewById(R.id.toolbar);
		if (Build.VERSION.SDK_INT >= 21) {
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
		}
		if (mToolbar != null) {
			mToolbar.setTitleTextColor(0xffffffff);
			setSupportActionBar(mToolbar);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewServer.get(this).removeWindow(this);
		mProgressDialog = null;
	}

	protected MenuItem addMenuItem(Menu menu, int itemId, int titleRes, int iconRes, int actionEnum) {
		MenuItem item = menu.add(Menu.NONE, itemId, Menu.NONE, titleRes);
		if (iconRes != 0) {
			item.setIcon(iconRes);
		}
		if (actionEnum != 0) {
			item.setShowAsAction(actionEnum);
		}
		return item;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id) {
		case R.id.action_settings:
			logout();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void logout() {
		SharedPreferences pref = getSharedPreferences("bokch", MODE_PRIVATE);
		pref.edit().putString("user_id", "").apply();
		startLoginActivity(getBaseContext());
	}

	protected void setActionBarTitle(int resId) {
		setActionBarTitle(getString(resId));
	}
	protected void setActionBarTitle(String title) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	protected void setActionBarImage(int resId) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setLogo(resId);
			actionBar.setDisplayUseLogoEnabled(true);
		}
	}

	protected void startUserListActivity() {
		Intent intent = new Intent(BaseActivity.this, UserListActivity.class);
		startActivity(intent);
	}

	protected void startRankingActivity() {
		Intent intent = new Intent(BaseActivity.this, RankingActivity.class);
		startActivity(intent);
	}

	public void startReviewActivity(History history) {
		ReviewDialog dialog = ReviewDialog.newInstance(history);
		dialog.show(getFragmentManager(), "ReviewDialog");
	}

	protected void startLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivityForResult(intent, REQUEST_LOGIN);
	}

	/**
	 * 本画面への遷移（ユーザステータスを取得してから）
	 */

	public void startBookActivity(Book book) {
		//startBookActivity(book.bookId, false);
		Intent intent = new Intent(BaseActivity.this, BookActivity.class);
		intent.putExtra("data", book);
		startActivity(intent);
	}
	protected void startBookActivity(String bookId, final boolean withReviewEdit) {
		getMyBookStatus(bookId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				JsonUtils.dump(response);
				dismissSpinner();
				try {
					MyBook myBook = new MyBook(response);
					startBookActivity(myBook, withReviewEdit);
				} catch (JSONException e) {
					Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
				dismissSpinner();
				Toast.makeText(BaseActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				Timber.w(error, null);
			}
		});
	}

	/**
	 * 本画面への遷移（ユーザステータスはもうある）
	 */
	protected void startBookActivity(MyBook myBook) {
		startBookActivity(myBook, false);
	}
	public void startBookActivity(MyBook myBook, boolean withReviewEdit) {
		Intent intent = new Intent(BaseActivity.this, BookActivity.class);
		intent.putExtra("data", myBook);
		intent.putExtra("review", myBook.review);
		intent.putExtra("history", myBook.history);
		intent.putExtra("stack", myBook.stack);
		intent.putExtra("with_review_edit", withReviewEdit);
		startActivity(intent);
	}

	public void startBookActivityWithUrl(String amazon) {
		showSpinner();
		getMyBookStatusFromUrl(amazon, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					dismissSpinner();
					JSONObject obj = response.optJSONObject("book");
					Book book = new Book(obj);
					startBookActivity(book.bookId, true);
				} catch (JSONException e) {
					Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
				dismissSpinner();
				Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(error, null);
			}
		});
	}
	/**
	 * ユーザ画面への遷移（ステータスを取得してから）
	 */
	public void startUserActivity(User user) {
		Intent intent = new Intent(BaseActivity.this, UserActivity.class);
		intent.putExtra("data", user);
		startActivity(intent);
	}
	protected void startUserActivity(User user, final String bookId) {
		showSpinner();
		getMyUserStatus(user.userId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				dismissSpinner();
				try {
					MyUser myUser = new MyUser(response);
					startUserActivity(myUser, bookId);
				} catch (JSONException e) {
					Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
				dismissSpinner();
				Timber.w(error, null);
				Toast.makeText(BaseActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * ユーザ画面への遷移（ステータス情報はもうある）
	 */
	public void startUserActivity(MyUser myUser, String bookId) {
		Intent intent = new Intent(BaseActivity.this, UserActivity.class);
		intent.putExtra("data", myUser);
		intent.putExtra("follow_id", myUser.followId);
		intent.putExtra("with_my_review", bookId);
		startActivity(intent);
	}

	public void getMyBookStatus(String bookId, ApiRequest.ApiListener<JSONObject> listener) {
		App app = (App)getApplicationContext();
		ApiRequest request = new ApiRequest();
		request.book(bookId, app.getMyUser().userId, listener);
	}

	public void getMyBookStatusFromUrl(String amazon, ApiRequest.ApiListener<JSONObject> listener) {
		App app = (App)getApplicationContext();
		ApiRequest request = new ApiRequest();
		try {
			request.book_from_url(amazon, app.getMyUser().userId, listener);
		} catch (JSONException e) {
			Timber.w(e, null);
		}
	}

	public void getMyUserStatus(String userId, ApiRequest.ApiListener<JSONObject> listener) {
		App app = (App)getApplicationContext();
		ApiRequest request = new ApiRequest();
		request.user(userId, app.getMyUser().userId, listener);
	}

	protected void showSpinner() {
		if (mSpinner == null) {
			mSpinner = ViewUtils.showSpinner(this, getString(R.string.loading));
		}
	}
	protected void dismissSpinner() {
		ViewUtils.dismissSpinner(mSpinner);
	}
}
