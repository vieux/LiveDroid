package com.victorvieux.livedroid.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.victorvieux.livedroid.R;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		findPreference("license").setOnPreferenceClickListener(this);
		findPreference("xboxapi").setOnPreferenceClickListener(this);
		findPreference("github").setOnPreferenceClickListener(this);
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
