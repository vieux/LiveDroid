package com.victorvieux.livedroid.tools;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Misc {
	
	public static boolean isTablet(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & 
				Configuration.SCREENLAYOUT_SIZE_MASK) == 
			    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			return true;
		}
		return false;
	}
	
	public static Bitmap loadBitmap(String url) {
	    Bitmap ret = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            ret = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return ret;
	}
}
