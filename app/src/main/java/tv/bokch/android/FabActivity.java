package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.MyBook;
import tv.bokch.data.Review;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.JSONUtils;
import tv.bokch.util.ViewUtils;
import tv.bokch.widget.StatableListView;

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

	protected void startBookActivity(String bookId) {
		App app = (App)getApplicationContext();
		ApiRequest request = new ApiRequest();
		request.book(bookId, app.getMyUser().userId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				ViewUtils.dismissSpinner(mSpinner);
				try {
					MyBook book = new MyBook(response);
					Intent intent = new Intent(FabActivity.this, BookActivity.class);
					intent.putExtra("data", book);
					intent.putExtra("review", book.review);
					intent.putExtra("history", book.history);
					startActivity(intent);
				} catch (JSONException e) {
					Timber.w(e, null);
				}
			}
			@Override
			public void onError(ApiRequest.ApiError error) {
			}
		});
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
