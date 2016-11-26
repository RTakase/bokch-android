package tv.bokch.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {
	public Book book;
	public User user;
	public String comment;
	public int rating;
	public long created;
	
	public Review(JSONObject obj) throws JSONException {
		book = new Book(obj.optString("book_id"));
		user = new User(obj.optString("user_id"));
		comment = obj.optString("comment");
		rating = obj.optInt("rating");
		created = obj.optLong("created");
	}
}
