package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.util.ViewServer;

public class BaseActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewServer.get(this).addWindow(this);

		Timber.plant(new Timber.DebugTree());

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
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
}
