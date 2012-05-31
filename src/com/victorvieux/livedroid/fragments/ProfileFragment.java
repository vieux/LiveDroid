package com.victorvieux.livedroid.fragments;

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
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.activities.BaseActivty;
import com.victorvieux.livedroid.activities.GameActivity;
import com.victorvieux.livedroid.adapters.GameListAdapter;
import com.victorvieux.livedroid.data.Game;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.tools.API_GAMES.GAME_TYPE;

public class ProfileFragment extends ListFragment implements OnItemSelectedListener {
    boolean mDualPane;
    int mCurCheckPosition = 0;
    Player mPlayer;
    GameListAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
      
        return inflater.inflate(R.layout.profile, container);
    }
    
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        mPlayer = ((BaseActivty)  getActivity()).getPlayer();
        if (mPlayer != null) {
        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        	Editor e = sp.edit();
        	e.putString("gamertag", mPlayer.Gamertag);
        	e.commit();
        	
        	AQuery aq = new AQuery(getActivity());
        	aq.id(R.id.imageViewProfile).image(mPlayer.Avatar_Gamertile);
	        ((TextView) getView().findViewById(R.id.textViewGamerTag)).setText(mPlayer.Gamertag);
	        ((TextView) getView().findViewById(R.id.textViewScore)).setText(mPlayer.Gamerscore);
	        ((TextView) getView().findViewById(R.id.textViewGames)).setText(""+ mPlayer.games.size());
	        mAdapter = new GameListAdapter(getActivity(), mPlayer.games, GAME_TYPE.ALL, false);
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
                details = GameFragment.newInstance(index, g.AchievementInfo, g.Name, g.BoxArt_Small);

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
            intent.putExtra("box", g.BoxArt_Small);
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
}