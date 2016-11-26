package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.History;

public class RecentListView extends HorizontalListView {
	private ArrayList<History> mDataSet;
	private LayoutInflater mInflater;
	
	public RecentListView(Context context) {
		super(context);
		initialize(context);
	}
	
	public RecentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public RecentListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		mDataSet = new ArrayList<>();
		mInflater = LayoutInflater.from(context);
		setAdapter(mAdapter);
	}
	
	public void setData(ArrayList<History> histories) {
		mDataSet.clear();
		mDataSet.addAll(histories);
		mAdapter.notifyDataSetChanged();
	}
	
	private RecyclerView.Adapter<Row> mAdapter = new RecyclerView.Adapter<Row>() {
		@Override
		public Row onCreateViewHolder(ViewGroup parent, int viewType) {
			Row row = new Row(mInflater.inflate(R.layout.row_home_recent, parent, false));
			return row;
		}
		
		@Override
		public void onBindViewHolder(Row holder, int position) {
			if (mDataSet.size() > position) {
				holder.bindView(mDataSet.get(position));
			}
		}
		
		@Override
		public int getItemCount() {
			return mDataSet.size();
		}
	};
	
	public static class Row extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private CircleNetworkImageView mUserIcon;
		private NetworkImageView mJacket;
		
		public Row(View view) {
			super(view);
			mTitle = (TextView)view.findViewById(R.id.title);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
			mUserIcon = (CircleNetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
		}
		
		public void bindView(History history) {
			mTitle.setText(history.book.title);
			mJacket.setImageUrl(history.book.largeImageUrl);
			mUserIcon.setImageUrl(history.user.iconUrl);
		}
	}
}
