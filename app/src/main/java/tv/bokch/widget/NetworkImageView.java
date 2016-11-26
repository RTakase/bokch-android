/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tv.bokch.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class NetworkImageView extends ImageView {
	/** The URL of the network image to load */
	private String mUrl;

	/**
	 * Drawable of the image to be used as a placeholder until the network image is loaded.
	 */
	protected Drawable mDefaultImageDrawable;

	/**
	 * Drawable of the image to be used if the network response fails.
	 */
	protected Drawable mErrorImageDrawable;

	/** Current image. (either in-flight or finished) */
	private String mCallUrl;

	public NetworkImageView(Context context) {
		this(context, null);
	}

	public NetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that calling this will
	 * immediately either set the cached image (if available) or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 *
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)} and
	 * {@link NetworkImageView#setErrorImageResId(int)} should be called prior to calling
	 * this function.
	 *
	 * @param url The URL that should be loaded into this ImageView.
	 */
	public void setImageUrl(String url) {
		mUrl = url;

		// The URL has potentially changed. See if we need to load it.
		loadImageIfNecessary();
	}

	public String getImageUrl() {
		return mUrl;
	}

	/**
	 * Sets the default image resource ID to be used for this view until the attempt to load it
	 * completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		setDefaultImage(getResources().getDrawable(defaultImage));
	}

	public void setDefaultImage(Drawable drawable) {
		mDefaultImageDrawable = drawable;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event that the image
	 * requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageDrawable = getResources().getDrawable(errorImage);
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 */
	void loadImageIfNecessary() {
		final int width = getWidth();
		final int height = getHeight();
		boolean wrapWidth = false, wrapHeight = false;
		if (getLayoutParams() != null) {
			wrapWidth = getLayoutParams().width == LayoutParams.WRAP_CONTENT;
			wrapHeight = getLayoutParams().height == LayoutParams.WRAP_CONTENT;
		}

		// if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
		// view, hold off on loading the image.
		boolean isFullyWrapContent = wrapWidth && wrapHeight;
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		// if the URL to be loaded in this view is empty, cancel any old requests and clear the
		// currently loaded image.
		if (TextUtils.isEmpty(mUrl)) {
			cancelCall();
			setDefaultImageOrNull();
			return;
		}

		// if there was an old request in this view, check if it needs to be canceled.
		if (mCallUrl != null) {
			if (mCallUrl.equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's fetching a different URL.
				cancelCall();
				setDefaultImageOrNull();
			}
		}
		mCallUrl = mUrl;

		// Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
		final ScaleType scaleType = getScaleType();
		final int maxWidth = wrapWidth ? 0 : width;
		final int maxHeight = wrapHeight ? 0 : height;

		RequestCreator creator = Picasso.with(getContext()).load(mUrl);
		creator.tag(getContext());
		if (mDefaultImageDrawable != null) {
			creator.placeholder(mDefaultImageDrawable);
		}
		if (mErrorImageDrawable != null) {
			creator.error(mErrorImageDrawable);
		}
		if (maxWidth > 0 || maxHeight > 0) {
			creator.resize(maxWidth, maxHeight);
			if (scaleType == ScaleType.CENTER_CROP) {
				creator.centerCrop();
			} else if (scaleType == ScaleType.CENTER_INSIDE) {
				creator.centerInside();
			}
		} else {
			creator.fit();
		}
		onRequestCreator(creator);
		creator.into(this);
	}

	protected void onRequestCreator(RequestCreator creator) {
	}

	private void cancelCall() {
		if (mCallUrl != null) {
			Picasso.with(getContext()).cancelRequest(this);
			mCallUrl = null;
		}
	}

	private void setDefaultImageOrNull() {
		if (mDefaultImageDrawable != null) {
			setImageDrawable(mDefaultImageDrawable);
		} else {
			setImageBitmap(null);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary();
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mCallUrl != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			Picasso.with(getContext()).cancelRequest(this);
			setImageBitmap(null);
			// also clear out the container so we can reload the image if necessary.
			mCallUrl = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}
}
