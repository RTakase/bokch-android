package tv.bokch.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Book extends Data {
	public String bookId;
	public String title;
	public String author;
	public String detailPageUrl;
	public String largeImageUrl;
	public int numberOfPages;
	public String publisher;
	public boolean inOffice;
	public String tag;
	public float ratingAverage;
	public int score;

	public Book(JSONObject obj) throws JSONException {
		if (obj == null) {
			return;
		}
		bookId = obj.optString("book_id");
		title = optString(obj, "title");
		author = obj.optString("author");
		detailPageUrl = obj.optString("detail_page_url");
		largeImageUrl = obj.optString("large_image_url");
		numberOfPages = obj.optInt("number_of_pages");
		publisher = obj.optString("publisher");
		inOffice  = obj.optBoolean("in_office");
		tag = obj.optString("tag");
		ratingAverage = (float)obj.optDouble("rating_average");
		score = obj.optInt("score");
	}

	public Book(String bookId) {
		this.bookId = bookId;
	}
}
