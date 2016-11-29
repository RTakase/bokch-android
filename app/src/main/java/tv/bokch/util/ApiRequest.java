package tv.bokch.util;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;
import tv.bokch.data.Review;

public class ApiRequest {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static String API_HOME = "http://52.198.155.49/";
	
	public static String API_RECENT = "histories";
	public static String API_REVIEW = "reviews";
	public static String API_BOOK = "books/%s";
	public static String API_RANKING_USER_WEEKLY = "ranking/user/weekly";
	public static String API_RANKING_BOOK_WEEKLY = "ranking/book/weekly";
	public static String API_RANKING_USER_TOTAL = "ranking/user/total";
	public static String API_RANKING_BOOK_TOTAL = "ranking/book/total";

	public static String API_LOGIN = "login";

	public static String PREFIX = ".json";
	


	public ApiRequest() {
	}

	private HttpUrl getUrl(String path) {
		return HttpUrl.parse(API_HOME + path);
	}

	private HttpUrl.Builder getUrlBuilder(String path) {
		return getUrl(TextUtils.concat(path, PREFIX).toString()).newBuilder();
	}

	private void getJsonObject(HttpUrl url, ApiListener<JSONObject> listener) {
		Request request = newRequestBuilder(url).get().build();
		startJsonObjectApiDeliver(request, listener);
	}

	private void postJsonObject(HttpUrl url, RequestBody body, ApiListener<JSONObject> listener) {
		if (body == null) {
			FormBody.Builder formBodyBuilder = new FormBody.Builder();
			body = formBodyBuilder.build();
		}
		Request request = newRequestBuilder(url).post(body).build();
		startJsonObjectApiDeliver(request, listener);
	}

	private Request.Builder newRequestBuilder(HttpUrl url) {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		return builder;
	}

	private void startJsonObjectApiDeliver(Request request, ApiListener<JSONObject> listener) {
		JSONObjectApiDeliver deliver = new JSONObjectApiDeliver(listener);
		startDeliver(ApiClient.getApiClient(), request, deliver);
	}

	private void startDeliver(OkHttpClient client, Request request, Callback callback) {
		Call call = client.newCall(request);
		call.enqueue(callback);
	}

	/******************************************************************************************/

	public void login(String userId, String code, ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_LOGIN);
		if (userId != null) {
			url.addQueryParameter("user_id", userId);
		}
		if (code != null) {
			url.addQueryParameter("code", code);
		}
		getJsonObject(url.build(), listener);
	}

	public void recent(ApiListener<JSONObject> listener) {
		recent(null, null, listener);
	}

	public void recent(String bookId, String userId, ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_RECENT);
		if (bookId != null) {
			url.addQueryParameter("book_id", bookId);
		}
		if (userId != null) {
			url.addQueryParameter("user_id", userId);
		}
		getJsonObject(url.build(), listener);
	}

	public void review(String bookId, String userId, ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_REVIEW);
		if (bookId != null) {
			url.addQueryParameter("book_id", bookId);
		}
		if (userId != null) {
			url.addQueryParameter("user_id", userId);
		}
		getJsonObject(url.build(), listener);
	}

	public void post_history(String bookId, String userId, int rating, String comment, ApiListener<JSONObject> listener) throws JSONException {
		HttpUrl.Builder url = getUrlBuilder(API_RECENT);

		JSONObject review = new JSONObject();
		review.put("book_id", bookId);
		review.put("user_id", userId);
		review.put("rating", rating);
		review.put("comment", comment);
		
		JSONObject json = new JSONObject();
		json.put("user_id", userId);
		json.put("book_id", bookId);
		json.put("review", review);

		RequestBody body = RequestBody.create(JSON, json.toString());
		postJsonObject(url.build(), body, new PostApiListener(listener));
	}

	public void ranking_user_weekly(ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_RANKING_USER_WEEKLY);
		getJsonObject(url.build(), listener);
	}
	
	public void ranking_book_weekly(ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_RANKING_BOOK_WEEKLY);
		getJsonObject(url.build(), listener);
	}
	
	public void ranking_user_total(ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_RANKING_USER_TOTAL);
		getJsonObject(url.build(), listener);
	}
	
	public void ranking_book_total(ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(API_RANKING_BOOK_TOTAL);
		getJsonObject(url.build(), listener);
	}

	public void book(String bookId, String userId, ApiListener<JSONObject> listener) {
		HttpUrl.Builder url = getUrlBuilder(String.format(API_BOOK, bookId));
		url.addQueryParameter("user_id", userId);
		getJsonObject(url.build(), listener);
	}
	
	public interface ApiListener<T> {
		// 通信がステータスコード200系で成功した場合に呼び出される
		void onSuccess(T response);

		// 通信がステータスコード200系でない場合に呼び出される
		void onError(ApiError error);
	}

	protected class PostApiListener implements ApiListener<JSONObject> {
		private ApiListener<JSONObject> mListener;
		public PostApiListener(ApiListener<JSONObject> listener) {
			mListener = listener;
		}
		@Override
		public void onSuccess(JSONObject response) {
			if (mListener != null) {
				mListener.onSuccess(response);
			}
		}

		@Override
		public void onError(ApiError error) {
			if (mListener != null) {
				mListener.onError(error);
			}
		}
	}

	public static class ApiError extends IOException {
		final public Call call;
		final public Response response;

		public ApiError(Call call, Response response, Exception ex) {
			super(ex);
			this.call = call;
			this.response = response;
		}

		public String getResponseBodyString() {
			if (response == null) {
				return null;
			}
			ResponseBody body = response.body();
			if (body == null) {
				return null;
			}
			try {
				return body.string();
			} catch (IOException ex) {
				return null;
			}
		}
	}

	public static class JSONObjectApiDeliver implements Callback {

		ApiListener<JSONObject> mListener;

		public JSONObjectApiDeliver(ApiListener<JSONObject> listener) {
			mListener = listener;
		}

		protected JSONObject decode(Response response) throws Exception {
			String body = response.body().string();
			response.body().close();
			return new JSONObject(body);
		}

		public void onFailure(Call call, IOException ex) {
			deliverError(call, null, ex);
		}

		@Override
		public void onResponse(Call call, Response response) throws IOException {
			// 非200系の場合はonErrorで通知する
			if (!response.isSuccessful()) {
				deliverError(call, response, null);
				return;
			}

			JSONObject object;
			try {
				object = decode(response);
			} catch (Exception ex) {
				deliverError(call, response, ex);
				return;
			}
			deliverSuccess(object);
		}

		private void deliverSuccess(final JSONObject object) {
			if (mListener != null) {
				ApiDeliverHandler.getHandler().post(new Runnable() {
					@Override
					public void run() {
						if (mListener != null) {
							mListener.onSuccess(object);
						}
					}
				});
			}
		}

		private void deliverError(Call call, Response response, Exception ex) {
			if (mListener != null) {
				final ApiError error = new ApiError(call, response, ex);
				ApiDeliverHandler.getHandler().post(new Runnable() {
					@Override
					public void run() {
						if (mListener != null) {
							mListener.onError(error);
						}
					}
				});
			}
		}
	}
}
