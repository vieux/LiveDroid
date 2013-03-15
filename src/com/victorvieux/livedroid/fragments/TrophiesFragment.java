package com.victorvieux.livedroid.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.devsmart.android.ui.HorizontalListView;
import com.victorvieux.livedroid.LiveDroidApp;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.activities.MainActivity;
import com.victorvieux.livedroid.adapters.GameListAdapter;
import com.victorvieux.livedroid.api.data.Game;
import com.victorvieux.livedroid.api.data.Game.GAME_TYPE;

public class TrophiesFragment extends SherlockFragment implements OnItemClickListener, OnTapListener {
    private List<Game> completed;
	private List<Game> almostCompleted;
	private List<Game> moreThan;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View root = inflater.inflate(R.layout.fragment_trophies, container, false);

        
        completed = new ArrayList<Game>();
        almostCompleted = new ArrayList<Game>();
        moreThan = new ArrayList<Game>();
        List<Game> gs = ((LiveDroidApp)  getActivity().getApplication()).getGames();

        if (gs != null) {
	        for (Game g : gs) {
	        	if (g.getType() == GAME_TYPE.APP) continue;
				int progress = g.Progress.Score * 100 / g.PossibleScore;
				if (progress == 100)
					completed.add(g);
				else if (progress >= 90)
					almostCompleted.add(g);
				else if (progress >= 75)
					moreThan.add(g);
			}
        } else
        	root.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        
        if (completed.size() > 0) {
        	root.findViewById(R.id.LinearLayoutCompleted).setVisibility(View.VISIBLE);
        	((TextView)root.findViewById(R.id.textViewCompletedNb)).setText(""+completed.size());
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewCompleted)).setAdapter(new GameListAdapter(getActivity(), completed, GAME_TYPE.ALL, true));
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewCompleted)).setOnItemClickListener(this);
        }        
        if (almostCompleted.size() > 0) {
        	root.findViewById(R.id.LinearLayoutAlmostCompleted).setVisibility(View.VISIBLE);
        	((TextView)root.findViewById(R.id.textViewAlmostCompletedNb)).setText(""+ almostCompleted.size());
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewAlmostCompleted)).setAdapter(new GameListAdapter(getActivity(), almostCompleted, GAME_TYPE.ALL, true));
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewAlmostCompleted)).setOnItemClickListener(this);
        }        
        if (moreThan.size() > 0) {
        	root.findViewById(R.id.LinearLayoutMoreThan).setVisibility(View.VISIBLE);
        	((TextView)root.findViewById(R.id.textViewMoreThanNb)).setText(""+ moreThan.size());
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewMoreThan)).setAdapter(new GameListAdapter(getActivity(), moreThan, GAME_TYPE.ALL, true));
        	((HorizontalListView)root.findViewById(R.id.horizontalListViewMoreThan)).setOnItemClickListener(this);
        }        
      
        return root;
    }
    
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		 Intent intent = new Intent();
         intent.setClass(getActivity(), GameActivity.class);
         intent.putExtra("index", pos);
         intent.putExtra("forced", true);
		
		if (arg0.equals(getView().findViewById(R.id.horizontalListViewCompleted))) {
			intent.putExtra("url", completed.get(pos).AchievementInfo);
			intent.putExtra("title",completed.get(pos).Name);
			intent.putExtra("box_small",completed.get(pos).BoxArt.Small);
			intent.putExtra("box_large",completed.get(pos).BoxArt.Large);
			intent.putExtra("catalog",completed.get(pos).CatalogLink);
		}
		if (arg0.equals(getView().findViewById(R.id.horizontalListViewAlmostCompleted))) {
			intent.putExtra("url", almostCompleted.get(pos).AchievementInfo);
			intent.putExtra("title",almostCompleted.get(pos).Name);
			intent.putExtra("box_small",almostCompleted.get(pos).BoxArt.Small);
			intent.putExtra("box_large",almostCompleted.get(pos).BoxArt.Large);
			intent.putExtra("catalog",almostCompleted.get(pos).CatalogLink);
		}
		if (arg0.equals(getView().findViewById(R.id.horizontalListViewMoreThan))) {
			intent.putExtra("url", moreThan.get(pos).AchievementInfo);
			intent.putExtra("title",moreThan.get(pos).Name);
			intent.putExtra("box_small",moreThan.get(pos).BoxArt.Small);
			intent.putExtra("box_large",moreThan.get(pos).BoxArt.Large);
			intent.putExtra("catalog",moreThan.get(pos).CatalogLink);
		}
		
        startActivity(intent);	

	}
	
	@Override
	public void onTap() {
		((ScrollView) getView().findViewById(R.id.scrollview)).smoothScrollTo(0, 0);
	}
}