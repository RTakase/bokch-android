package tv.bokch.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import tv.bokch.R;


public class ViewUtils {
	private static ProgressBar getProgressBar(ViewGroup view) {
		if (view.getChildCount() > 0) {
			View v = view.getChildAt(0);
			if (v instanceof ProgressBar) {
				return (ProgressBar)v;
			}
		}
		return null;
	}

	public static void showProgress(ViewGroup view) {
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		showProgress(view, params);
	}

	public static void showProgress(FrameLayout view) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		showProgress(view, params);
	}

	public static void showProgress(LinearLayout view) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		showProgress(view, params);
	}

	public static void showProgress(ViewGroup view, ViewGroup.LayoutParams params) {
		ProgressBar p = getProgressBar(view);
		if (p == null) {
			p = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleLarge);
			view.removeAllViews();
			view.addView(p, params);
		}
	}

	public static void dismissProgress(ViewGroup view) {
		ProgressBar p = getProgressBar(view);
		if (p != null) {
			view.removeView(p);
		}
	}

	public static void showSuccessToast(Context context, int messageResource) {
		showToast(context, messageResource, R.drawable.ic_toast_success_white);
	}

	public static void showErrorToast(Context context, int messageResource) {
		showToast(context, messageResource, R.drawable.ic_toast_error_white);
	}

	public static void showErrorToast(Context context, String message) {
		showToast(context, message, R.drawable.ic_toast_error_white);
	}

	private static void showToast(Context context, int messageResource, int iconResource) {
		if (context == null) {
			return;
		}
		Toast t = Toast.makeText(context, messageResource, Toast.LENGTH_SHORT);
		showToast(t, iconResource);
	}


	private static void showToast(Context context, String message, int iconResource) {
		if (context == null) {
			return;
		}
		Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		showToast(t, iconResource);
	}

	private static void showToast(Toast toast, int iconResource) {
		LinearLayout l = (LinearLayout)toast.getView();
		TextView tv = (TextView)l.findViewById(android.R.id.message);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setCompoundDrawablesWithIntrinsicBounds(iconResource, 0, 0, 0);
		tv.setCompoundDrawablePadding(25);
		toast.show();
	}

	public static ProgressDialog showSpinner(Context context, String message) {
		return showSpinner(context, message, true);
	}

	public static ProgressDialog showSpinner(Context context, String message, boolean cancelable) {
		ProgressDialog spinner = new ProgressDialog(context);
		spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		spinner.setCanceledOnTouchOutside(false);
		spinner.setCancelable(cancelable);
		spinner.setMessage(message);
		spinner.show();
		return spinner;
	}

	public static void dismissSpinner(ProgressDialog spinner) {
		if (spinner != null && spinner.isShowing()) {
			try {
				spinner.dismiss();
			} catch (IllegalArgumentException ex) {
				// Activityが閉じた場合はIllegalArgumentExceptionが発生するが無視する
			}
		}
	}
}
