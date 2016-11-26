package tv.bokch.data;

import org.json.JSONException;
import org.json.JSONObject;

public class History extends Data{
	public int id;
	public long created;
	public User user;
	public Book book;
	public Review review;

	public History(JSONObject obj) throws JSONException {
		user = new User(obj.optJSONObject("user"));
		book = new Book(obj.optJSONObject("book"));
		review = new Review(obj.optJSONObject("review"));
		created = obj.optLong("created");
	}
}
