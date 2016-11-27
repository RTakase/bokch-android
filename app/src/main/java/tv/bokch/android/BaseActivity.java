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


import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.util.ViewServer;

public class BaseActivity extends AppCompatActivity {
	
	public static final int REQUEST_LOGIN = 1;

	protected User mMyUser;

	protected ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewServer.get(this).addWindow(this);

		Timber.plant(new Timber.DebugTree());
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

	protected void showProgress(String message) {
		Timber.d("tks, show progress.");
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(
				this,
				"",
				message
			);
		}
	}

	protected void hideProgress() {
		Timber.d("tks, hide progress.");
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	protected void startLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, REQUEST_LOGIN);
	}
}
