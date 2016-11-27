package tv.bokch.android;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import tv.bokch.R;

public class CameraDialog extends BaseDialog {

	private CompoundBarcodeView mBarcodeView;

	public static CameraDialog newInstance() {
		CameraDialog dialog = new CameraDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//引数受け取り
		View view = mParentActivity.getLayoutInflater().inflate(R.layout.dialog_camera, null);

		mBarcodeView = (CompoundBarcodeView)view.findViewById(R.id.camera);
		mBarcodeView.decodeContinuous(mBarcodeCallback);

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
	protected int getWidth(int orientation) {
		return SIZE_DEFAULT;
	}

	@Override
	protected int getHeight(int orientation) {
		return getResources().getDimensionPixelSize(R.dimen.dialog_width_portrait);
	}

	private BarcodeCallback mBarcodeCallback = new BarcodeCallback() {
		@Override
		public void barcodeResult(BarcodeResult result) {
			String barcode = result.getText();
			if (!TextUtils.isEmpty(barcode)) {
				Intent intent = new Intent();
				intent.putExtra("barcode", barcode);
				sendActivityResultCallback(intent);
				mBarcodeView.pause();
				dismiss();
			}
		}

		@Override
		public void possibleResultPoints(List<ResultPoint> resultPoints) {
		}
	};
}
