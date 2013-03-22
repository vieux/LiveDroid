package com.victorvieux.livedroid.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.victorvieux.livedroid.LiveDroidApp;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.api.data.Game;
import com.victorvieux.livedroid.api.data.Game.GAME_TYPE;

public class GameGridAdapter extends BaseAdapter implements OnItemClickListener{
	final List<Game> mGames;
	final List<Game> mFilteredGames;
	final Context mContext;
	final int ht_px;
	final int wt_px;
	final AQuery aq;
	
	public GameGridAdapter(Context context, List<Game> games) {
		aq = new AQuery(context);
		mContext = context;
		mGames = games;
		mFilteredGames = new ArrayList<Game>();
		ht_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics());
		wt_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85, context.getResources().getDisplayMetrics());
		filter();
	}

	public void filter() {
		if (mGames != null) {
		for (Game g : mGames)
			if (g.getType() != GAME_TYPE.APP)
				mFilteredGames.add(g);
		}
	}
	@Override
	public int getCount() {
		return mFilteredGames == null ? 0 : mFilteredGames.size();
	}

	@Override
	public Object getItem(int position) {
		return mFilteredGames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFilteredGames.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
        if (convertView == null) { 
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(wt_px, ht_px));
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setBackgroundResource(R.drawable.box);
		aq.id(imageView).image(mFilteredGames.get(position).BoxArt.Small);
		return imageView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Intent intent = new Intent();
		intent.setClass(mContext, GameActivity.class);
		intent.putExtra("index", pos);
		intent.putExtra("forced", true);
		intent.putExtra("box_small", mFilteredGames.get(pos).BoxArt.Small);
		intent.putExtra("box_large", mFilteredGames.get(pos).BoxArt.Large);
		intent.putExtra("url", mFilteredGames.get(pos).AchievementInfo);
		intent.putExtra("title", mFilteredGames.get(pos).Name);
		intent.putExtra("catalog", mFilteredGames.get(pos).CatalogLink);
		mContext.startActivity(intent);	
	}
}
