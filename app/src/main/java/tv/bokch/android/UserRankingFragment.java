package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.bokch.data.User;
import tv.bokch.widget.FullUserRankingListView;
import tv.bokch.widget.RecyclerView;
import tv.bokch.widget.StatableListView;

public class UserRankingFragment extends Fragment {

	public boolean loaded;

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

		StatableListView<User> content = new StatableListView<>(getContext());
		RecyclerView<User> listview = new FullUserRankingListView(getContext());
		content.addListView(listview);
		loaded = content.onData(data);
		return content;
	}

	public boolean onData(ArrayList<User> data) {
		StatableListView content = (StatableListView)getView();
		boolean res = false;
		if (content != null) {
			res = content.onData(data);
		}
		return res;
	}
}