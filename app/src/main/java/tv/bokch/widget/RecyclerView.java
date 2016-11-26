package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.bokch.util.Display;

public abstract class RecyclerView<Data> extends android.support.v7.widget.RecyclerView {

	public static final int VIEW_TYPE_CONTENT = 0;
	public static final int VIEW_TYPE_HEADER = 1;
	public static final int VIEW_TYPE_FOOTER = 2;

	enum Orientation {
		Vertical,
		Horizontal
	}

	protected LayoutInflater mInflater;
	protected Display mDisplay;
	protected ArrayList<Data> mDataSet;
	private LinearLayoutManager mManager;
	
	public RecyclerView(Context context) {
		super(context);
		initialize(context);
	}
	public RecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		mDataSet = new ArrayList<>();
		mInflater = LayoutInflater.from(context);
		mDisplay = new Display(context);
		setAdapter(mAdapter);

		mManager = new LinearLayoutManager(context);
		setLayoutManager(mManager);
	}

	public void setOrientation(Orientation orientation) {
		switch (orientation) {
		case Horizontal:
			mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			break;
		case Vertical:
			mManager.setOrientation(LinearLayoutManager.VERTICAL);
			break;
		}
	}

	public void setData(ArrayList<Data> data) {
		mDataSet.clear();
		if (data.size() == 0) {
		} else {
			mDataSet.addAll(data);
			mDataSet.add(0, null); //ヘッダーを認識するためのダミーデータ
			mDataSet.add(null); //フッターを認識するためのダミーデータ
			mAdapter.notifyDataSetChanged();
		}
	}

	public ArrayList<Data> getData() {
		if (mDataSet.size() == 0) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(mDataSet.subList(1, mDataSet.size() - 1));
		}
	}

	public int getDataSetSize() {
		return mDataSet.size() - 2; //ダミーデータの分だけ引く
	}

	protected abstract int getLayoutResId();
	protected abstract int getFooterResId();
	protected abstract int getHeaderResId();
	protected abstract Row createRow(View view);
	protected abstract Row createHeaderRow(View view);
	protected abstract Row createFooterRow(View view);

	protected RecyclerView.Adapter<Row> mAdapter = new RecyclerView.Adapter<Row>() {

		@Override
		public int getItemViewType(int position) {
			if (position == 0) {
				return VIEW_TYPE_HEADER;
			} else if (position == mDataSet.size() - 1) {
				return VIEW_TYPE_FOOTER;
			} else {
				return VIEW_TYPE_CONTENT;
			}
		}

		@Override
		public Row onCreateViewHolder(ViewGroup parent, int viewType) {
			switch (viewType) {
			case VIEW_TYPE_FOOTER:
				return createFooterRow(mInflater.inflate(getFooterResId(), parent, false));
			case VIEW_TYPE_HEADER:
				return createHeaderRow(mInflater.inflate(getHeaderResId(), parent, false));
			default:
				return createRow(mInflater.inflate(getLayoutResId(), parent, false));
			}
		}
		@Override
		public void onBindViewHolder(Row holder, int position) {
			if (position == 0) {
				//no implement
			} else if (position == mDataSet.size() - 1) {
				//do nothing
			} else {
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

	protected class DummyRow extends Row {
		public DummyRow(View view) {
			super(view);
		}
	}
}
