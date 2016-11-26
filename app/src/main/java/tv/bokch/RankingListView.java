package tv.bokch;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by takase.ryohei on 2016/11/25.
 */

public class RankingListView extends RecyclerView {
	private ArrayList<Integer> mDataSet;
	private LayoutInflater mInflater;
	private LinearLayoutManager mManager;
	private ItemDecoration mDevider;

	public RankingListView(Context context) {
		super(context);
		initialize(context);
	}

	public RankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public RankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	public void initialize(Context context) {
		mDataSet = new ArrayList<>();
		mInflater = LayoutInflater.from(context);
		setAdapter(mAdapter);

		mManager = new LinearLayoutManager(context);
		mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		setLayoutManager(mManager);
	}

	public void addData(Integer i) {
		mDataSet.add(i);
		mAdapter.notifyDataSetChanged();
	}

	private RecyclerView.Adapter<Row> mAdapter = new RecyclerView.Adapter<Row>() {
		@Override
		public Row onCreateViewHolder(ViewGroup parent, int viewType) {
			Row row = new Row(mInflater.inflate(R.layout.view_ranking_row, parent, false));
			return row;
		}

		@Override
		public void onBindViewHolder(Row holder, int position) {
			holder.bindView(mDataSet.get(position));
		}

		@Override
		public int getItemCount() {
			return mDataSet.size();
		}
	};

	public static class Row extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private ImageView mJacket;

		public Row(View view) {
			super(view);
			mTitle = (TextView)view.findViewById(R.id.title);
			mJacket = (ImageView)view.findViewById(R.id.jacket);
		}

		public void bindView(Integer data) {
			mTitle.setText("長い長いタイトルだよ！タイトルだよ！これくらいは長いかもね");
			mJacket.setImageResource(R.drawable.jacket);
		}
	}
}
