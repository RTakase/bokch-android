package tv.bokch.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

public class MyUser extends User {
	public long followId;

	public MyUser(JSONObject obj) throws JSONException {
		super(obj);
		setStatus(obj);
	}

	public MyUser(String userId) {
		super(userId);
	}

	public MyUser(Parcel in) {
		super(in);
	}

	private void setStatus(JSONObject obj) throws JSONException {
		followId = obj.optLong("my_followee");
	}
}
