package com.victorvieux.livedroid.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Achievement;
import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.tools.API_ACHIEVMENTS.ACH_TYPE;
import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class AchAdapter extends BaseAdapter{
	final List<Achievement> mAchs;
	final List<Achievement> mFilteredAchs;
	final LayoutInflater mLayoutInflater;
	final AQuery aq;
	final Context mContext;
	final String mGameName;
	
	public AchAdapter(Context context, List<Achievement> achs, String gameName) {
		aq = new AQuery(context);
		mAchs = achs;
		mFilteredAchs = new ArrayList<Achievement>();
		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mGameName = gameName;
		filter(ACH_TYPE.ALL);
	}

	public void filter(ACH_TYPE type) {
		mFilteredAchs.clear();
		if (mAchs != null) {
		for (Achievement a : mAchs)
			switch (type) {
			case WON:
				if (a.EarnedOn)
					mFilteredAchs.add(a);
				break;
			case MISSING:
				if (!a.EarnedOn)
					mFilteredAchs.add(a);
				break;
			default:
				mFilteredAchs.add(a);
				break;
		}
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mFilteredAchs.size();
	}

	@Override
	public Object getItem(int position) {
		return mFilteredAchs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFilteredAchs.get(position).ID;
	}
	
	@Override
	public int getItemViewType(int position) {
		return (mFilteredAchs.get(position).EarnedOn ? 0 : 1);
	}
	
	@Override
	public int	getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(getItemViewType(position) == 0 ? R.layout.ach_owned_item : R.layout.ach_item, null);
			vh = new ViewHolder();
			vh.imageViewTile = (ImageView) convertView.findViewById(R.id.imageViewTile);
			vh.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
			vh.textViewScore = (TextView) convertView.findViewById(R.id.textViewScore);
			vh.textViewDesc = (TextView) convertView.findViewById(R.id.textViewDesc);
			vh.imageViewVideo = (ImageView) convertView.findViewById(R.id.imageViewVideo);
			convertView.setTag(vh);
		} else
			vh = (ViewHolder) convertView.getTag();
		
		final Achievement a = mFilteredAchs.get(position);
		
		aq.id(vh.imageViewTile).image(a.TileUrl);
		vh.textViewTitle.setText(a.Name);
		vh.textViewScore.setText(""+a.Score);
		vh.textViewDesc.setText(a.Description);
		vh.imageViewVideo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEARCH);
				intent.setPackage("com.google.android.youtube");
				intent.putExtra("query", mGameName + " " + a.Name);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				
			}
		});

		return convertView;
	}
	
	
	private class ViewHolder
	{
		public ImageView imageViewTile;
		public TextView textViewTitle;
		public TextView textViewScore;
		public TextView textViewDesc;
		public ImageView imageViewVideo;
	}

}
