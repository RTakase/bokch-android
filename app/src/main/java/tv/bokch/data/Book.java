package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Book extends Data implements Parcelable {
	public String bookId;
	public String title;
	public String author;
	public String detailPageUrl;
	public String largeImageUrl;
	public int largeImageWidth;
	public int largeImageHeight;
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
		largeImageWidth = obj.optInt("large_image_width");
		largeImageHeight = obj.optInt("large_image_height");
		numberOfPages = obj.optInt("number_of_pages");
		publisher = obj.optString("publisher");
		inOffice  = obj.optBoolean("in_office");
		tag = optString(obj, "tag");
		ratingAverage = (float)obj.optDouble("rating_average");
		score = obj.optInt("score");
	}

	public Book(String bookId) {
		this.bookId = bookId;
	}

	public Book(Parcel in) {
		bookId = in.readString();
		title = in.readString();
		author = in.readString();
		detailPageUrl = in.readString();
		largeImageUrl = in.readString();
		largeImageWidth = in.readInt();
		largeImageHeight = in.readInt();
		numberOfPages = in.readInt();
		publisher = in.readString();
		inOffice  = in.readInt() == 1;
		tag = in.readString();
		ratingAverage = in.readFloat();
		score = in.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(bookId);
		dest.writeString(title);
		dest.writeString(author);
		dest.writeString(detailPageUrl);
		dest.writeString(largeImageUrl);
		dest.writeInt(largeImageWidth);
		dest.writeInt(largeImageHeight);
		dest.writeInt(numberOfPages);
		dest.writeString(publisher);
		dest.writeInt(inOffice ? 1 : 0);
		dest.writeString(tag);
		dest.writeFloat(ratingAverage);
		dest.writeInt(score);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
}
