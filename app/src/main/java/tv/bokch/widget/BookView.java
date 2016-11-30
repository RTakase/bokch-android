package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.BookViewHolder;

public class BookView extends RelativeLayout {
	private BookViewHolder mHolder;
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
		inflate(context, R.layout.view_book, this);
		mHolder = new BookViewHolder(this);
	}

	public void bindView(Book book) {
		if (mHolder != null) {
			mHolder.bindView(book);
		}
	}
}
