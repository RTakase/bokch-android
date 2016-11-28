package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.bokch.data.Review;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.ReviewListView;
import tv.bokch.widget.StatableListView;

public class ReviewFragment extends Fragment {
	public static ReviewFragment newInstance() {
		return newInstance(null);
	}
	public static ReviewFragment newInstance(ArrayList<Review> data) {
		ReviewFragment fragment = new ReviewFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("data", data);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		ArrayList<Review> data = arguments.getParcelableArrayList("data");
		
		StatableListView<Review> content = new StatableListView<>(getContext());
		BaseListView<Review> listview = new ReviewListView(getContext());
		content.addListView(listview);
		content.onData(data);
		return content;
	}
	
	public boolean onData(ArrayList<Review> data) {
		StatableListView content = (StatableListView)getView();
		boolean res = false;
		if (content != null) {
			res = content.onData(data);
		}
		return res;
	}

	public void setState(StatableListView.State state) {
		StatableListView content = (StatableListView)getView();
		if (content != null) {
			content.setState(state);
		}
	}
}