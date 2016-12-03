package tv.bokch.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.StatableListView;

public abstract class BaseFragment<Data extends Parcelable> extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		StatableListView<Data> content = createStatableListView(getContext());
		BaseListView<Data> listview = createListView(getContext());
		content.addListView(listview);
		return content;
	}

	protected abstract BaseListView<Data> createListView(Context context);

	protected StatableListView<Data> createStatableListView(Context context) {
		return new StatableListView<>(context);
	}

	protected ArrayList<Data> filterData(ArrayList<Data> data) {
		return data;
	}
	
	public boolean onData(ArrayList<Data> data) {
		StatableListView content = (StatableListView)getView();
		data = filterData(data);
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