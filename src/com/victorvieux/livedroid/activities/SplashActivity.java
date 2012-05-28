package com.victorvieux.livedroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.support.cache.AbstractCache;
import com.github.ignition.support.http.IgnitedHttp;
import com.github.ignition.support.http.IgnitedHttpResponse;
import com.github.ignition.support.http.cache.CachedHttpResponse;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.tools.API_GAMES;
import com.victorvieux.livedroid.tools.SSL;

public class SplashActivity extends Activity {
    private IgnitedHttp http;
    AlertDialog alert;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ((TextView)findViewById(R.id.textViewTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "X360.ttf"));
       
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
    	final View layout = inflater.inflate(R.layout.dialog,
    	                               (ViewGroup) findViewById(R.id.layout_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(R.string.enter)
    		   .setView(layout)
    	       .setCancelable(false)
    	       .setPositiveButton(R.string.connection, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	dialog.dismiss();
    	        	HttpTask task = new HttpTask(http, ((EditText) layout.findViewById(R.id.editTextGamerTag)).getEditableText().toString());
    	           	task.connect(SplashActivity.this);
    	           	task.execute();
    	           }
    	       })
    	       .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   SplashActivity.this.finish();
    	           }
    	       });
    	alert = builder.create();
    	
        http = new IgnitedHttp(this);

        http.enableResponseCache(this, 3, 61, 1,
                AbstractCache.DISK_CACHE_INTERNAL);
        
        http.setHttpClient(SSL.getNewHttpClient());

        String gamertag = PreferenceManager.getDefaultSharedPreferences(this).getString("gamertag", null);
        if (gamertag != null) {
        	HttpTask task = new HttpTask(http, gamertag);
        	task.connect(this);
        	task.execute();
        	((EditText) layout.findViewById(R.id.editTextGamerTag)).setText(gamertag);
        } else
        {
        	
        	
        	alert.show();
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
			ImageView introanim = (ImageView) context.findViewById(R.id.imageViewLogo);
	        introanim.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading));

	        final AnimationSet set = new AnimationSet(false);
	        Animation animation = new TranslateAnimation(
	                Animation.RELATIVE_TO_PARENT, 0.5f,Animation.RELATIVE_TO_PARENT, 0.1f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f
	            );
	            animation.setDuration(1000);
	            animation.setInterpolator(new DecelerateInterpolator());
	            set.addAnimation(animation);
	            
	            
	            animation = new TranslateAnimation(
	                    Animation.RELATIVE_TO_PARENT, 0.1f,Animation.RELATIVE_TO_PARENT, -0.1f,
	                    Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f
	                );
	                animation.setDuration(500);
	                animation.setInterpolator(new LinearInterpolator());
	                animation.setStartOffset(1000);
	                set.addAnimation(animation);
	            
	                animation = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, -0.1f,Animation.RELATIVE_TO_PARENT, -1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f
	                    );
	                    animation.setDuration(1000);
	                    animation.setInterpolator(new AccelerateInterpolator());
	                    animation.setStartOffset(1500);
	                    animation.setAnimationListener(new AnimationListener() {

	                        @Override
	                        public void onAnimationEnd(Animation arg0) {
	                        	context.findViewById(R.id.imageViewDot).startAnimation(set);
	                        }

	                        @Override
	                        public void onAnimationRepeat(Animation arg0) {
	                            // TODO Auto-generated method stub

	                        }

	                        @Override
	                        public void onAnimationStart(Animation arg0) {
	                            // TODO Auto-generated method stub

	                        }

	                    });
	                    set.addAnimation(animation);
	                    
	                    context.findViewById(R.id.imageViewDot).startAnimation(set);
		    return true;
		}
		
		@Override
		public IgnitedHttpResponse run(Void... params) throws Exception {
		    return http.get(url, true).retries(3).expecting(200).send();
		}
		
		@Override
		public boolean onTaskFailed(SplashActivity context, Exception error) {
		    super.onTaskFailed(context, error); // prints a stack trace
				
		    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
		        	context.alert.show();
					
			} catch (Exception e) {
	        	context.alert.show();
			}
		
		    return true;
		}
	}
}