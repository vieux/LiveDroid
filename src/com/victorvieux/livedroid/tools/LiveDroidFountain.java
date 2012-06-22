package com.victorvieux.livedroid.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.victorvieux.livedroid.R;

public class LiveDroidFountain
{
	private Activity mActivity;
	private Drawable[] mDrawables;
	private List<ImageView> mImages = new ArrayList<ImageView>();
	
	public LiveDroidFountain(Activity activity)
	{
	    this.mActivity = activity;
	}
	  
	private void animate(ImageView paramImageView, boolean infinite)
	{
	    Random localRandom = new Random();
	    float f1 = -45 + localRandom.nextInt(90);
	    float f3 = (float)(Math.PI * (10 + localRandom.nextInt(160)) / 180.0D);
	    int i = 300 * localRandom.nextInt(10);
	    int j = 1500 + localRandom.nextInt(1500);
	    float f2 = FloatMath.cos(f3);
	    f3 = FloatMath.sin(f3);
	    RotateAnimation localRotateAnimation = new RotateAnimation(f1, f1, 1, 0.5F, 1, 0.5F);
	    localRotateAnimation.setDuration(0L);
	    localRotateAnimation.setFillBefore(true);
	    localRotateAnimation.setFillAfter(true);
	    localRotateAnimation.setFillEnabled(true);
	    TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, -400.0F * f2, 0.0F, -400.0F * f3);
	    localTranslateAnimation.setDuration(j);
	    localTranslateAnimation.setStartOffset(i);
	    if (infinite)
	    {
	      localTranslateAnimation.setRepeatMode(-1);
	      localTranslateAnimation.setRepeatCount(1000);
	    }
	    AnimationSet localAnimationSet = new AnimationSet(true);
	    localAnimationSet.addAnimation(localRotateAnimation);
	    localAnimationSet.addAnimation(localTranslateAnimation);
	    paramImageView.startAnimation(localAnimationSet);
	}
	
	public void stop() {
	    RelativeLayout localRelativeLayout = (RelativeLayout)mActivity.findViewById(R.id.Fountain);
	    for (ImageView imageView : mImages) {
	    	imageView.setAnimation(null);
	    	localRelativeLayout.removeView(imageView);
	    }
	}
	
	public void animate(boolean infinite)
	  {
	    ImageView localImageView1 = (ImageView)mActivity.findViewById(R.id.imageViewFountain); 
	    RelativeLayout localRelativeLayout = (RelativeLayout)mActivity.findViewById(R.id.Fountain);
	    for (int i = 0; i < mDrawables.length; i++)
	    {
	      int j = i + 1862803457;
	      ImageView localImageView2 = (ImageView)localRelativeLayout.findViewById(j);
	      if (localImageView2 != null)
	      {
	        j = 0;
	      }
	      else
	      {
	        localImageView2 = new ImageView(this.mActivity);
	        localImageView2.setId(j);
	        j = 1;
	      }
	      localImageView2.setImageDrawable(mDrawables[i]);
	      localImageView2.setLayoutParams(localImageView1.getLayoutParams());
	      if (j != 0) {
	        localRelativeLayout.addView(localImageView2, 0);
	        mImages.add(localImageView2);
	      }
	      animate(localImageView2, infinite);
	    }
	  }
	
	public void initDrawables(List<Integer> ids, int size)
	{
        Collections.shuffle(ids);
	    mDrawables = new Drawable[size];
	    final Resources resources = mActivity.getResources();
	    for (int i = 0 ; i < size; ++i)
	    	mDrawables[i] = resources.getDrawable(ids.get(i)); 
	}
}
