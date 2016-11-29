package tv.bokch.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;

public class FullBookRankingListView extends RankingListView<Book> {
	
	public FullBookRankingListView(Context context) {
		super(context);
		initialize(context);
	}
	public FullBookRankingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	public FullBookRankingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setOrientation(Orientation.Vertical);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.cell_full_ranking_book;
	}
	
	@Override
	protected Cell createCell(View view) {
		return new FullBookRankingCell(view);
	}
	
	@Override
	protected int getFooterResId() {
		return R.layout.cell_footer;
	}

	@Override
	protected int getHeaderResId() {
		return R.layout.cell_header;
	}

	
	protected class FullBookRankingCell extends RankingCell {
		private NetworkImageView mJacket;
		private TextView mTitle;
		private RatingBar mRatingAverage;
		private TextView mScore;
		private TextView mTag;
		private Button mMoreButton;
		
		public FullBookRankingCell(View view) {
			super(view);
			mJacket = (NetworkImageView)view.findViewById(R.id.jacket);
			mJacket.setDefaultImageResId(R.drawable.mysteryman);
			mTitle = (TextView)view.findViewById(R.id.title);
			mRatingAverage = (RatingBar)view.findViewById(R.id.rating_average);
			mTag = (TextView)view.findViewById(R.id.tag);
			mScore = (TextView)view.findViewById(R.id.score);
			mMoreButton = (Button)view.findViewById(R.id.more_btn);
		}
		
		public void bindView(Book book, int position) {
			super.bindView(book, position);
			mTitle.setText(book.title);
			ViewGroup.LayoutParams params = mJacket.getLayoutParams();
			if (params != null) {
				params.width = mDisplay.toPixels(book.largeImageWidth);
				params.height = mDisplay.toPixels(book.largeImageHeight);
				mJacket.setLayoutParams(params);
			}
			mJacket.setImageUrl(book.largeImageUrl);			
			if (TextUtils.isEmpty(book.tag)) {
				mTag.setVisibility(View.GONE);
			} else {
				mTag.setText(book.tag);
			}
			if (book.score <= 0) {
				mScore.setVisibility(View.GONE);
			} else {
				mScore.setText(String.format("スコア：%d", book.score));
			}
			mRatingAverage.setRating(book.ratingAverage * 100);
			mMoreButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Timber.d("tks, hoge");
				}
			});
		}
	}
}
