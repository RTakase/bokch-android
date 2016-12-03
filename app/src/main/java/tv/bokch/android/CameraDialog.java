package tv.bokch.android;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import timber.log.Timber;
import tv.bokch.R;

public class CameraDialog extends BaseDialog {

	private CompoundBarcodeView mBarcodeView;
	private View mCameraButton;
	private View mUrlButton;
	private View mDescView;

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
		
		mCameraButton = view.findViewById(R.id.with_camera_btn);
		mCameraButton.setOnClickListener(mCameraClickListener);
		
		mUrlButton = view.findViewById(R.id.with_url_btn);
		mUrlButton.setOnClickListener(mUrlClickListener);

		mDescView = view.findViewById(R.id.camera_desc);

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
//
//	@Override
//	protected int getWidth(int orientation) {
//		int resId = orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.dialog_width_portrait : R.dimen.dialog_height_landscape;
//		return getResources().getDimensionPixelSize(resId);
//	}
//
//	@Override
//	protected int getHeight(int orientation) {
//		int resId = orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.dialog_width_portrait : R.dimen.dialog_height_landscape;
//		return getResources().getDimensionPixelSize(resId);
//	}

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
	
	private View.OnClickListener mCameraClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mBarcodeView.setVisibility(View.VISIBLE);
			mDescView.setVisibility(View.VISIBLE);

			mUrlButton.setVisibility(View.GONE);
			mCameraButton.setVisibility(View.GONE);
		}
	};
	private View.OnClickListener mUrlClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ClipboardManager manager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData data = manager.getPrimaryClip();
			if (data != null) {
				ClipData.Item item = data.getItemAt(0);
				BaseActivity baseAct = (BaseActivity)getActivity();
				//baseAct.startBookActivity(item.getLabel());
			} else {
				Toast.makeText(getActivity(), getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
			}
		}
	};
}
