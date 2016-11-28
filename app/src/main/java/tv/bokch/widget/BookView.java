package tv.bokch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.util.Display;

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

	protected class BookViewHolder {
		public NetworkImageView jacket;
		public TextView title;
		public TextView author;
		public TextView publisher;
		public RatingBar ratingAverage;
		public TextView tag;

		public BookViewHolder(View view) {
			jacket = (NetworkImageView)view.findViewById(R.id.jacket);
			jacket.setDefaultImageResId(R.drawable.mysteryman);
			title = (TextView)view.findViewById(R.id.title);
			author = (TextView)view.findViewById(R.id.author);
			publisher = (TextView)view.findViewById(R.id.publisher);
			ratingAverage = (RatingBar)view.findViewById(R.id.rating_average);
			tag = (TextView)view.findViewById(R.id.tag);
		}

		public void bindView(Book book) {
			ViewGroup.LayoutParams params = jacket.getLayoutParams();
			if (params != null) {
				params.width = Display.dpToPx(getContext(), book.largeImageWidth);
				params.height = Display.dpToPx(getContext(), book.largeImageHeight);
				jacket.setLayoutParams(params);
			}
			jacket.setImageUrl(book.largeImageUrl);

			title.setText(book.title);
			author.setText(book.author);
			publisher.setText(book.publisher);
			ratingAverage.setRating(book.ratingAverage * 100);
			tag.setText(book.tag);
		}
	}
}
