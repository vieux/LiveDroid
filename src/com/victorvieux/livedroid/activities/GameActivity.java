package com.victorvieux.livedroid.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.fragments.GameFragment;

public class GameActivity extends SherlockFragmentActivity {

    private MenuItem mMenuRefresh;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int mask = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask >= Configuration.SCREENLAYOUT_SIZE_LARGE && getIntent().getBooleanExtra("forced", false) == false) {
        	finish();
            return;
        }

        if (savedInstanceState == null) {
            GameFragment game = new GameFragment();
            game.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, game).commit();
        }
    }
	
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.refresh, menu);
	    mMenuRefresh = menu.findItem(R.id.itemRefresh);
	    return true;
	}
}