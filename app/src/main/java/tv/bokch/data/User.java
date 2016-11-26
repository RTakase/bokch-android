package tv.bokch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends Data implements Parcelable {
	public String userId;
	public String name;
	public String email;
	public String iconUrl;
	public int score;

	public User(JSONObject obj) throws JSONException {
		if (obj == null) {
			return;
		}
		userId = obj.optString("user_id");
		name = obj.optString("name");
		email = obj.optString("email");
		iconUrl = obj.optString("icon_url");
		score = obj.optInt("score");
	}

	public User(String userId) {
		this.userId = userId;
	}

	public User(Parcel in) {
		userId = in.readString();
		name = in.readString();
		email = in.readString();
		iconUrl = in.readString();
		score = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userId);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(iconUrl);
		dest.writeInt(score);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
