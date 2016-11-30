package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class History extends Data implements Parcelable {
	public long id;
	public long created;
	public User user;
	public Book book;
	public Review review;

	public History(JSONObject obj) throws JSONException {
		id = obj.optLong("id");
		user = new User(obj.optJSONObject("user"));
		book = new Book(obj.optJSONObject("book"));
		JSONObject _review = obj.optJSONObject("review");
		review = _review == null ? null : new Review(_review);
		created = obj.optLong("created");
	}
	
	public History(Parcel in) {
		id = in.readLong();
		created = in.readLong();
		user = in.readParcelable(User.class.getClassLoader());
		book = in.readParcelable(Book.class.getClassLoader());
		review = in.readParcelable(Review.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(created);
		dest.writeParcelable(user, flags);
		dest.writeParcelable(book, flags);
		dest.writeParcelable(review, flags);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
		public History createFromParcel(Parcel in) {
			return new History(in);
		}
		public History[] newArray(int size) {
			return new History[size];
		}
	};
}
