package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.Book;
import tv.bokch.data.User;

public abstract class RankingListView<Data> extends BaseListView<Data> {

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
	
	private void initialize(Context context) {
	}

	@Override
	protected void onCellClick(Data data) {
		if (data instanceof Book) {
			//startUserListActivity((Book)data);
			((BaseActivity)getContext()).startBookActivity((Book)data);
		} else if (data instanceof User) {
			((BaseActivity)getContext()).startUserActivity((User)data);
		} else {
			super.onCellClick(data);
		}
	}

	protected abstract int getLayoutResId();

	protected abstract Cell createCell(View view);

	protected class RankingCell extends Cell {
		private ImageView mMedal;
		
		public RankingCell(View view) {
			super(view);
			mMedal = (ImageView)view.findViewById(R.id.medal);
		}
		
		protected void bindView(Data data, int position) {
			super.bindView(data, position);
			//0はヘッダーなので1から
			switch (position) {
			case 1:
				mMedal.setImageResource(R.drawable.crown_first);
				mMedal.setVisibility(View.VISIBLE);
				break;
			case 2:
				mMedal.setImageResource(R.drawable.crown_second);
				mMedal.setVisibility(View.VISIBLE);
				break;
			case 3:
				mMedal.setImageResource(R.drawable.crown_thrid);
				mMedal.setVisibility(View.VISIBLE);
				break;
			default:
				mMedal.setVisibility(View.INVISIBLE);
			}
		}
	}
}
