package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.StatableListView;
import tv.bokch.widget.BookListView;

public class BookListActivity extends BaseActivity {
	private StatableListView<Book> mContent;
	private boolean mLoaded;
	private String mUserId;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_list);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.acitivity_title_book_list);
		
		Intent intent = getIntent();
		mUserId = intent.getStringExtra("user_id");
		if (TextUtils.isEmpty(mUserId)) {
			finish();
			return;
		}
		mContent = (StatableListView<Book>)findViewById(R.id.content);
		BookListView listview = new BookListView(this);
		mContent.addListView(listview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!mLoaded) {
			ApiRequest request = new ApiRequest();
			request.recent(null, mUserId, mRecentApiListener);
		}
	}
	
	private ApiRequest.ApiListener<JSONObject> mRecentApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			try {
				JSONArray array = response.optJSONArray("histories");
				Timber.d("tks, arrays = %d", array.length());
				if (array == null) {
					return;
				}
				int length = array.length();
				ArrayList<Book> books = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					JSONObject obj = array.optJSONObject(i);
					if (obj != null) {
						History history = new History(obj);
						//たまに取得できないやつがいるのでスルー
						if (!TextUtils.isEmpty(history.book.title)) {
							books.add(history.book);
						}
					}
				}
				mLoaded = mContent.onData(books);
			} catch (JSONException e) {
				Timber.w(e, null);
				Toast.makeText(BookListActivity.this, getString(R.string.failed_data_set), Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			Timber.w(error, null);
			Toast.makeText(BookListActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
		}
	};
}
