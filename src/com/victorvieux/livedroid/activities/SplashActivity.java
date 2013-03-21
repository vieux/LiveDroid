package com.victorvieux.livedroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.api.RestClient;
import com.victorvieux.livedroid.api.endpoints.Games;
import com.victorvieux.livedroid.tools.CachedAsyncHttpResponseHandler;
import com.victorvieux.livedroid.tools.LoadingAnimation;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		((TextView)findViewById(R.id.textViewTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "X360.ttf"));

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
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.editTextGamerTag)).getWindowToken(), 0);
					getGames(gamertag.trim());
				}
			}
		});

	}

	private void getGames(String gamertag) {
		((ViewSwitcher)findViewById(R.id.viewSwitcherLogin)).showNext();
		LoadingAnimation.start(findViewById(R.id.imageViewLogo));
		
		RestClient.getGames(this, new CachedAsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				String cache = getCache();
				if (cache != null) {
					Gson gson = new Gson();
					Games games = gson.fromJson(cache, Games.class);
					if (games != null && games.Success) {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						intent.putExtra("root", games);
						SplashActivity.this.startActivity(intent);
					}
					else
						((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
					LoadingAnimation.stop(findViewById(R.id.imageViewLogo));
				}
			}
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				if (hadCache()) return;
				Gson gson = new Gson();
				Games games = gson.fromJson(response, Games.class);
				if (games != null && games.Success) {
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					intent.putExtra("root", games);
					SplashActivity.this.startActivity(intent);
				}
				else
					((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
				LoadingAnimation.stop(findViewById(R.id.imageViewLogo));
			}

			@Override
			public void onFailure(Throwable error) {
				
				LoadingAnimation.stop(findViewById(R.id.imageViewLogo));
				((ViewSwitcher)SplashActivity.this.findViewById(R.id.viewSwitcherLogin)).showPrevious();
			}

		}, gamertag);



	}



}