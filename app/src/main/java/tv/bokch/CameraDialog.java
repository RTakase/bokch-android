package tv.bokch;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import timber.log.Timber;

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
