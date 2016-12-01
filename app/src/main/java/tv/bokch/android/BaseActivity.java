package tv.bokch.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.MyBook;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.Display;
import tv.bokch.util.ViewServer;
import tv.bokch.util.ViewUtils;

public class BaseActivity extends AppCompatActivity {
	
	public static final int REQUEST_LOGIN = 1;

	protected User mMyUser;

	protected Display mDisplay;

	protected ProgressDialog mProgressDialog;

	protected ProgressDialog mSpinner;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewServer.get(this).addWindow(this);
		mDisplay = new Display(this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
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
		mMyUser = null;
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

	protected void startLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, REQUEST_LOGIN);
	}

	protected void startBookActivity(Book book) {
		startBookActivity(book.bookId);
	}

	protected void startBookActivity(String bookId) {
		App app = (App)getApplicationContext();
		ApiRequest request = new ApiRequest();
		showSpinner();
		request.book(bookId, app.getMyUser().userId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				dismissSpinner();
				try {
					MyBook book = new MyBook(response);
					book.title = null;
					Intent intent = new Intent(BaseActivity.this, BookActivity.class);
					intent.putExtra("data", book);
					intent.putExtra("review", book.review);
					intent.putExtra("history", book.history);
					startActivity(intent);
				} catch (JSONException e) {
					Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
				dismissSpinner();
				Toast.makeText(BaseActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void startUserActivity(User user) {
		Intent intent = new Intent(BaseActivity.this, UserActivity.class);
		intent.putExtra("data", user);
		startActivity(intent);
	}

	protected void showSpinner() {
		mSpinner = ViewUtils.showSpinner(this, getString(R.string.loading));
	}
	protected void dismissSpinner() {
		ViewUtils.dismissSpinner(mSpinner);
	}
}
