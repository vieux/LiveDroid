package com.victorvieux.livedroid.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.support.cache.AbstractCache;
import com.github.ignition.support.http.IgnitedHttp;
import com.github.ignition.support.http.IgnitedHttpResponse;
import com.github.ignition.support.http.cache.CachedHttpResponse;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.tools.API_GAMES;
import com.victorvieux.livedroid.tools.LiveDroidFountain;
import com.victorvieux.livedroid.tools.Misc;
import com.victorvieux.livedroid.tools.SSL;

public class SplashActivity extends Activity {
    private IgnitedHttp http;
    List<Integer> ach_ids = Arrays.asList(new Integer[] {	R.drawable.ach1,R.drawable.ach2,R.drawable.ach3,R.drawable.ach0,
    							R.drawable.ach5,R.drawable.ach6,R.drawable.ach7,R.drawable.ach8,
    							R.drawable.ach9,R.drawable.ach10,R.drawable.ach11,R.drawable.ach12,
    							R.drawable.ach13,R.drawable.ach0, R.drawable.ach14,R.drawable.ach15});
    LiveDroidFountain mLiveDroidFountain;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ((TextView)findViewById(R.id.textViewTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "X360.ttf"));
       
        
        mLiveDroidFountain = new LiveDroidFountain(this);
        mLiveDroidFountain.initDrawables(ach_ids, Misc.isTablet(this) ? ach_ids.size() : ach_ids.size() / 2);
      
        http = new IgnitedHttp(this);

        http.enableResponseCache(this, 3, 61, 1,
                AbstractCache.DISK_CACHE_INTERNAL);
        
        http.setHttpClient(SSL.getNewHttpClient());

        String gamertag = PreferenceManager.getDefaultSharedPreferences(this).getString("gamertag", null);
        if (gamertag != null && gamertag.compareTo("") != 0) {
        	((EditText) findViewById(R.id.editTextGamerTag)).setText(gamertag);
        	HttpTask task = new HttpTask(http, gamertag);
        	task.connect(this);
        	task.execute();
        } else {
        	findViewById(R.id.buttonLogin).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String gamerTag = ((EditText) findViewById(R.id.editTextGamerTag)).getEditableText().toString().trim();
					if (gamerTag != null && gamerTag.length() > 1) {
						HttpTask task = new HttpTask(http, gamerTag);
						task.connect(SplashActivity.this);
						task.execute();
					}
				}
			});
        }
    }
    
    public static final class HttpTask extends IgnitedAsyncTask<SplashActivity, Void, Void, IgnitedHttpResponse> {

		private IgnitedHttp http;
		
		private String url = "https://xboxapi.com/json/games/";
		
		public HttpTask(IgnitedHttp http, String gamertag) {
		    this.http = http;
		    this.url += gamertag.replace(" ", "%20");
		}
		
		@Override
		public boolean onTaskStarted(final SplashActivity context) {
        	((ViewSwitcher)context.findViewById(R.id.viewSwitcherLogin)).showNext();
			context.mLiveDroidFountain.animate(true);
		    return true;
		}
		
		@Override
		public IgnitedHttpResponse run(Void... params) throws Exception {
		    return http.get(url, true).retries(3).expecting(200).send();
		}
		
		@Override
		public boolean onTaskFailed(SplashActivity context, Exception error) {
		    super.onTaskFailed(context, error); // prints a stack trace
		    ((ViewSwitcher)context.findViewById(R.id.viewSwitcherLogin)).showPrevious();
		    return true;
		}
		
		@Override
		public boolean onTaskSuccess(SplashActivity context, IgnitedHttpResponse response) {
		    boolean cachedResponse = response instanceof CachedHttpResponse;
		
		    try {
				API_GAMES api_games = new API_GAMES(response.getResponseBodyAsString());
				if (api_games.Success()) {
					Intent intent = new Intent(context, ListActivity.class);
					intent.putExtra("player", api_games.getPlayer());
					context.startActivity(intent);
				} else
		        	((ViewSwitcher)context.findViewById(R.id.viewSwitcherLogin)).showPrevious();

					
			} catch (Exception e) {
	        	((ViewSwitcher)context.findViewById(R.id.viewSwitcherLogin)).showPrevious();
			}
		
		    return true;
		}
	}
}