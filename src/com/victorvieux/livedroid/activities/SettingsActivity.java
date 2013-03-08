package com.victorvieux.livedroid.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.victorvieux.livedroid.R;

public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.settings);
		((EditTextPreference) findPreference("gamertag")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Toast.makeText(getApplicationContext(), R.string.restart, Toast.LENGTH_SHORT).show();
				 Intent i = new Intent(getApplicationContext(), SplashActivity.class);
				    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    getApplicationContext().startActivity(i);
				return true;
			}
		});
		
		
		findPreference("license").setOnPreferenceClickListener(this);
		findPreference("xboxapi").setOnPreferenceClickListener(this);
		findPreference("github").setOnPreferenceClickListener(this);
		findPreference("api_limit").setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("API_LIMIT", ""));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("license")) {
			String license = null;
			try {
				InputStream input = getAssets().open("LICENSE");
				int size = input.available();
				byte[] buffer = new byte[size];
	            input.read(buffer);
	            input.close();
	            license = new String(buffer);
			} catch (UnsupportedEncodingException e) {
			} catch (IOException e) {
			}
			AlertDialog.Builder b = new Builder(this);
			b.setMessage(license)
			.setCancelable(true)
			.setPositiveButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			b.create().show();
			return true;
		} else if (preference.getKey().equals("xboxapi")) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xboxapi.com")));
		} else if (preference.getKey().equals("github")) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vieux/LiveDroid")));
		}
		return false;
	}
}
