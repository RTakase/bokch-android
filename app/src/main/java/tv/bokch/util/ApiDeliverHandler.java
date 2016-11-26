package tv.bokch.util;

import android.os.Handler;
import android.os.Looper;

class ApiDeliverHandler {
	static private Handler cHandler;

	synchronized static Handler getHandler() {
		if (cHandler == null) {
			cHandler = new Handler(Looper.getMainLooper());
		}
		return cHandler;
	}
}
