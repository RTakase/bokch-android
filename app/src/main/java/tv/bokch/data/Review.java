package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Review extends Data implements Parcelable {
	public long id;
	public Book book;
	public User user;
	public String comment;
	public int rating;
	public long created;

	public Review () {
		id = 0L;
		book = null;
		user = null;
		comment = "";
		rating = -1;
		created = -1L;
	}

	public Review(JSONObject obj) throws JSONException {
		id = obj.optLong("id");
		comment = obj.optString("comment");
		rating = obj.optInt("rating");
		created = obj.optLong("created");
	}

	public Review(Parcel in) {
		id = in.readLong();
		book = in.readParcelable(Book.class.getClassLoader());
		user = in.readParcelable(User.class.getClassLoader());
		comment = in.readString();
		rating = in.readInt();
		created = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeParcelable(book, flags);
		dest.writeParcelable(user, flags);
		dest.writeString(comment);
		dest.writeInt(rating);
		dest.writeLong(created);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
		public Review createFromParcel(Parcel in) {
			return new Review(in);
		}
		public Review[] newArray(int size) {
			return new Review[size];
		}
	};	
}
