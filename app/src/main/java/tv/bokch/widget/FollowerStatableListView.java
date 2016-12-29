package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.History;

public class FollowerStatableListView extends StatableListView<History> {
	public FollowerStatableListView(Context context) {
		super(context);
	}

	public FollowerStatableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FollowerStatableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected View createEmptyView(final Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_timeline_empty, this, false);
		View ranking = view.findViewById(R.id.find_from_ranking_btn);
		ranking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getContext()).startRankingActivity();
			}
		});
		View list = view.findViewById(R.id.find_from_list_btn);
		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity)getContext()).startUserListActivity(getContext().getString(R.string.activity_all_users));
			}
		});		
		return view;
	}
}
