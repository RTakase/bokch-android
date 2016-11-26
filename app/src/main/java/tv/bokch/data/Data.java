package tv.bokch.data;

import org.json.JSONObject;

public class Data {
	protected String optString(JSONObject obj, String name) {
		return optString(obj, name, null);
	}

	protected String optString(JSONObject obj, String name, String fallback) {
		if (obj.isNull(name)) {
			return null;
		}
		return obj.optString(name, fallback);
	}
}
