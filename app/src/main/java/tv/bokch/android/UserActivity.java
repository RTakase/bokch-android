package tv.bokch.android;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import info.hoang8f.widget.FButton;
import timber.log.Timber;
import tv.bokch.App;
import tv.bokch.R;
import tv.bokch.data.History;
import tv.bokch.data.MyUser;
import tv.bokch.data.Relation;
import tv.bokch.data.Stack;
import tv.bokch.data.User;
import tv.bokch.util.ApiRequest;
import tv.bokch.util.ViewUtils;
import tv.bokch.widget.FollowButton;
import tv.bokch.widget.RelationButton;
import tv.bokch.widget.UserView;

public class UserActivity extends TabActivity {
	public static final int INDEX_HISTORY = 0;
	public static final int INDEX_STACK = 1;

	private User mUser;
	private String mBookIdToOpen;
	private Long mFollowId;
	private User mLoginUser;
	private FollowButton mFollowButton;
	private RelationButton mFollowerButton;
	private RelationButton mFolloweeButton;

	private ArrayList<User> mFollowers;
	private ArrayList<User> mFollowees;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setContentView(R.layout.activity_user);
		super.onCreate(savedInstanceState);

		setActionBarTitle(getString(R.string.activity_user));

		Intent intent = getIntent();
		mUser = intent.getParcelableExtra("data");
		if (mUser == null) {
			finish();
			return;
		}

		String bookId = intent.getStringExtra("with_my_review");
		if (!TextUtils.isEmpty(bookId)) {
			mBookIdToOpen = bookId;
		}
		mLoginUser = ((App)getApplicationContext()).getMyUser();

		String _followId = intent.getStringExtra("follow_id");
		if (_followId != null) {
			mFollowId = Long.parseLong(_followId);
		}

		mFollowees = new ArrayList<>();
		mFollowers = new ArrayList<>();

