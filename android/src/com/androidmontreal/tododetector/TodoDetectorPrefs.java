package com.androidmontreal.tododetector;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.androidmontreal.tododetector.ui.toaster;

public class TodoDetectorPrefs extends SherlockPreferenceActivity {

	@Override
	protected void onCreate(Bundle parent) {
		super.onCreate(parent);
		addPreferencesFromResource(R.xml.preferences);
		this.setTitle(getString(R.string.app_name)); // Strangely, we need to use "this" and not the SupportActionBar accessor. I wonder why.
		getSupportActionBar().setSubtitle(getString(R.string.strIdPrefsSubTitle));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME );

		getAboutPref().setOnPreferenceClickListener(getAboutClick());
	}

	private OnPreferenceClickListener mAboutClick = null;
	private OnPreferenceClickListener getAboutClick() {
		if(mAboutClick == null) {
			mAboutClick = new OnPreferenceClickListener() {

				public boolean onPreferenceClick(Preference preference) {
					toaster.printMessage(getActivity(), "Will thank people here");
					return true;
				}
			};
		}
		return mAboutClick;
	}

	protected TodoDetectorPrefs getActivity() {
		return this;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		this.finish();
		return true;
	}	
	protected Preference getAboutPref() {
		return (Preference) findPreference(getString(R.string.strAbout));
	}
	
}
