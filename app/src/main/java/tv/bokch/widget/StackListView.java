package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.android.BaseActivity;
import tv.bokch.data.BookViewHolder;
import tv.bokch.data.Stack;
import tv.bokch.data.ReviewViewHolder;
import tv.bokch.data.Stack;
import tv.bokch.data.UserViewHolder;

public abstract class StackListView extends BaseListView<Stack> {

	public StackListView(Context context) {
		super(context);
		initialize(context);
	}
	public StackListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public StackListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
	}
	
	@Override
	protected void onCellClick(Stack stack) {
		((BaseActivity)getContext()).startBookActivity(stack.book);
	}
	
	@Override
	protected Cell createCell(View view) {
		return new StackCell(view);
	}

	protected class StackCell extends Cell {
		protected BookViewHolder mBook;
		protected UserViewHolder mUser;
		protected TextView mCreated;
		
		private boolean mDisableBookClick;
		private boolean mDisableUserClick;
		
		public StackCell(View view) {
			super(view);
			mBook = new BookViewHolder(view);
			mUser = new UserViewHolder(view);
			mCreated = (TextView)view.findViewById(R.id.created);
		}
		
		public void bindView(final Stack stack, int position) {
			super.bindView(stack, position);
			mBook.bindView(stack.book);
			if (!mDisableBookClick) {
				mBook.setOnJacketClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((BaseActivity)getContext()).startBookActivity(stack.book);
					}
				});
			}
			mUser.bindView(stack.user);
			if (!mDisableUserClick) {
				mUser.setOnIconClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((BaseActivity)getContext()).startUserActivity(stack.user);
					}
				});
			}

			if (mCreated != null) {
				mCreated.setText(DateFormat.format("yyyy/MM/dd", stack.created * 1000));
			}
		}
		
		public void disableBookClick() {
			mDisableBookClick = true;
		}
		public void disableUserClick() {
			mDisableUserClick = true;
		}
	}
}
