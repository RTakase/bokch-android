package tv.bokch.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.Stack;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.UserView;

public class MyPageActivity extends TabActivity {
	
	public static final int INDEX_HISTORY = 0;
	public static final int INDEX_STACK = 1;
	private User mLoginUser;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypage);

		setActionBarTitle(R.string.activity_mypage);

		mLoginUser = ((App)getApplicationContext()).getMyUser();
		initialize();
	}

	private void initialize() {
		UserView userView = (UserView)findViewById(R.id.user);
		assert userView != null;
		userView.bindView(mLoginUser);

		View followButton = findViewById(R.id.follow_btn);
		followButton.setVisibility(View.GONE);
	}

	@Override
	protected int getTabCount() {
		return 2;
	}
	
	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return getString(R.string.title_histories);
		case INDEX_STACK:
			return getString(R.string.title_stacks);
		default:
			return null;
		}
	}
	
	
	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return BookGridFragment.newInstance();
		case INDEX_STACK:
			return BookGridFragment.newInstance();
		default:
			return null;
		}
	}
	
	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_HISTORY:
			request.recent(null, mLoginUser.userId, null, listener);
			return true;
		case INDEX_STACK:
			request.stack(null, mLoginUser.userId, null, listener);
			return true;
		default:
			return false;
		}
	}
	
	
	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		ArrayList<Book> books;
		switch (index) {
		case INDEX_HISTORY:
			books = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					Book book = history.book;
					if (!TextUtils.isEmpty(book.title)) {
						books.add(book);
					}
				}
			}
			Collections.reverse(books);
			return books;
		case INDEX_STACK:
			books = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Stack stack = new Stack(obj);
					Book book = stack.book;
					if (!TextUtils.isEmpty(book.title)) {
						books.add(book);
					}
				}
			}
			return books;
		default:
			return null;
		}
	}
	
	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return "histories";
		case INDEX_STACK:
			return "stacks";
		default:
			return null;
		}
	}
}
