package tv.bokch.android;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;

public class LoginActivity extends BaseActivity {
	public static final String CLIENT_ID_WEB = "780402054902-o75kj622d9e3qu9qdnerk8gkd0d59heu.apps.googleusercontent.com";
	public static final String LOGIN_URL = "https://accounts.google.com/o/oauth2/auth?scope=%s&redirect_uri=%s&response_type=code&client_id=%s&access_type=offline";
	public static final String SCOPE_GOOGLE_API = "profile email";
	public static final String REDIRECT_URL = "http://bokch.tv.com";

	private TextView mMessageTextView;
	private Button mLoginButton;
	private ProgressBar mProgressBar;
	private boolean mLogining;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		setActionBarTitle(getString(R.string.welcome));

		SharedPreferences pref = this.getSharedPreferences("bokch", MODE_PRIVATE);

		String userId = pref.getString("user_id", null);

		if (!TextUtils.isEmpty(userId)) {
			finish();
		}

		initViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			if (!mLogining) {
				mLogining = true;
				//ログイン後のコールバック
				mMessageTextView.setText(getString(R.string.loging_in));
				mLoginButton.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.VISIBLE);
				String code = getResponseCode(intent.getDataString());
				Timber.d("tks, code = %s", code);
				ApiRequest request = new ApiRequest();
				request.login(null, code, mLoginApiListener);
			}
		}
	}

	private void initViews() {
		mMessageTextView = (TextView)findViewById(R.id.message);
		assert mMessageTextView != null;
		mMessageTextView.setText(getString(R.string.prompt_login));

		mLoginButton = (Button)findViewById(R.id.login_btn);
		assert mLoginButton != null;
		mLoginButton.setOnClickListener(mLoginClickListener);

		mProgressBar = (ProgressBar)findViewById(R.id.progress);
	}

	private String getResponseCode(String response) {
		String key = "code=";
		int index = response.indexOf(key);
		if (index > 0) {
			return response.substring(index + key.length());
		} else {
			return null;
		}
	}

	private View.OnClickListener mLoginClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String url = String.format(LOGIN_URL,
				SCOPE_GOOGLE_API,
				REDIRECT_URL,
				CLIENT_ID_WEB
			);
			try {
				intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
				intent.setData(Uri.parse(url));
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Timber.w(e, null);
				ViewUtils.showErrorToast(LoginActivity.this, getString(R.string.failed_find_chrome));
			}
		}
	};

	private ApiRequest.ApiListener<JSONObject> mLoginApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			mLogining = false;
			Timber.d("tks, %s", response.toString());
			try {
				if (response.isNull("user")) {
					mMessageTextView.setText(getString(R.string.failed_login));
					mProgressBar.setVisibility(View.GONE);
					mLoginButton.setVisibility(View.VISIBLE);
				} else {
					mMessageTextView.setText(getString(R.string.successed_login));
					mProgressBar.setVisibility(View.GONE);
					App app = (App)getApplication();
					app.setMyUser(new User(response.optJSONObject("user")));
					setResult(RESULT_OK);
					finish();
				}
			} catch (JSONException e) {
				Timber.w(e, null);
			}
		}
		@Override
		public void onError(ApiRequest.ApiError error) {
			mLogining = false;
			Timber.d("tks, api error, %s", error.getLocalizedMessage());
			Timber.w(error, null);
			mMessageTextView.setText(getString(R.string.failed_login));
			mProgressBar.setVisibility(View.GONE);
			mLoginButton.setVisibility(View.VISIBLE);
		}
	};
}
