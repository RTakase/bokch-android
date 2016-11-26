package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.util.ViewServer;

public class BaseActivity extends AppCompatActivity {
	public static final int REQUEST_CODE_CAMERA = 1;
	private CameraDialog mCameraDialog;
	protected FloatingActionButton fab;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewServer.get(this).addWindow(this);

		Timber.plant(new Timber.DebugTree());

		fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(mFabClickListener);
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
		if (mCameraDialog != null) {
			mCameraDialog.dismiss();
		}
		mCameraDialog = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String isbn = data.getStringExtra("barcode");
				String str = String.format("ISBNコードをよみとりました！（%s）", isbn);
				Toast.makeText(this,  str, Toast.LENGTH_SHORT).show();
			}
		}
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
			return true;
		}

		return super.onOptionsItemSelected(item);
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

	private View.OnClickListener mFabClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mCameraDialog = CameraDialog.newInstance();
			mCameraDialog.setTargetFragment(null, REQUEST_CODE_CAMERA);
			mCameraDialog.show(getFragmentManager(), "CameraDialog");
		}
	};

}
