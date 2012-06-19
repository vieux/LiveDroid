package com.victorvieux.livedroid;

import android.app.Application;

import com.androidquery.callback.BitmapAjaxCallback;

public class LiveDroidApp extends Application{	
    @Override
    public void onLowMemory(){  
    	BitmapAjaxCallback.clearCache();
    }
        
}