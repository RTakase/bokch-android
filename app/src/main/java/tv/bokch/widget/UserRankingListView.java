package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.User;

public class UserRankingListView extends HorizontalListView {
	private ArrayList<User> mDataSet;
	private LayoutInflater mInflater;
	
	public UserRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	
	public UserRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public UserRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		mDataSet = new ArrayList<>();
		mInflater = LayoutInflater.from(context);
		setAdapter(mAdapter);
	}
	
	public void setData(ArrayList<User> users) {
		mDataSet.clear();
		mDataSet.addAll(users);
		mAdapter.notifyDataSetChanged();
	}
	
	private RecyclerView.Adapter<Row> mAdapter = new RecyclerView.Adapter<Row>() {
		@Override
		public Row onCreateViewHolder(ViewGroup parent, int viewType) {
			Row row = new Row(mInflater.inflate(R.layout.row_home_ranking_user, parent, false));
			return row;
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
	
	public static class Row extends RecyclerView.ViewHolder {
		private TextView mName;
		private NetworkImageView mUserIcon;
		private ImageView mMedal;
		
		public Row(View view) {
			super(view);
			mName = (TextView)view.findViewById(R.id.name);
			mUserIcon = (NetworkImageView)view.findViewById(R.id.user_icon);
			mUserIcon.setDefaultImageResId(R.drawable.mysteryman);
			mMedal = (ImageView)view.findViewById(R.id.medal);
		}
		
		public void bindView(User user, int rank) {
			mName.setText(user.name);
			mUserIcon.setImageUrl(user.iconUrl);

			switch (rank) {
			case 0:
				mMedal.setImageResource(R.drawable.crown_first);
				mMedal.setVisibility(View.VISIBLE);
				break;
			case 1:
				mMedal.setImageResource(R.drawable.crown_second);
				mMedal.setVisibility(View.VISIBLE);
				break;
			case 2:
				mMedal.setImageResource(R.drawable.crown_thrid);
				mMedal.setVisibility(View.VISIBLE);
				break;
			default:
				mMedal.setVisibility(View.INVISIBLE);
			}
		}
	}
}
