package tv.bokch.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;

import timber.log.Timber;

public class JsonUtils {

	public static int DEPTH = 0;
	public static final String TAB = "\t";

	public static void dump(Object obj) {
		Timber.d("****************************************");
		dump("root", obj);
		Timber.d("****************************************");
	}
	public static void dump(String key, Object obj) {
		if (obj == null) {
			return;
		}
		String tab = "";
		if (DEPTH > 0) {
			String[] tabs = new String[DEPTH];
			Arrays.fill(tabs, TAB);
			tab = TextUtils.join("", tabs);
		}
		if (obj instanceof JSONObject) {
			DEPTH++;
			Timber.d("%s%s: {", tab, key);
			dumpJsonObject((JSONObject)obj);
			Timber.d("%s}",tab);
			DEPTH--;
		} else if (obj instanceof JSONArray) {
			DEPTH++;
			Timber.d("%s%s: [", tab, key);
			dumpJsonArray((JSONArray)obj);
			Timber.d("%s]", tab);
			DEPTH--;
		} else {
			Timber.d("%s%s: %s", tab, key, obj.toString());
		}
	}
	
	public static void dumpJsonObject(JSONObject obj) {
		if (obj == null) {
			return;
		}
		Iterator it = obj.keys();
		while (it.hasNext()) {
			String key = (String)it.next();
			dump(key, obj.opt(key));
		}
	}

	public static void dumpJsonArray(JSONArray array) {
		if (array == null) {
			return;
		}
		for (int i = 0; i < array.length(); i++) {
			Object obj = array.opt(i);
			dump(String.valueOf(i), obj);
		}
	}
}
