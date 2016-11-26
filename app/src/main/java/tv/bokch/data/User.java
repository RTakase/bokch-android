package tv.bokch.data;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends Data {
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
}
