package tv.bokch.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Display {
	public final int widthPixels;
	public final int heightPixels;
	private final DisplayMetrics mMetrics;

	public Display(Context context) {
		mMetrics = context.getResources().getDisplayMetrics();
		widthPixels = mMetrics.widthPixels;
		heightPixels = mMetrics.heightPixels;
	}

	static public int dpToPx(Context context, int dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return dpToPx(metrics, dp);
	}

	static public int dpToPx(DisplayMetrics metrics, int dp) {
		return (int)(dp * metrics.density + 0.5f);
	}

	static public int pxToDp(DisplayMetrics metrics, int px) {
		return (int)((float)px / metrics.density);
	}

	public int toPixels(int dp) {
		return dpToPx(mMetrics, dp);
	}

	public float toPixels(float sp) {
		return sp * mMetrics.scaledDensity;
	}

	public int toDp(int px) {
		return pxToDp(mMetrics, px);
	}

	public int getWidthDp() {
		return (int)((float)mMetrics.widthPixels / mMetrics.density);
	}
}
