package com.crubio.twitterclient.clients;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class TwitterClient extends OAuthBaseClient {
	private static final String CLASS = TwitterClient.class.getSimpleName();
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "quhmogQ8qMnlwzsd04SrsGY75";       // Change this
	public static final String REST_CONSUMER_SECRET = "jQbVHBgbVnl7B13xpAF1BBChonG4lTuN4tHfHe86kPcuCO8tnT"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		Log.i(CLASS, "statuses/home_timeline.json");
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		Log.i(CLASS, "statuses/update.json");
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	public void getUserTimeline(String user, int page, AsyncHttpResponseHandler handler) {
		Log.i(CLASS, "statuses/user_timeline.json: "+user+" page: "+page);
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", String.valueOf(user));
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	public void getUserStatus(AsyncHttpResponseHandler handle){
		Log.i(CLASS, "statuses/verify_credentials.json");
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, handle);
	}

	public void getMentions(int page, AsyncHttpResponseHandler handler) {
		Log.i(CLASS, "statuses/mentions_timeline.json");
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(String userId, AsyncHttpResponseHandler handler) {
		Log.i(CLASS, "statuses/show.json");
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		getClient().get(apiUrl, params, handler);
	}
}