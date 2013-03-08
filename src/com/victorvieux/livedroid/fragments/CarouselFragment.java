package com.victorvieux.livedroid.fragments;

import android.app.Activity;

import com.victorvieux.livedroid.activities.CarouselActivity;
import com.victorvieux.livedroid.fragments.hack.ActivityHostFragment;

public class CarouselFragment extends ActivityHostFragment{
    
    @Override
    protected Class<? extends Activity> getActivityClass() {
        return CarouselActivity.class;
    }
}
