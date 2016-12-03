package tv.bokch.data;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import tv.bokch.R;

public class ReviewViewHolder {
	public RatingBar rating;
	public TextView comment;

	public ReviewViewHolder(View view) {
		rating = (RatingBar)view.findViewById(R.id.rating);
		comment = (TextView)view.findViewById(R.id.comment);
	}

	public void bindView(Review review) {
		if (review == null) {
			return;
		}
		if (rating != null) {
			rating.setRating(review.rating);
		}
		if (comment != null) {
			comment.setText(review.comment);
		}
	}
}