package com.victorvieux.livedroid.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;

public class AvatarActivity extends Activity implements OnClickListener  {
    public Player mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");
        setContentView(R.layout.avatar);
    	final AQuery aq = new AQuery(this);;
    	aq.id(R.id.imageViewAvatar).image(mPlayer.Avatar_Body.replace(" ","%20"));
       
        findViewById(R.id.buttonWallpaper).setOnClickListener(this);
    }
    
  

	@Override
	public void onClick(View v) {
		new getBackGroundTask().execute((Void)null);
	}
	
	class getBackGroundTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AvatarActivity.this);
			progressDialog.setIndeterminate(true);
			progressDialog.setMessage(getString(R.string.loading));
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				findViewById(R.id.wallpaper).buildDrawingCache();
				WallpaperManager.getInstance(AvatarActivity.this).setBitmap( findViewById(R.id.wallpaper).getDrawingCache());
			} catch (IOException e) {
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
		}
		
	}


}