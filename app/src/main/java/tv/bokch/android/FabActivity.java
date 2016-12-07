package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import tv.bokch.R;

public class FabActivity extends BaseActivity {

	public static final int REQUEST_CODE_CAMERA = 999;

	private SearchDialog mSearchDialog;
	private FloatingActionButton mFab;

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		mFab = (FloatingActionButton)findViewById(R.id.fab);
		if (mFab != null) {
			mFab.setOnClickListener(mFabClickListener);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSearchDialog != null) {
			//mSearchDialog.dismiss();
		}
		mSearchDialog = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_CAMERA:
			if (resultCode == RESULT_OK) {
				String isbn = data.getStringExtra("barcode");
				String amazon = data.getStringExtra("amazon");
				if (TextUtils.isEmpty(isbn) && !TextUtils.isEmpty(amazon)) {
					startBookActivityWithUrl(amazon);
				} else if (!TextUtils.isEmpty(isbn) && TextUtils.isEmpty(amazon)) {
					startBookActivity(isbn, true);
				} else {
					Toast.makeText(FabActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private View.OnClickListener mFabClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mSearchDialog = SearchDialog.newInstance();
			mSearchDialog.setTargetFragment(null, REQUEST_CODE_CAMERA);
			mSearchDialog.show(getFragmentManager(), "SearchDialog");
		}
	};
}
