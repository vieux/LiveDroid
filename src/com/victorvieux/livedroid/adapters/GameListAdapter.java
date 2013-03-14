package com.victorvieux.livedroid.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class GameListAdapter extends BaseAdapter {
	final List<Game> mGames;
	final List<Game> mFilteredGames;
	final LayoutInflater mLayoutInflater;
	final AQuery aq;
	final boolean mSmall;
	
	public GameListAdapter(Context context, List<Game> games, GAME_TYPE type, boolean small) {
		aq = new AQuery(context);
		mGames = games;
		mFilteredGames = new ArrayList<Game>();
		mLayoutInflater = LayoutInflater.from(context);
		mSmall = small;
		filter(type);
	}
	
	public void filter(GAME_TYPE type) {
		mFilteredGames.clear();
		if (mGames != null) {
		for (Game g : mGames)
			switch (type) {
			case ARCADE:
				if (g.GameType == GAME_TYPE.ARCADE)
					mFilteredGames.add(g);
				break;
			case RETAIL:
				if (g.GameType == GAME_TYPE.RETAIL)
					mFilteredGames.add(g);
				break;
			default:
				mFilteredGames.add(g);
				break;
		}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFilteredGames.size();
	}

	@Override
	public Game getItem(int position) {
		return mFilteredGames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFilteredGames.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(mSmall ? R.layout.item_game_trophy : R.layout.item_game, null);
			vh = new ViewHolder();
			vh.imageViewBox = (ImageView) convertView.findViewById(R.id.imageViewBox);
			vh.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
			vh.textViewScore = (TextView) convertView.findViewById(R.id.textViewScore);
			vh.textViewAch = (TextView) convertView.findViewById(R.id.textViewAch);
			vh.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			convertView.setTag(vh);
		} else
			vh = (ViewHolder) convertView.getTag();
		
		Game g = mFilteredGames.get(position);
		aq.id(vh.imageViewBox).image(g.BoxArt_Small);
		vh.textViewTitle.setText(g.Name);
		vh.textViewScore.setText(g.Progress_Score + " / " + g.PossibleScore);
		vh.textViewAch.setText(g.Progress_Achievements + " / " + g.PossibleAchievements);
		
		vh.progressBar.setProgress(g.Progress_Score * 100 / g.PossibleScore);

		return convertView;
	}
	
	
	private class ViewHolder
	{
		public ImageView imageViewBox;
		public TextView textViewTitle;
		public TextView textViewScore;
		public TextView textViewAch;
		public ProgressBar progressBar;
	}

}
