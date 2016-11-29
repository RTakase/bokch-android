package tv.bokch.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.android.BookActivity;
import tv.bokch.data.Book;
import tv.bokch.data.User;
import tv.bokch.util.Display;

public abstract class BaseListView<Data> extends android.support.v7.widget.RecyclerView {

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
	
	public BaseListView(Context context) {
		super(context);
		initialize(context);
	}
	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
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

	protected int getFooterResId() {
		return R.layout.cell_dummy;
	}
	protected int getHeaderResId() {
		return R.layout.cell_dummy;
	}

	protected Cell createHeader(View view) {
		return new DummyCell(view);
	}

	protected Cell createFooter(View view) {
		return new DummyCell(view);
	}

	protected abstract int getLayoutResId();
	protected abstract Cell createCell(View view);

	protected BaseListView.Adapter<Cell> mAdapter = new BaseListView.Adapter<Cell>() {

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
		public Cell onCreateViewHolder(ViewGroup parent, int viewType) {
			switch (viewType) {
			case VIEW_TYPE_FOOTER:
				return createFooter(mInflater.inflate(getFooterResId(), parent, false));
			case VIEW_TYPE_HEADER:
				return createHeader(mInflater.inflate(getHeaderResId(), parent, false));
			default:
				return createCell(mInflater.inflate(getLayoutResId(), parent, false));
			}
		}
		@Override
		public void onBindViewHolder(Cell holder, int position) {
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

	protected class Cell extends BaseListView.ViewHolder {
		protected View root;
		public Cell(View view) {
			super(view);
			this.root = view;
		}
		protected void bindView(final Data data, int position) {
			root.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					if (data instanceof User) {
//						intent.setClass(getContext(), UserActivity.class);
//						intent.putExtra("data", (User)data);
//						getContext().startActivity(intent);
					} else if (data instanceof Book) {
						intent.setClass(getContext(), BookActivity.class);
						intent.putExtra("data", (Book)data);
						getContext().startActivity(intent);
					}
				}
			});
		}
	}

	private class DummyCell extends Cell {
		public DummyCell(View view) {
			super(view);
		}
	}
}
