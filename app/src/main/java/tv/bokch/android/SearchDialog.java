package tv.bokch.android;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import timber.log.Timber;
import tv.bokch.R;

import static android.app.Activity.RESULT_OK;

public class SearchDialog extends BaseDialog {

	public static final int REQUEST_PERMISSION = 1;
	private CompoundBarcodeView mBarcodeView;
	private View mCameraButton;
	private View mUrlButton;
	private View mAlternativeView;
	private View mByCameraView;
	private View mByUrlView;
	private View mAmazonButton;
	private View mClipBoardButton;


	public static SearchDialog newInstance() {
		SearchDialog dialog = new SearchDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//引数受け取り
		View view = mParentActivity.getLayoutInflater().inflate(R.layout.dialog_search, null);

		mCameraButton = view.findViewById(R.id.with_camera_btn);
		mCameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisibilities(true);
			}
		});

		mUrlButton = view.findViewById(R.id.with_url_btn);
		mUrlButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisibilities(false);
			}
		});

		mAlternativeView = view.findViewById(R.id.alternative);
		mByCameraView = view.findViewById(R.id.by_camera);
		mByUrlView = view.findViewById(R.id.by_url);

		mBarcodeView = (CompoundBarcodeView)view.findViewById(R.id.camera);
		mBarcodeView.decodeContinuous(mBarcodeCallback);
		mBarcodeView.setStatusText(getString(R.string.barcode_status));

		mAmazonButton = view.findViewById(R.id.amazon_top_btn);
		mAmazonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.co.jp/%E6%9C%AC-%E9%80%9A%E8%B2%A9/b?ie=UTF8&node=465392"));
				startActivity(intent);
			}
		});

		mClipBoardButton = view.findViewById(R.id.read_clipboard_btn);
		mClipBoardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager manager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData data = manager.getPrimaryClip();
				if (data != null) {
					ClipData.Item item = data.getItemAt(0);
					Intent intent = new Intent();
					intent.putExtra("amazon", item.getText().toString());
					sendActivityResultCallback(intent);
					dismiss();
				}
			}
		});

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setContentView(view);
		return dialog;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBarcodeView != null) {
			mBarcodeView.resume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mBarcodeView != null) {
			mBarcodeView.pause();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_PERMISSION:
			if (resultCode == RESULT_OK) {
				if (mBarcodeView != null) {
					mBarcodeView.resume();
				}
			}
		}
	}

	private boolean checkCameraPermission() {
		Timber.d("tks, check permission.");
		if (Build.VERSION.SDK_INT < 23) {
			return true;
		}
		if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
		return false;
	}

	private void setVisibilities(boolean myCamera) {
		if (myCamera) {
			checkCameraPermission();
			mByUrlView.setVisibility(View.GONE);
			mByCameraView.setVisibility(View.VISIBLE);
		} else {
			mByUrlView.setVisibility(View.VISIBLE);
			mByCameraView.setVisibility(View.GONE);
		}
		mAlternativeView.setVisibility(View.GONE);
	}

	private BarcodeCallback mBarcodeCallback = new BarcodeCallback() {
		@Override
		public void barcodeResult(BarcodeResult result) {
			String barcode = result.getText();
			if (!TextUtils.isEmpty(barcode)) {
				Intent intent = new Intent();
				intent.putExtra("barcode", barcode);
				sendActivityResultCallback(intent);
				dismiss();
			}
		}
		@Override
		public void possibleResultPoints(List<ResultPoint> resultPoints) {
		}
	};
}
