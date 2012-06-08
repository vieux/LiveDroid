package com.victorvieux.livedroid.tools;

import android.content.Context;
import android.content.res.Configuration;

public class Misc {
	
	public static boolean isTablet(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & 
				Configuration.SCREENLAYOUT_SIZE_MASK) == 
			    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			return true;
		}
		return false;
	}
}
