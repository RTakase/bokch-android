package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tv.bokch.R;

public abstract class RankingListView<Data> extends RecyclerView<Data> {

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
