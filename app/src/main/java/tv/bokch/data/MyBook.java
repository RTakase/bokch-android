package tv.bokch.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class MyBook extends Book {
	public User user;
	public Review review;
	public History history;
	
	public MyBook(JSONObject obj) throws JSONException {
		super(obj);
		setStatus(obj);
	}
	
	public MyBook(String bookId) {
		super(bookId);
	}
	
	public MyBook(Parcel in) {
		super(in);
	}
	
	public void setStatus(JSONObject obj) throws JSONException {
		JSONObject status = obj.optJSONObject("my_status");
		if (status == null) {
			return;
		}

		JSONObject _history = status.optJSONObject("history");
		this.history = _history == null ? null : new History(_history);
		
		JSONObject _review = status.optJSONObject("review");
		this.review = _review == null ? null : new Review(_review);
	}
}
