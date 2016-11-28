package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.widget.NetworkImageView;

public class BookActivity extends BaseActivity {
	
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_USERS = 1;
	public static final int NUMBER_OF_PAGES = 2;

	BookRankingFragment mReviewFragment;
	BookRankingFragment mUsersFragment;

	private Book mBook;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		initialize();
	}

	private void initialize() {
		NetworkImageView jacket = (NetworkImageView)findViewById(R.id.jacket);
		assert jacket != null;
		jacket.setDefaultImageResId(R.drawable.mysteryman);
		jacket.setImageUrl(mBook.largeImageUrl);

		TextView title = (TextView)findViewById(R.id.title);
		assert title != null;
		title.setText(mBook.title);

		TextView author = (TextView)findViewById(R.id.author);
		assert author != null;
		author.setText(mBook.author);

		TextView publisher = (TextView)findViewById(R.id.publisher);
		assert publisher != null;
		publisher.setText(mBook.publisher);

		TextView ratingAverage = (TextView)findViewById(R.id.rating_average);
		assert ratingAverage != null;
		ratingAverage.setText(String.valueOf(mBook.ratingAverage));

		TextView tag = (TextView)findViewById(R.id.tag);
		assert tag != null;
		tag.setText(mBook.tag);

		Button amazon = (Button)findViewById(R.id.amazon_btn);
		assert amazon != null;
		amazon.setOnClickListener(mAmazonClickListener);

		Button add = (Button)findViewById(R.id.add_btn);
		assert add != null;
		add.setOnClickListener(mAddClickListener);
	}

	private View.OnClickListener mAmazonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBook.detailPageUrl));
			startActivity(intent);
		}
	};

	private View.OnClickListener mAddClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(BookActivity.this, "登録しちゃいましょう。", Toast.LENGTH_SHORT).show();
		}
	};
}
