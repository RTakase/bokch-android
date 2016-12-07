package tv.bokch.data;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import tv.bokch.R;

public class ReviewViewHolder {
	public RatingBar rating;
	public TextView comment;
	protected TextView created;

	public ReviewViewHolder(View view) {
		rating = (RatingBar)view.findViewById(R.id.rating);
		comment = (TextView)view.findViewById(R.id.comment);
		created = (TextView)view.findViewById(R.id.created);
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
		if (created != null) {
			created.setText(DateFormat.format("yyyy/MM/dd", review.created * 1000));
		}

	}
}