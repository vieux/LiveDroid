package com.victorvieux.livedroid.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.adapters.GameListAdapter;
import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class TrophiesActivity extends Activity implements OnItemClickListener {
    public Player mPlayer;
    private List<Game> completed;
	private List<Game> almostCompleted;
	private List<Game> moreThan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");
        setContentView(R.layout.trophies);
        
        completed = new ArrayList<Game>();
        almostCompleted = new ArrayList<Game>();
        moreThan = new ArrayList<Game>();
        
        for (Game g : mPlayer.games) {
			int progress = g.Progress_Score * 100 / g.PossibleScore;
			if (progress == 100)
				completed.add(g);
			else if (progress >= 90)
				almostCompleted.add(g);
			else if (progress >= 75)
				moreThan.add(g);
		}
        
        if (completed.size() > 0) {
        	findViewById(R.id.LinearLayoutCompleted).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.textViewCompletedNb)).setText(""+completed.size());
        	((HorizontalListView)findViewById(R.id.horizontalListViewCompleted)).setAdapter(new GameListAdapter(this, completed, GAME_TYPE.ALL, true));
        	((HorizontalListView)findViewById(R.id.horizontalListViewCompleted)).setOnItemClickListener(this);
        }        
        if (almostCompleted.size() > 0) {
        	findViewById(R.id.LinearLayoutAlmostCompleted).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.textViewAlmostCompletedNb)).setText(""+ almostCompleted.size());
        	((HorizontalListView)findViewById(R.id.horizontalListViewAlmostCompleted)).setAdapter(new GameListAdapter(this, almostCompleted, GAME_TYPE.ALL, true));
        	((HorizontalListView)findViewById(R.id.horizontalListViewAlmostCompleted)).setOnItemClickListener(this);
        }        
        if (moreThan.size() > 0) {
        	findViewById(R.id.LinearLayoutMoreThan).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.textViewMoreThanNb)).setText(""+ moreThan.size());
        	((HorizontalListView)findViewById(R.id.horizontalListViewMoreThan)).setAdapter(new GameListAdapter(this, moreThan, GAME_TYPE.ALL, true));
        	((HorizontalListView)findViewById(R.id.horizontalListViewMoreThan)).setOnItemClickListener(this);
        }        
      
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trophies, menu);
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
		
		if (arg0.equals(findViewById(R.id.horizontalListViewCompleted))) {
			intent.putExtra("url", completed.get(pos).AchievementInfo);
			intent.putExtra("title",completed.get(pos).Name);
		}
		if (arg0.equals(findViewById(R.id.horizontalListViewAlmostCompleted))) {
			intent.putExtra("url", almostCompleted.get(pos).AchievementInfo);
			intent.putExtra("title",almostCompleted.get(pos).Name);
		}
		if (arg0.equals(findViewById(R.id.horizontalListViewMoreThan))) {
			intent.putExtra("url", moreThan.get(pos).AchievementInfo);
			intent.putExtra("title",moreThan.get(pos).Name);
		}
		
        startActivity(intent);	

	}
}