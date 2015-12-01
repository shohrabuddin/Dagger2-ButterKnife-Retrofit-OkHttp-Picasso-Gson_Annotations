
package com.shohrab.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by shohrab.uddin on 22.11.2015.
 * This class is used to store Timestamp sharedPreferences data. Timestamp is used to take decision whether to call remote API or to take data from local SQLite DB
 */
public class SharedPref {

	public long cached_time_stamp;

    public static SharedPreferences sharedPreferences;
	public static Editor editor;

	public SharedPref(){}

	public void setSharedPref(Context context, String prefName) {
		sharedPreferences = context.getSharedPreferences(prefName,Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public Long getCached_time_stamp() {
		return cached_time_stamp;
	}

	public void setCached_time_stamp(Long cached_time_stamp) {
		this.cached_time_stamp = cached_time_stamp;
        editor.putLong(StaticValue.TAG_TIMESTAMP, cached_time_stamp);
		editor.commit();
	}


	
	
}
