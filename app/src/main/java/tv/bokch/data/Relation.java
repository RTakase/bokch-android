package tv.bokch.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Relation {
	public User followee;
	public User follower;

	public Relation(JSONObject obj) throws JSONException {
		if (obj == null) {
			return;
		}

		followee = new User(obj.optJSONObject("followee"));
		follower = new User(obj.optJSONObject("follower"));
	}
}
