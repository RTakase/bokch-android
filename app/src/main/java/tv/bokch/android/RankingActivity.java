package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;

public class RankingActivity extends TabActivity {
	
	public static final int INDEX_BOOK = 0;
	public static final int INDEX_USER = 1;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		setActionBarTitle(R.string.ranking_total);
	}
	
	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_BOOK:
			return "本";
		case INDEX_USER:
			return "人";
		default:
			return null;
		}
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_BOOK:
			return BookRankingFragment.newInstance();
		case INDEX_USER:
			return UserRankingFragment.newInstance();
		default:
			return null;
		}
	}
	
	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_BOOK:
			request.ranking_book_total(listener);
			return true;
		case INDEX_USER:
			request.ranking_user_total(listener);
			return true;
		default:
			return false;
		}
	}
	
	@Override
	protected int getTabCount() {
		return 2;
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_BOOK:
			ArrayList<Book> books = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Book book = new Book(obj);
					if (!TextUtils.isEmpty(book.title)) {
						books.add(book);
					}
				}
			}
			return books;
		case INDEX_USER:
			ArrayList<User> users = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					User User = new User(obj);
					users.add(User);
				}
			}
			return users;
		default:
			return null;
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_BOOK:
			return "books";
		case INDEX_USER:
			return "users";
		default:
			return null;
		}
	}
}
