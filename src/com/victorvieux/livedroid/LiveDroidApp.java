package com.victorvieux.livedroid;

import java.util.List;

import android.app.Application;

import com.androidquery.callback.BitmapAjaxCallback;
import com.victorvieux.livedroid.api.data.Game;
import com.victorvieux.livedroid.api.data.Player;
import com.victorvieux.livedroid.api.endpoints.Games;

public class LiveDroidApp extends Application{	
	
    @Override
    public void onLowMemory(){  
    	BitmapAjaxCallback.clearCache();
    }
    
    private Games mRoot;
    
    public List<Game> getGames() {
		return mRoot.Games;
	}
    
    public Player getPlayer() {
    	return mRoot.Player;
    }
    
    public void setRoot(Games root) {
    	mRoot = root;
    }
        
}