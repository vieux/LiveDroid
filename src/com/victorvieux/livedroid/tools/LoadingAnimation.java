package com.victorvieux.livedroid.tools;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class LoadingAnimation {
	static public void start(final View v) {
	
	 	final Animation animation1 = new AlphaAnimation(0.0f, 1.0f);
	    animation1.setDuration(750);
	    animation1.setStartOffset(0);
	    
	    final Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
	    animation2.setDuration(750);
	    animation2.setStartOffset(0);

	    animation1.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {v.startAnimation(animation2);}

	        @Override
	        public void onAnimationRepeat(Animation arg0) {}

	        @Override
	        public void onAnimationStart(Animation arg0) {}

	    });
	    
	    animation2.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {v.startAnimation(animation1);}

	        @Override
	        public void onAnimationRepeat(Animation arg0) {}

	        @Override
	        public void onAnimationStart(Animation arg0) {}

	    });

	    v.startAnimation(animation1);
	}
	
	static public void stop(final View v) {
		v.setAnimation(null);		
	}
}
