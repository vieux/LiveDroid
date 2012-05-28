package com.victorvieux.livedroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;

public class ListActivity extends FragmentActivity {
    public Player mPlayer;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");
        setContentView(R.layout.list);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wall: 
            {
                Intent intent = new Intent(this, WallActivity.class);
                intent.putExtra("player", mPlayer);
                startActivity(intent);
                return true;
            }
            case R.id.trophies: 
            {
                Intent intent = new Intent(this, TrophiesActivity.class);
                intent.putExtra("player", mPlayer);
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
}