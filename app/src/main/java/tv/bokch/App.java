package tv.bokch;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import timber.log.Timber;
import tv.bokch.data.User;

public class App extends Application {
	protected User myUser;
	private Tracker mTracker;

	@Override
	public void onCreate() {
		super.onCreate();
		Timber.plant(new Timber.DebugTree());

		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		mTracker = analytics.newTracker(R.xml.app_tracker);

	}

	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User user) {
		if (user != null) {
			myUser = user;
			SharedPreferences pref = getSharedPreferences("bokch", MODE_PRIVATE);
			pref.edit().putString("user_id", myUser.userId).apply();
		}
	}

	public Tracker getTracker() {
		return mTracker;
	}
}
