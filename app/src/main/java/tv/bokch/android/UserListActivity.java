package tv.bokch.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;
import tv.bokch.R;
import tv.bokch.data.Book;
import tv.bokch.data.History;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.widget.BaseListView;
import tv.bokch.widget.UserListView;

public class UserListActivity extends ListActivity<User> {
	private Book mBook;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_list);
		
		super.onCreate(savedInstanceState);
		
		setActionBarTitle(R.string.title_users_with_this_book);
		
		Intent intent = getIntent();
		mBook = intent.getParcelableExtra("data");
		if (mBook == null) {
			finish();
			return;
		}
		Timber.d("tks, %s", mBook.bookId);
	}

	@Override
	protected BaseListView<User> createListView() {
		return new UserListView(this);
	}

	@Override
	protected void request(ApiRequest.ApiListener<JSONObject> listener) {
		ApiRequest r = new ApiRequest();
		r.recent(mBook.bookId, null, listener);
	}

	@Override
	protected String getKey() {
		return "histories";
	}

	@Override
	protected ArrayList<User> getData(JSONArray array) throws JSONException {
		ArrayList<User> histories = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			History history = new History(array.getJSONObject(i));
			//本IDをもとに取得した履歴には本情報が入っていないので取得中チェックをしなくて良い
			histories.add(history.user);
		}
		Collections.reverse(histories);
		return histories;
	}
}
