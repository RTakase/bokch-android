package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.widget.FullUserRankingListView;
import tv.bokch.widget.RecyclerView;
import tv.bokch.widget.StatableListView;

public class UserRankingFragment extends Fragment {

	public static UserRankingFragment newInstance(String title) {
		return newInstance(title, null);
	}
	public static UserRankingFragment newInstance(String title, ArrayList<User> data) {
		UserRankingFragment fragment = new UserRankingFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("data", data);
		args.putString("title", title);
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
		ArrayList<User> data = arguments.getParcelableArrayList("data");

		StatableListView<User> view = new StatableListView<>(getContext());
		RecyclerView<User> listview = new FullUserRankingListView(getContext());
		view.addListView(listview);
		onData(view, data);
		return view;
	}

	public void onData(ArrayList<User> data) {
		onData((StatableListView)getView(), data);
	}
	public void onData(StatableListView view, ArrayList<User> data) {
		if (data == null) {
			view.setState(StatableListView.State.Loading);
		} else if (data.size() == 0) {
			view.setState(StatableListView.State.Failed);
		} else {
			view.setData(data);
			view.setState(StatableListView.State.OK);
		}
	}
}