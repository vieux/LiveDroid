package com.victorvieux.livedroid;

import android.app.Application;

import com.androidquery.callback.BitmapAjaxCallback;
import com.victorvieux.livedroid.data.Player;

public class LiveDroidApp extends Application{	
	
    @Override
    public void onLowMemory(){  
    	BitmapAjaxCallback.clearCache();
    }
    
    private Player mPlayer;
    
    public Player getPlayer() {
		return mPlayer;
	}
    
    public void setPlayer(Player player) {
    	mPlayer = player;
    }
        
}