		initialize();
	}

	private void initialize() {
		UserView userView = (UserView)findViewById(R.id.user);
		userView.bindView(mUser);

		mFollowButton = (FollowButton)findViewById(R.id.follow_btn);
		mFollowButton.setClickListener(mFollowClickListener);

		mFollowerButton = (RelationButton)findViewById(R.id.follower_btn);
		mFollowerButton.setClickListener(mRelationClickListener);

		mFolloweeButton = (RelationButton)findViewById(R.id.followee_btn);
		mFolloweeButton.setClickListener(mRelationClickListener);
		
		setFollowButtonState();
		setRelationsButtonState();
	}

	@Override
	protected BaseFragment createFragment(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return BookRecentFragment.newInstance();
		case INDEX_STACK:
			return StackFragment.newInstance();
		default:
			return null;
		}
	}

	@Override
	protected int getTabCount() {
		return 2;
	}

	@Override
	protected String getTabTitle(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return getString(R.string.title_histories);
		case INDEX_STACK:
			return getString(R.string.title_stacks);
		default:
			return null;
		}
	}

	@Override
	protected boolean requestData(int index, TabApiListener listener) {
		ApiRequest request = new ApiRequest();
		switch (index) {
		case INDEX_HISTORY:
			request.recent(null, mUser.userId, null, listener);
			return true;
		case INDEX_STACK:
			request.stack(null, mUser.userId, null, listener);
			return true;
		default:
			return false;
		}
	}

	@Override
	protected String getKey(int index) {
		switch (index) {
		case INDEX_HISTORY:
			return "histories";
		case INDEX_STACK:
			return "stacks";
		default:
			return null;
		}
	}

	@Override
	protected ArrayList<?> getData(int index, JSONArray array) throws JSONException {
		switch (index) {
		case INDEX_HISTORY:
			ArrayList<History> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					History history = new History(obj);
					if (history.review != null && !TextUtils.isEmpty(history.book.title)) {
						history.user = mUser;
						histories.add(history);

						//指定されていたレビューを開く
						if (TextUtils.equals(history.book.bookId, mBookIdToOpen)) {
							startReviewActivity(history);
							mBookIdToOpen = null;
						}
					}
				}
			}
			Collections.reverse(histories);
			return histories;
		case INDEX_STACK:
			ArrayList<Stack> stacks = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.optJSONObject(i);
				if (obj != null) {
					Stack stack = new Stack(obj);
					stacks.add(stack);
				}
			}
			return stacks;
		default:
			return null;
		}
	}

	private void setRelationsButtonState() {
		mFolloweeButton.setState(RelationButton.State.LOADING);
		mFollowerButton.setState(RelationButton.State.LOADING);
		ApiRequest request = new ApiRequest();
		request.relation(mUser.userId, new ApiRequest.ApiListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					JSONArray array = response.optJSONArray("relation");
					for (int i = 0; i < array.length(); i++) {
						Relation relation = new Relation(array.optJSONObject(i));
						//本当は逆！
						if (TextUtils.equals(mUser.userId, relation.follower.userId)) {
							mFollowers.add(relation.followee);
						} else if(TextUtils.equals(mUser.userId, relation.followee.userId)) {
							mFollowees.add(relation.follower);
						}
					}
					mFolloweeButton.setState(RelationButton.State.FOLLOWEES, mFollowees.size());
					mFollowerButton.setState(RelationButton.State.FOLLOWERS, mFollowers.size());
				} catch (JSONException e) {
					Timber.w(e, null);
				}
			}

			@Override
			public void onError(ApiRequest.ApiError error) {
				Toast.makeText(UserActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
				Timber.w(error, null);
			}
		});
	}

	private void setFollowButtonState() {
		if (TextUtils.equals(mLoginUser.userId, mUser.userId)) {
			//自分のページ
			mFollowButton.setState(FollowButton.State.MINE);
		} else if (mFollowId == null) {
			mFollowButton.setState(FollowButton.State.LOADING);
			//フォロー状態を確認する
			getMyUserStatus(mUser.userId, new ApiRequest.ApiListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						MyUser myUser = new MyUser(response);
						mFollowId = myUser.followId;
						if (mFollowId > 0) {
							//フォロー中なのでボタンはフォロー解除状態
							mFollowButton.setState(FollowButton.State.UNFOLLOW);
						} else {
							//フォローしていないのでボタンはフォロー登録状態
							mFollowButton.setState(FollowButton.State.FOLLOW);
						}
					} catch (JSONException e) {
						Timber.w(e, null);
					}
				}

				@Override
				public void onError(ApiRequest.ApiError error) {
					Toast.makeText(UserActivity.this, getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
					Timber.w(error, null);
				}
			});
		} else {
			if (mFollowId > 0) {
				//フォロー中
				mFollowButton.setState(FollowButton.State.UNFOLLOW);
			} else {
				//未フォロー
				mFollowButton.setState(FollowButton.State.FOLLOW);
			}
		}
	}

	private FollowButton.ClickListener mFollowClickListener = new FollowButton.ClickListener() {
		@Override
		public void onClick(FollowButton.State state) {
			try {
				ApiRequest request;
				switch (state) {
				case UNFOLLOW:
					//フォロー解除状態のボタンが押された
					request = new ApiRequest();
					showSpinner();
					request.unfollow(mUser.userId, mLoginUser.userId, mUnfollowApiListener);
					break;
				case FOLLOW:
					//フォロー登録状態のボタンが押された
					request = new ApiRequest();
					showSpinner();
					request.follow(mUser.userId, mLoginUser.userId, mFollowApiListener);
					break;
				}
			} catch (JSONException e) {
				Timber.w(e, null);
			}
		}
	};

	private RelationButton.ClickListener mRelationClickListener = new RelationButton.ClickListener() {
		@Override
		public void onClick(RelationButton.State state) {
			switch(state) {
			case FOLLOWEES:
				startUserListActivity(mFollowees);
				break;
			case FOLLOWERS:
				startUserListActivity(mFollowers);
				break;
			}
		}
	};

	private ApiRequest.ApiListener<JSONObject> mFollowApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			//フォロー登録に成功したのでボタンはフォロー解除状態にする
			mFollowButton.setState(FollowButton.State.UNFOLLOW);
			ViewUtils.showSuccessToast(UserActivity.this, R.string.succeed_follow);
		}

		@Override
		public void onError(ApiRequest.ApiError error) {
			dismissSpinner();
			Timber.w(error, null);
			ViewUtils.showErrorToast(UserActivity.this, R.string.failed_load);
		}
	};

	private ApiRequest.ApiListener<JSONObject> mUnfollowApiListener = new ApiRequest.ApiListener<JSONObject>() {
		@Override
		public void onSuccess(JSONObject response) {
			dismissSpinner();
			//フォロー解除に成功したのでボタンはフォロー登録状態にする
			mFollowButton.setState(FollowButton.State.FOLLOW);
			ViewUtils.showSuccessToast(UserActivity.this, R.string.succeed_unfollow);
		}

		@Override
		public void onError(ApiRequest.ApiError error) {
			dismissSpinner();
			Timber.w(error, null);
			ViewUtils.showErrorToast(UserActivity.this, R.string.failed_load);
		}
	};
}
