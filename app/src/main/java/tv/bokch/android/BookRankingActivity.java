package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.util.ApiRequest;

public class BookRankingActivity extends TabActivity {

	public static final int INDEX_WEEKLY = 0;
	public static final int INDEX_TOTAL = 1;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_ranking);
		super.onCreate(savedInstanceState);
		setActionBarTitle(R.string.acitivity_title_book_ranking);
	}
	
	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_WEEKLY:
			return getString(R.string.ranking_weekly);
		case INDEX_TOTAL:
			return getString(R.string.ranking_total);
		default:
			return null;
		}
	}
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			return BookRankingFragment.newInstance();
		default:
			return null;
		}
	}
	
	@Override
	protected void requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_WEEKLY:
			request.ranking_book_weekly(listener);
			break;
		case INDEX_TOTAL:
			request.ranking_book_total(listener);
			break;
		default:
		}
	}
	
	@Override
	protected int getTabCount() {
		return 2;
	}
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			ArrayList<Book> res = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Book book = new Book(obj);
					if (!TextUtils.isEmpty(book.title)) {
						res.add(book);
					}
				}
			}
			return res;
		default:
			return null;
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_WEEKLY:
		case INDEX_TOTAL:
			return "books";
		default:
			return null;
		}
	}
}
