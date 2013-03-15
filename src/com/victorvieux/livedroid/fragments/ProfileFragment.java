package com.victorvieux.livedroid.fragments;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.victorvieux.livedroid.LiveDroidApp;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.activities.MainActivity;
import com.victorvieux.livedroid.adapters.GameListAdapter;
import com.victorvieux.livedroid.api.data.Game;
import com.victorvieux.livedroid.api.data.Game.GAME_TYPE;
import com.victorvieux.livedroid.api.data.Player;

public class ProfileFragment extends ListFragment implements OnItemSelectedListener, OnTapListener{
    boolean mDualPane;
    int mCurCheckPosition = 0;
    GameListAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
      
        return inflater.inflate(R.layout.fragment_profile, null, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        Player p = ((LiveDroidApp)  getActivity().getApplication()).getPlayer();
        List<Game> gs = ((LiveDroidApp)  getActivity().getApplication()).getGames();
        if (p != null) {
        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        	Editor e = sp.edit();
        	e.putString("gamertag", p.Gamertag);
        	e.commit();
        	
        	AQuery aq = new AQuery(getActivity());
        	aq.id(R.id.imageViewProfile).image(p.Avatar.Gamertile.Large);
	        ((TextView) getView().findViewById(R.id.textViewGamerTag)).setText(p.Gamertag);
	        ((TextView) getView().findViewById(R.id.textViewScore)).setText(""+p.Gamerscore);
	        ((TextView) getView().findViewById(R.id.textViewGames)).setText(""+ (gs == null ? "0" : gs.size()));
	        mAdapter = new GameListAdapter(getActivity(), gs, GAME_TYPE.ALL, false);
	        setListAdapter(mAdapter);
	        ((Spinner) getView().findViewById(R.id.spinnerType)).setOnItemSelectedListener(this);
        }

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedState != null) {
            mCurCheckPosition = savedState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        showDetails(pos);
    }

   void showDetails(int index) {
	   if (index >= mAdapter.getCount())
		   return;
        mCurCheckPosition = index;
    	Game g = mAdapter.getItem(index);

        if (mDualPane) {
            // We can display everything in-place with fragments.
            // Have the list highlight this item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is shown, replace if needed.
            GameFragment details = (GameFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = GameFragment.newInstance(index, g.AchievementInfo, g.Name, g.BoxArt.Small, g.BoxArt.Large, g.CatalogLink);

                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft
                        = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), GameActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("url", g.AchievementInfo);
            intent.putExtra("title", g.Name);
            intent.putExtra("box_small", g.BoxArt.Small);
            intent.putExtra("box_large", g.BoxArt.Large);
            intent.putExtra("catalog", g.CatalogLink);
            startActivity(intent);
        }
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 2:
			mAdapter.filter(GAME_TYPE.ARCADE);
			break;
		case 1:
			mAdapter.filter(GAME_TYPE.RETAIL);
			break;
		default:
			mAdapter.filter(GAME_TYPE.ALL);
			break;
		}
		setSelection(0);
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	@Override
	public void onTap() {
		try {
			getListView().smoothScrollToPosition(0);
	    } catch (NoSuchMethodError e) {
	    	getListView().setSelectionAfterHeaderView();
	    }
	}
}