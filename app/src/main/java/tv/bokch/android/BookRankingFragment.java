package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.bokch.data.Book;
import tv.bokch.widget.FullBookRankingListView;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.StatableListView;

public class BookRankingFragment extends Fragment {
	public static BookRankingFragment newInstance() {
		return newInstance(null);
	}
	public static BookRankingFragment newInstance(ArrayList<Book> data) {
		BookRankingFragment fragment = new BookRankingFragment();
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
		ArrayList<Book> data = arguments.getParcelableArrayList("data");
		
		StatableListView<Book> content = new StatableListView<>(getContext());
		BaseListView<Book> listview = new FullBookRankingListView(getContext());
		content.addListView(listview);
		content.onData(data);
		return content;
	}
	
	public boolean onData(ArrayList<Book> data) {
		StatableListView content = (StatableListView)getView();
		boolean res = false;
		if (content != null) {
			res = content.onData(data);
		}
		return res;
	}
}