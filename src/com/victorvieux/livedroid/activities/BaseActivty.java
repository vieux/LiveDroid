package com.victorvieux.livedroid.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;

public abstract class BaseActivty extends FragmentActivity{
    private Player mPlayer;

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wall: 
            {
                Intent intent = new Intent(this, WallActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            }
            case R.id.list: 
            {
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            }
            case R.id.trophies: 
            {
                Intent intent = new Intent(this, TrophiesActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            }
            case R.id.carousel: 
            {
                Intent intent = new Intent(this, CarouselActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            }
            case R.id.avatar:
            	Intent intent = new Intent(this, AvatarActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            case R.id.settings:
            	startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	public Player getPlayer() {
		return mPlayer;
	}

	public void setPlayer(Player mPlayer) {
		this.mPlayer = mPlayer;
	}
}
