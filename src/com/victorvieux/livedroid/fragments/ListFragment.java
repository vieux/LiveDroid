package com.victorvieux.livedroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.victorvieux.livedroid.R;

public class ListFragment extends SherlockFragment implements OnTapListener {
	
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.list, container, false);
    }

	@Override
	public void onTap() {		
	}
 
}