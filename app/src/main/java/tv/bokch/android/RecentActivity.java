package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.History;
import tv.bokch.widget.FullRecentListView;

public class RecentActivity extends BaseActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		setContentView(R.layout.activity_recent);

		super.onCreate(savedInstanceState);

		setActionBarTitle(R.string.acitivity_title_recent);

		Intent intent = getIntent();
		ArrayList<History> data = intent.getParcelableArrayListExtra("data");
		if (data == null) {
			finish();
			return;
		}

		FullRecentListView listview = (FullRecentListView)findViewById(R.id.listview);
		listview.setData(data);
	}
}
