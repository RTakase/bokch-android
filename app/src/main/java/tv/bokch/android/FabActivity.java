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
import tv.bokch.data.Review;
import tv.bokch.util.ApiRequest;
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
					ApiRequest request = new ApiRequest();
					App app = (App)getApplication();
					request.book(isbn, app.getMyUser().userId, mBookApiListener);
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
	
	private ApiRequest.ApiListener<JSONObject> mBookApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONObject obj = response.getJSONObject("book");
				if (obj == null) {
					Toast.makeText(FabActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
					return;
				}

				Intent intent = new Intent(FabActivity.this, BookActivity.class);
				Book book = new Book(obj);
				intent.putExtra("data", book);

				JSONObject status = obj.optJSONObject("my_status");
				Timber.d("tks, status = %s", status.toString());
				if (status != null) {
					History history = new History(status.optJSONObject("history"));
					Review review = new Review(status.optJSONObject("review"));
					intent.putExtra("history", history);
					intent.putExtra("review", review);
				}
				startActivity(intent);
			} catch (JSONException e) {
				Toast.makeText(FabActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Toast.makeText(FabActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	};
}
