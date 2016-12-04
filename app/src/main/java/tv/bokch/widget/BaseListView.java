package tv.bokch.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.android.BookActivity;
import tv.bokch.android.ReviewDialog;
import tv.bokch.android.UserActivity;
import tv.bokch.android.UserListActivity;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.MyBook;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.Display;
import tv.bokch.util.ViewUtils;

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
	protected ProgressDialog mSpinner;
	protected LayoutManager mManager;
	
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

		mManager = createLayoutManager(context);
		setLayoutManager(mManager);
	}

	public void setOrientation(Orientation orientation) {
		if (!(mManager instanceof LinearLayoutManager)) {
			return;
		}
		LinearLayoutManager manager = (LinearLayoutManager)mManager;
		switch (orientation) {
		case Horizontal:
			manager.setOrientation(LinearLayoutManager.HORIZONTAL);
			break;
		case Vertical:
			manager.setOrientation(LinearLayoutManager.VERTICAL);
			break;
		}
	}

	public void setData(ArrayList<Data> data) {
		setData(data, true, true);
	}

	public void setData(ArrayList<Data> data, boolean withHeader, boolean withFooter) {
		mDataSet.clear();
		if (data.size() == 0) {
		} else {
			mDataSet.addAll(data);
			if (withHeader) {
				//ヘッダーを認識するためのダミーデータ
				mDataSet.add(0, null);
			}
			if (withFooter) {
				//フッターを認識するためのダミーデータ
				mDataSet.add(null);
			}
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
	protected void onCellClick(Data data) {
	}
	
	protected LayoutManager createLayoutManager(Context context) {
		return new LinearLayoutManager(context);
	}

	protected BaseListView.Adapter<Cell> mAdapter = new BaseListView.Adapter<Cell>() {

		@Override
		public int getItemViewType(int position) {
			if (position == 0 && mDataSet.get(position) == null) {
				return VIEW_TYPE_HEADER;
			} else if (position == mDataSet.size() - 1 && mDataSet.get(position) == null) {
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
			switch (getItemViewType(position)) {
			case VIEW_TYPE_FOOTER:
				//do nothing
			case VIEW_TYPE_HEADER:
				// do nothing
			default:
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
					onCellClick(data);
				}
			});
		}

		protected void visible() {
			root.setVisibility(View.VISIBLE);
		}

		protected void gone() {
			root.setVisibility(View.GONE);
		}
	}

	private class DummyCell extends Cell {
		public DummyCell(View view) {
			super(view);
		}
	}

	protected void showSpinner() {
		mSpinner = ViewUtils.showSpinner(getContext(), getContext().getString(R.string.loading));
	}
	protected void dismissSpinner() {
		ViewUtils.dismissSpinner(mSpinner);
	}
}
