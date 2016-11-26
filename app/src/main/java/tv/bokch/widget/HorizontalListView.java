package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class HorizontalListView<Data> extends RecyclerView {
	protected LayoutInflater mInflater;
	protected ArrayList<Data> mDataSet;
	private LinearLayoutManager mManager;
	
	public HorizontalListView(Context context) {
		super(context);
		initialize(context);
	}
	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public HorizontalListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	protected void setData(ArrayList<Data> data) {
		mDataSet.clear();
		mDataSet.addAll(data);
		mAdapter.notifyDataSetChanged();
	}

	private void initialize(Context context) {
		mDataSet = new ArrayList<>();
		mInflater = LayoutInflater.from(context);
		setAdapter(mAdapter);

		mManager = new LinearLayoutManager(context);
		mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		setLayoutManager(mManager);
	}

	protected abstract int getLayoutResId();

	protected abstract Row createRow(View view);

	protected RecyclerView.Adapter<Row> mAdapter = new RecyclerView.Adapter<Row>() {
		@Override
		public Row onCreateViewHolder(ViewGroup parent, int viewType) {
			return createRow(mInflater.inflate(getLayoutResId(), parent, false));
		}
		@Override
		public void onBindViewHolder(Row holder, int position) {
			if (mDataSet.size() > position) {
				holder.bindView(mDataSet.get(position), position);
			}
		}
		@Override
		public int getItemCount() {
			return mDataSet.size();
		}
	};

	protected class Row extends RecyclerView.ViewHolder {
		public Row(View view) {
			super(view);
		}
		protected void bindView(Data data, int position) {
		}
	}
}
