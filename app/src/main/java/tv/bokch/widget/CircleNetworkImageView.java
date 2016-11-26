package tv.bokch.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import tv.bokch.util.BitmapUtils;

public class CircleNetworkImageView extends NetworkImageView {
	private float mBorderSize;
	private Paint mBorderPaint;

	public CircleNetworkImageView(Context context) {
		super(context);
	}

	public CircleNetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageDrawable = getBitmapDrawable(defaultImage);
	}

	@Override
	public void setErrorImageResId(int defaultImage) {
		mDefaultImageDrawable = getBitmapDrawable(defaultImage);
	}

	private BitmapDrawable getBitmapDrawable(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
		Bitmap circle = BitmapUtils.circleBitmap(bitmap);
		if (!circle.equals(bitmap)) {
			bitmap.recycle();
		}
		return new BitmapDrawable(getResources(), circle);
	}

	public void setBorder(float border, int color) {
		mBorderSize = border;
		mBorderPaint = new Paint();
		mBorderPaint.setStrokeWidth(border);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(color);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		if (mBorderSize > 0 && mBorderPaint != null) {
			int width = c.getWidth();
			int height = c.getHeight();
			float cx = width / 2.0f;
			float cy = height / 2.0f;
			float size = Math.min(cx, cy) - (mBorderSize / 2);
			c.drawCircle(cx, cy, size, mBorderPaint);
		}
	}

	@Override
	protected void onRequestCreator(RequestCreator creator) {
		creator.transform(new Transformation() {
			@Override
			public Bitmap transform(Bitmap source) {
				Bitmap trans = BitmapUtils.circleBitmap(source);
				if (!source.equals(trans)) {
					source.recycle();
				}
				return trans;
			}

			@Override
			public String key() {
				return "circle";
			}
		});
	}
}
