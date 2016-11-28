package tv.bokch.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.Review;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BookView;

public class BookActivity extends TabActivity {
	
	public static final int INDEX_REVIEW = 0;
	public static final int INDEX_USERS = 1;

	private Book mBook;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_book);
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		initialize();
	}

	private void initialize() {
		BookView book = (BookView)findViewById(R.id.book);
		assert book != null;
		book.bindView(mBook);

		Button add = (Button)findViewById(R.id.add_btn);
		assert add != null;
		add.setOnClickListener(mAddClickListener);
	}

	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return ReviewFragment.newInstance();
		case INDEX_USERS:
			return UserFragment.newInstance();
		default:
			return null;
		}
	}

	@Override
	protected int getTabCount() {
		return 2;
	}

	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return getString(R.string.title_reviews);
		case INDEX_USERS:
			return getString(R.string.title_read_users);
		default:
			return null;
		}
	}

	@Override
	protected void requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_REVIEW:
			request.review(mBook.bookId, null, listener);
		case INDEX_USERS:
			request.recent(mBook.bookId, null, listener);
		default:
		}
	}

	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_REVIEW:
			return "reviews";
		case INDEX_USERS:
			return "users";
		default:
			return null;
		}
	}

	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_REVIEW:
			ArrayList<Review> reviews = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Review review = new Review(obj);
					reviews.add(review);
				}
			}
			return reviews;
		case INDEX_USERS:
			ArrayList<User> users = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Review review = new Review(obj);
					users.add(review.user);
				}
			}
			return users;
		default:
			return null;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_book, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_amazon:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBook.detailPageUrl));
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private View.OnClickListener mAddClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(BookActivity.this, "登録しちゃいましょう。", Toast.LENGTH_SHORT).show();
		}
	};
}
