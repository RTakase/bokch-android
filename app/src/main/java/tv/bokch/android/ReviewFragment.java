package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;

import tv.bokch.data.Review;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.ReviewListView;

public class ReviewFragment extends BaseFragment<Review> {
	@Override
	protected BaseListView<Review> createListView(Context context) {
		return new ReviewListView(context);
	}
	public static ReviewFragment newInstance() {
		ReviewFragment fragment = new ReviewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}	
}