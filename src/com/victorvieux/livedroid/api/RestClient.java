package com.victorvieux.livedroid.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;

public class RestClient {
	public static final String BASE_URL = "https://xboxapi.com/json/";
	private static AsyncHttpClient client = new AsyncHttpClient();

	static public void getGames(Context context, CachedAsyncHttpResponseHandler responseHandler, String gamertag) {
		responseHandler.setContext(context);
		responseHandler.setUrl(BASE_URL + "games/" + gamertag.replace(" ", "%20"));
		client.get(context, BASE_URL + "games/" + gamertag.replace(" ", "%20"), null, responseHandler);
	}

	static public void get(Context context, CachedAsyncHttpResponseHandler responseHandler, String url) {
		responseHandler.setContext(context);
		responseHandler.setUrl(url);
		client.get(context, url, null, responseHandler);
	}
}
