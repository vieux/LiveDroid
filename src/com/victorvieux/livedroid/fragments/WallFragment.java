package com.victorvieux.livedroid.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.victorvieux.livedroid.LiveDroidApp;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.adapters.GameGridAdapter;
import com.victorvieux.livedroid.api.data.Game;

public class WallFragment extends SherlockFragment implements OnTapListener{


	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_wall, container, false);

		((GridView) root.findViewById(R.id.gridView)).setEmptyView(root.findViewById(android.R.id.empty));
		GameGridAdapter adapter = new GameGridAdapter(getActivity(), ((LiveDroidApp)  getActivity().getApplication()).getGames());
		((GridView) root.findViewById(R.id.gridView)).setAdapter(adapter);
		((GridView) root.findViewById(R.id.gridView)).setOnItemClickListener(adapter);
	
		return root;
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