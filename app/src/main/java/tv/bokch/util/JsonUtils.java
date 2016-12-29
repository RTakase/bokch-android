package tv.mixch.util;

import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import timber.log.Timber;

public class JsonUtils {

	public static int DEPTH = 0;
	public static final String TAB = "\t";

	public static void dump(Object obj) {
		Timber.d("****************************************");
		dump("", obj);
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
		} else if (obj instanceof String) {
			Timber.d("%s%s: %s", tab, key, (String)obj);
		} else if (obj instanceof Integer) {
			Timber.d("%s%s: %d", tab, key,(Integer)obj);
		} else if (obj instanceof Float) {
			Timber.d("%s%s: %f", tab, key, (Float)obj);
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
	/**
	 * JSONObject を Bundle に適切に変換して返す.
	 * JSONArray は ArrayList<Bundle> に変換される.
	 *
	 * @param json JSONObject
	 * @return Bundle
	 */
	public static Bundle toBundle(final JSONObject json) {
		final Bundle bundle = new Bundle();
		if (json != null) {
			final Iterator<String> iterator = json.keys();
			while (iterator.hasNext()) {
				final String key = iterator.next();
				if (json.isNull(key)) {
					bundle.putString(key, null);
					continue;
				}
				final Object value = json.opt(key);
				if (value instanceof JSONObject) {
					bundle.putBundle(key, toBundle((JSONObject)value));
				} else if (value instanceof JSONArray) {
					bundle.putParcelableArrayList(key, toBundle((JSONArray)value));
				} else if (value instanceof Boolean) {
					bundle.putBoolean(key, (boolean)value);
				} else if (value instanceof String) {
					bundle.putString(key, (String)value);
				} else if (value instanceof Integer) {
					bundle.putInt(key, (int)value);
				} else if (value instanceof Long) {
					bundle.putLong(key, (long)value);
				} else if (value instanceof Double) {
					bundle.putDouble(key, (double)value);
				}
			}
		}
		return bundle;
	}

	/**
	 * JSONArray を ArrayList<Bundle> に適切に変換して返す.
	 *
	 * @param array JSONArray
	 * @return ArrayList<Bundle>
	 */
	public static ArrayList<Bundle> toBundle(final JSONArray array) {
		final ArrayList<Bundle> bundles = new ArrayList<>();
		if (array != null) {
			for (int i = 0, size = array.length(); i < size; i++) {
				bundles.add(toBundle(array.optJSONObject(i)));
			}
		}
		return bundles;
	}
}
