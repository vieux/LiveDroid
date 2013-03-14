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

import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.api.RestClient;
import com.victorvieux.livedroid.tools.API_GAMES;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;
import com.victorvieux.livedroid.tools.LiveDroidFountain;
import com.victorvieux.livedroid.tools.Misc;

public class SplashActivity extends Activity {
	List<Integer> ach_ids = Arrays.asList(new Integer[] {	R.drawable.ach1,R.drawable.ach2,R.drawable.ach3,R.drawable.ach0,
			R.drawable.ach5,R.drawable.ach6,R.drawable.ach7,R.drawable.ach8,
			R.drawable.ach9,R.drawable.ach10,R.drawable.ach11,R.drawable.ach12,
			R.drawable.ach13,R.drawable.ach0, R.drawable.ach14,R.drawable.ach15});
	LiveDroidFountain mLiveDroidFountain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		((TextView)findViewById(R.id.textViewTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "X360.ttf"));


		mLiveDroidFountain = new LiveDroidFountain(this);
		mLiveDroidFountain.initDrawables(ach_ids, Misc.isTablet(this) ? ach_ids.size() : ach_ids.size() / 2);

		String gamertag = PreferenceManager.getDefaultSharedPreferences(this).getString("gamertag", null);
		if (gamertag != null && gamertag.trim().compareTo("") != 0) {
			((EditText) findViewById(R.id.editTextGamerTag)).setText(gamertag.trim());
			getGames(gamertag.trim());
		}
		findViewById(R.id.buttonLogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String gamertag = ((EditText) findViewById(R.id.editTextGamerTag)).getEditableText().toString().trim();
				if (gamertag != null && gamertag.length() > 1) {
					getGames(gamertag.trim());
				}
			}
		});

	}

	private void getGames(String gamertag) {
		((ViewSwitcher)findViewById(R.id.viewSwitcherLogin)).showNext();
		mLiveDroidFountain.animate(true);
		RestClient.getGames(this, new CachedAsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				String cache = getCache();
				if (cache != null) {
					try {
						API_GAMES api_games = new API_GAMES(cache);
						if (api_games.Success()) {
							Intent intent = new Intent(SplashActivity.this, MainActivity.class);
							intent.putExtra("player", api_games.getPlayer());
							SplashActivity.this.startActivity(intent);
						} else
							((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();


					} catch (Exception e) {
						((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
					}
					SplashActivity.this.mLiveDroidFountain.stop();
				}
			}
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (hadCache()) return;
				try {
					API_GAMES api_games = new API_GAMES(response);
					PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit().putString("API_LIMIT", api_games.getApiLimit()).commit();
					if (api_games.Success()) {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						intent.putExtra("player", api_games.getPlayer());
						SplashActivity.this.startActivity(intent);
					} else
						((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();


				} catch (Exception e) {
					((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
				}
				SplashActivity.this.mLiveDroidFountain.stop();
			}

			@Override
			public void onFailure(Throwable error) {
				SplashActivity.this.mLiveDroidFountain.stop();
				((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
			}

		}, gamertag);



	}



}