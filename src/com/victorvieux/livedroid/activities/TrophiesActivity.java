package com.victorvieux.livedroid.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.devsmart.android.ui.HorizontalListView;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.adapters.GameListAdapter;
import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class TrophiesActivity extends BaseActivty implements OnItemClickListener {
    private List<Game> completed;
	private List<Game> almostCompleted;
	private List<Game> moreThan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPlayer((Player) getIntent().getExtras().getSerializable("player"));
        setContentView(R.layout.trophies);
        
        completed = new ArrayList<Game>();
        almostCompleted = new ArrayList<Game>();
        moreThan = new ArrayList<Game>();
        
        for (Game g : getPlayer().games) {
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
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_trophies, menu);
        return true;
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