package com.victorvieux.livedroid.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.victorvieux.livedroid.R;

public class ListFragment extends SherlockFragment implements OnTapListener {
	
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	  FragmentTransaction ft
    	                     = getFragmentManager().beginTransaction();
    	                  ft.replace(R.id.fragment_list, new ProfileFragment());
    	                  ft.setTransition(
    	                          FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	                  ft.commit();
    	return inflater.inflate(R.layout.fragment_list, container, false);
    }

	@Override
	public void onTap() {		
	}
 
}