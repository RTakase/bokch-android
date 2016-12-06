package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;

public class BookView extends RelativeLayout {
	private BookViewHolder mHolder;
	private View mView;
	private View mEmptyView;

	public BookView(Context context) {
		super(context);
		initialize(context);
	}

	public BookView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	private void initialize(Context context) {
		View root = inflate(context, R.layout.view_book, this);
		mView = root.findViewById(R.id.content);
		mEmptyView = root.findViewById(R.id.empty);

		mHolder = new BookViewHolder(this);
	}

	public void setEmpty(boolean empty) {
		if (empty) {
			mView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		} else {
			mView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		}
	}

	public void bindView(Book book) {
		if (mHolder != null) {
			mHolder.bindView(book);
		}
	}

	public void setRatingAverageSuffix(String str) {
		if (mHolder != null) {
			mHolder.setRatingAverageSuffix(str);
		}
	}
}
