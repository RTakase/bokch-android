package tv.bokch.data;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import tv.bokch.R;
import tv.bokch.widget.CircleNetworkImageView;

public class BookViewHolder {
	public CircleNetworkImageView jacket;
	public TextView title;
	public TextView author;
	public TextView publisher;
	public RatingBar ratingAverage;
	public TextView suffixRatingAverage;
	public TextView tag;
	public int jacketWidth = -1;
	public int jacketHeight = -1;

	public BookViewHolder(View view) {
		jacket = (CircleNetworkImageView)view.findViewById(R.id.jacket);
		title = (TextView)view.findViewById(R.id.title);
		if (title == null) {
			title = (TextView)view.findViewById(R.id.book_title);
		}
		author = (TextView)view.findViewById(R.id.author);
		//publisher = (TextView)view.findViewById(R.id.publisher);
		ratingAverage = (RatingBar)view.findViewById(R.id.rating_average);
		tag = (TextView)view.findViewById(R.id.tag);
		suffixRatingAverage = (TextView)view.findViewById(R.id.suffix_rating);
	}

	public void bindView(Book book) {
		if (jacket != null && !TextUtils.isEmpty(book.largeImageUrl)) {
			jacket.setDefaultImageResId(R.drawable.mysteryman);
			ViewGroup.LayoutParams params = jacket.getLayoutParams();
			if (params != null) {
				jacketWidth = book.largeImageWidth;
				jacketHeight = book.largeImageHeight;
				params.width = jacketWidth;
				params.height = jacketHeight;
				jacket.setLayoutParams(params);
			}
			jacket.setImageUrl(book.largeImageUrl);
		}

		if (title != null && !TextUtils.isEmpty(book.title)) {
			title.setText(book.title);
		}

		if (author != null) {
			if (!TextUtils.isEmpty(book.author) && !book.author.matches("^[\\s　]+$")) {
				author.setVisibility(View.VISIBLE);
				author.setText(book.author);
			} else {
				author.setVisibility(View.INVISIBLE);
			}
		}

		if (publisher != null && !TextUtils.isEmpty(book.publisher)) {
			publisher.setText(book.publisher);
		}

		if (ratingAverage != null && book.ratingAverage > 0) {
			ratingAverage.setRating(book.ratingAverage);
		}

		if (tag != null && !TextUtils.isEmpty(book.tag)) {
			tag.setText(book.tag);
		}
	}

	public void setOnJacketClickListener(View.OnClickListener listener) {
		if (jacket != null) {
			jacket.setOnClickListener(listener);
		}
	}
	public void setRatingAverageSuffix(String str) {
		if (suffixRatingAverage != null) {
			suffixRatingAverage.setText(str);
		}
	}

	public void hideBookTitle() {
		title.setVisibility(View.GONE);
	}
}