package tv.bokch.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.util.ViewUtils;

public class BaseDialog extends DialogFragment {
	protected Activity mParentActivity;
	protected ProgressDialog mSpinner;
	
	@Override
	public void onAttach(Activity activity) {
		mParentActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Dialog dialog = getDialog();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

		int orientation = getResources().getConfiguration().orientation;
		params.width = getWidth(orientation);
		params.height = getHeight(orientation);
		dialog.getWindow().setAttributes(params);
	}

	
	protected int getWidth(int orientation) {
		int resId = orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.dialog_width_portrait : R.dimen.dialog_width_landscape;
		return getResources().getDimensionPixelSize(resId);
	}
	protected int getHeight(int orientation) {
		int resId = orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.dialog_height_portrait : R.dimen.dialog_height_landscape;
		return getResources().getDimensionPixelSize(resId);
	}

	protected boolean onBackKeyPressed() {
		return false;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Activity activity = getActivity();
		if (activity == null) {
			dismiss();
			return null;
		}
		Dialog dialog = new Dialog(activity);
		//オリジナルのデザインを適用するために標準のデザインを消す
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //nullじゃだめだった
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		//dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
					return onBackKeyPressed();
				}
				return false;
			}
		});
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	protected void sendActivityResultCallback(@Nullable Intent intent) {
		if (intent == null) {
			intent = new Intent();
		}
		PendingIntent pending = mParentActivity.createPendingResult(getTargetRequestCode(), intent, PendingIntent.FLAG_ONE_SHOT);
		try {
			pending.send(Activity.RESULT_OK);
		} catch (PendingIntent.CanceledException e) {
			Timber.w(e, null);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		//キャンセルされると呼び出し元のactivityResultが呼ばれないため、
		//キャンセル動作はdismissに変更する
		dismiss();
	}

	protected void showSpinner() {
		mSpinner = ViewUtils.showSpinner(getActivity(), getString(R.string.loading));
	}
	protected void dismissSpinner() {
		ViewUtils.dismissSpinner(mSpinner);
	}
}
