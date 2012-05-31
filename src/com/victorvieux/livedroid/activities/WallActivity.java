package com.victorvieux.livedroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.adapters.GameGridAdapter;
import com.victorvieux.livedroid.data.Player;

public class WallActivity extends BaseActivty implements OnItemClickListener {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPlayer((Player) getIntent().getExtras().getSerializable("player"));
        setContentView(R.layout.wall);
        
        ((GridView) findViewById(R.id.gridView)).setAdapter(new GameGridAdapter(this, getPlayer().games));
        ((GridView) findViewById(R.id.gridView)).setOnItemClickListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wall, menu);
        return true;
    }
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		 Intent intent = new Intent();
         intent.setClass(this, GameActivity.class);
         intent.putExtra("index", pos);
         intent.putExtra("forced", true);
         intent.putExtra("url", getPlayer().games.get(pos).AchievementInfo);
         intent.putExtra("title", getPlayer().games.get(pos).Name);
         startActivity(intent);	
	}
}