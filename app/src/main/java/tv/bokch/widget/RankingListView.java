package tv.bokch.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import tv.bokch.R;

public abstract class RankingListView<Data> extends HorizontalListView<Data> {

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

	protected abstract int getLayoutResId();

	protected abstract Row createRow(View view);

	protected class RankingRow extends Row {
		private ImageView mMedal;
		
		public RankingRow(View view) {
			super(view);
			mMedal = (ImageView)view.findViewById(R.id.medal);
		}
		
		protected void bindView(Data data, int position) {
			super.bindView(data, position);
			switch (position) {
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
