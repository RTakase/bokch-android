package tv.bokch.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtils {
	static public Bitmap roundBitmap(Bitmap source, float radius) {
		return roundBitmap(source, radius, false);
	}

	static public Bitmap roundBitmap(Bitmap source, float radius, boolean circle) {
		// create a target bitmap
		final int width = source.getWidth();
		final int height = source.getHeight();
		final Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// init canvas and paint
		final Canvas canvas = new Canvas(target);
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		if (circle) {
			BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawCircle(radius, radius, radius, paint);
		} else {
			// calc rectangle to draw
			final Rect rect = new Rect(0, 0, width, height);
			final RectF rectF = new RectF(rect);

			canvas.drawARGB(0, 0, 0, 0);

			// draw round
			paint.setColor(Color.WHITE);
			canvas.drawRoundRect(rectF, radius, radius, paint);

			// draw source to target
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(source, rect, rectF, paint);
			paint.setXfermode(null);
		}

		return target;
	}

	static public Bitmap circleBitmap(Bitmap source) {
		final int width = source.getWidth();
		final int height = source.getHeight();
		final int size = Math.min(width, height);
		if (width != height) {
			final int x = Math.max((width - height) / 2, 0);
			final int y = Math.max((height - width) / 2, 0);
			source = Bitmap.createBitmap(source, x, y, size, size);
		}
		final float radius = (size / 2.0f);
		return roundBitmap(source, radius, true);
	}
}
