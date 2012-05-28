package com.victorvieux.livedroid.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.victorvieux.livedroid.fragments.GameFragment;

public class GameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int mask = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask >= Configuration.SCREENLAYOUT_SIZE_LARGE && getIntent().getBooleanExtra("forced", false) == false) {
        	finish();
            return;
        }

        if (savedInstanceState == null) {
            GameFragment game = new GameFragment();
            game.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, game).commit();
        }
    }
}