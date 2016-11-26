package tv.bokch.util;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ApiClient {
	// 複数のAPI呼び出しを並行せずにキューイングするために
	// 単一のクライアントオブジェクトを共有して使う
	static private OkHttpClient cApiClient; // 通常API
	static private OkHttpClient cBackgroundClient; // バックグラウンド処理

	synchronized static public OkHttpClient getApiClient() {
		if (cApiClient == null) {
			final OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(10, TimeUnit.SECONDS);
			builder.readTimeout(60, TimeUnit.SECONDS);
			builder.writeTimeout(60, TimeUnit.SECONDS);
			cApiClient = builder.build();
		}
		return cApiClient;
	}

	synchronized static public OkHttpClient getBackgroundClient() {
		if (cBackgroundClient == null) {
			final OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(10, TimeUnit.SECONDS);
			builder.readTimeout(60, TimeUnit.SECONDS);
			builder.writeTimeout(60, TimeUnit.SECONDS);
			cBackgroundClient = builder.build();
		}
		return cBackgroundClient;
	}

	static public OkHttpClient newClient() {
		final OkHttpClient.Builder builder = new OkHttpClient.Builder();
		return builder.build();
	}
}
