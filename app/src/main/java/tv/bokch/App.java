package tv.bokch;

import android.app.Application;
import android.content.SharedPreferences;

import timber.log.Timber;
import tv.bokch.data.User;

public class App extends Application {
	protected User myUser;
	@Override
	public void onCreate() {
		super.onCreate();
		Timber.plant(new Timber.DebugTree());
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
}
