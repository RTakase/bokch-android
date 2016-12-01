package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import tv.bokch.R;

public class FabActivity extends BaseActivity {

	public static final int REQUEST_CODE_CAMERA = 999;

	private CameraDialog mCameraDialog;
	private FloatingActionButton mFab;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFab = (FloatingActionButton)findViewById(R.id.fab);
		mFab.setOnClickListener(mFabClickListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
				if (TextUtils.isEmpty(isbn)) {
					Toast.makeText(FabActivity.this, getString(R.string.failed_load_isbn), Toast.LENGTH_SHORT).show();
				} else {
					startBookActivity(isbn);
				}
			}
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
