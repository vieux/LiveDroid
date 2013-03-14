package com.victorvieux.livedroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.activities.MainActivity;
import com.victorvieux.livedroid.adapters.GameGridAdapter;
import com.victorvieux.livedroid.data.Player;

public class WallFragment extends SherlockFragment implements OnItemClickListener, OnTapListener{


	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_wall, container, false);

		((GridView) root.findViewById(R.id.gridView)).setEmptyView(root.findViewById(android.R.id.empty));
		((GridView) root.findViewById(R.id.gridView)).setAdapter(new GameGridAdapter(getActivity(), ((MainActivity)getActivity()).getPlayer().games));
		((GridView) root.findViewById(R.id.gridView)).setOnItemClickListener(this);
	
		return root;
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Player p = ((MainActivity)getActivity()).getPlayer();
		Intent intent = new Intent();
		intent.setClass(getActivity(), GameActivity.class);
		intent.putExtra("index", pos);
		intent.putExtra("forced", true);
		intent.putExtra("box", p.games.get(pos).BoxArt_Small);
		intent.putExtra("url", p.games.get(pos).AchievementInfo);
		intent.putExtra("title", p.games.get(pos).Name);
		startActivity(intent);	
	}



	@Override
	public void onTap() {
		try {
			((GridView) getView().findViewById(R.id.gridView)).smoothScrollToPosition(0);
	    } catch (NoSuchMethodError e) {
	    	((GridView) getView().findViewById(R.id.gridView)).setSelection(0);
	    }
	}
}