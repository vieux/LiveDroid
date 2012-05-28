package com.victorvieux.livedroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.adapters.GameGridAdapter;
import com.victorvieux.livedroid.data.Player;

public class WallActivity extends Activity implements OnItemClickListener {
    public Player mPlayer;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");
        setContentView(R.layout.wall);
        
        ((GridView) findViewById(R.id.gridView)).setAdapter(new GameGridAdapter(this, mPlayer.games));
        ((GridView) findViewById(R.id.gridView)).setOnItemClickListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wall, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.trophies: 
        {
            Intent intent = new Intent(this, TrophiesActivity.class);
            intent.putExtra("player", mPlayer);
            startActivity(intent);
            return true;
        }
            case R.id.list:
            {
            	Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("player", mPlayer);
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		        startActivity(intent);
                return true;
            }
            case R.id.avatar:
            	Intent intent = new Intent(this, AvatarActivity.class);
                intent.putExtra("player", mPlayer);
                startActivity(intent);
                return true;
            case R.id.settings:
            	startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		 Intent intent = new Intent();
         intent.setClass(this, GameActivity.class);
         intent.putExtra("index", pos);
         intent.putExtra("forced", true);
         intent.putExtra("url", mPlayer.games.get(pos).AchievementInfo);
         intent.putExtra("title", mPlayer.games.get(pos).Name);
         startActivity(intent);	
	}
}