package tv.bokch.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class MyUser extends User {
	public boolean isFollow;

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
		Timber.d("tks, %s", obj.toString());
		isFollow = obj.optBoolean("my_followee");
		Timber.d("tks, is follow = %b", isFollow);
	}
}
