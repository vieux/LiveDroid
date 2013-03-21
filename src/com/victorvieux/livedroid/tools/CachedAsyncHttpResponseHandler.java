package com.victorvieux.livedroid.tools;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class CachedAsyncHttpResponseHandler extends AsyncHttpResponseHandler{
	private Context mContext = null;
	private String mUrl = null;
	private boolean mHadCache = false;
	
	final static long CACHE_PERIOD = 1000 * 60 * 60 * 1; //1 hour
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public void setUrl(String url) {
		mUrl = url;
	}
	
	protected boolean hadCache() {
		return mHadCache;
	}
	
	protected String getCache() {
		String cache = null;
		if (mContext != null && mUrl != null && PreferenceManager.getDefaultSharedPreferences(mContext).getLong(mUrl + "_millitimestamp", 0) + CACHE_PERIOD > System.currentTimeMillis()) {
			cache = PreferenceManager.getDefaultSharedPreferences(mContext).getString(mUrl, null);
			if (cache != null) mHadCache = true;
		}
		return cache;
	}
	
	public void onSuccess(String content) {
		if (mContext == null || mUrl == null) return;
		Editor e = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		e.putString(mUrl, content);
		e.putLong(mUrl + "_millitimestamp", System.currentTimeMillis());
		e.commit();
	}
}
