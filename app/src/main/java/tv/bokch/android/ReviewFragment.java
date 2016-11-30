package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;

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

	@Override
	protected ArrayList<Review> filterData(ArrayList<Review> data) {
		return super.filterData(data);
//		ArrayList<Review> newOne = new ArrayList<>();
//		for (int i = 0; i < data.size(); i++) {
//			if (!TextUtils.isEmpty(data.get(i).comment)) {
//				newOne.add(data.get(i));
//			}
//		}
//		return newOne;
	}
}