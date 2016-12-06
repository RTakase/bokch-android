package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Stack extends Data implements Parcelable {
	public long id;
	public long created;
	public User user;
	public Book book;
	
	public Stack(JSONObject obj) throws JSONException {
		id = obj.optLong("id");
		user = new User(obj.optJSONObject("user"));
		book = new Book(obj.optJSONObject("book"));
		created = obj.optLong("created");
	}
	
	public Stack(Parcel in) {
		id = in.readLong();
		created = in.readLong();
		user = in.readParcelable(User.class.getClassLoader());
		book = in.readParcelable(Book.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(created);
		dest.writeParcelable(user, flags);
		dest.writeParcelable(book, flags);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Stack> CREATOR = new Parcelable.Creator<Stack>() {
		public Stack createFromParcel(Parcel in) {
			return new Stack(in);
		}
		public Stack[] newArray(int size) {
			return new Stack[size];
		}
	};
}
