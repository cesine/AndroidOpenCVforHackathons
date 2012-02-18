package com.androidmontreal.tododetector.ui;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.androidmontreal.tododetector.db.ImageUploadHistoryDatabase.ImageUploadHistory;
import com.androidmontreal.tododetector.pref.PreferenceConstants;
import com.androidmontreal.tododetector.pref.SetPreferencesActivity;
import com.androidmontreal.tododetector.service.ImageUploadService;

import com.androidmontreal.tododetector.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainPortal extends Activity {

	private static final String EXTRA_WATER_SOURCE_CODE = null;
	private String imageSourceCodeFileName = "";
	private String mImageFileName = "";

	private static final String TAG = "AndroidBacterialCountingMain";
	private String mOutputDir = "";
	private String mSampleCodeCount = "0";
	private String mSampleId = "0";
	private String mExperimenterCode = "AA";
	public static final int WATER_SOURCE = 1;
	private static final int SWITCH_LANGUAGE = 2;
	private Menu mMenu;
	private Uri mUri;
	static int PETRI_IMAGE_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SharedPreferences prefs = getSharedPreferences(
				PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		mOutputDir = prefs.getString(
				PreferenceConstants.OUTPUT_IMAGE_DIRECTORY,
				"/sdcard/ToDos/");
		mSampleId = prefs.getString(
				PreferenceConstants.PREFERENCE_WATER_SAMPLE_ID, "unkown");
		mExperimenterCode = prefs.getString(
				PreferenceConstants.PREFERENCE_EXPERIMENTER_ID, "AA");

		saveStateToPreferences();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences prefens = getSharedPreferences(
				PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		switch (requestCode) {
		case WATER_SOURCE:

			if (mSampleCodeCount == null) {
				mSampleCodeCount = "0";
			}
			Toast.makeText(
					getApplicationContext(),
					"TODO Display water sample code to write on the petri dish.",
					Toast.LENGTH_LONG).show();
			break;
		case SWITCH_LANGUAGE:
			//TODO
			break;
		default:
			break;

		}
	}

	public void onTodoDetectClick(View v) {
		//Use this button temporarily to test the OpenCV activity on your machine:
//		Intent intent = new Intent(this, PetrifilmSnapActivity.class);
//		startActivity(intent);
		
		Intent intent = new Intent(this, PetrifilmSnapActivity.class);
		String guid=UUID.randomUUID().toString();
		File file = new File(getExternalFilesDir(null), "petri_" + guid + ".jpg");
		intent.putExtra("filepath", file.getAbsolutePath());
		intent.putExtra("guid", guid);

		startActivityForResult(intent, PETRI_IMAGE_REQUEST);
	}

	public void onSyncServerClick(View v) {

		Intent intent = new Intent(this, ImageUploadService.class);
		if (mUri != null){
			intent.setData(mUri);
		}
		intent.putExtra(PreferenceConstants.EXTRA_IMAGEFILE_FULL_PATH, mImageFileName);
		startService(intent); 
		//TODO put the logic in this class later, for now this is just so teh server side cand ebug the connection
		//startActivity(new Intent(this, ServerSync.class));
	}

	private void saveStateToPreferences() {
		if (mExperimenterCode != null || mSampleCodeCount != null) {
			mSampleId = mExperimenterCode + mSampleCodeCount;
		} else {
			mSampleId = "unknown";
		}
		SharedPreferences prefs = getSharedPreferences(
				PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PreferenceConstants.PREFERENCE_WATER_SAMPLE_ID,
				mSampleId);
		editor.commit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		saveStateToPreferences();
		super.onDestroy();
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Hold on to this
		mMenu = menu;

		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// For "Title only": Examples of matching an ID with one assigned in
		// the XML
		case R.id.open_settings:

			Intent i = new Intent(getBaseContext(),
					SetPreferencesActivity.class);
			startActivity(i);
			return true;
		case R.id.language_settings:
			Intent inte = new Intent(getBaseContext(),
					SetPreferencesActivity.class);
			startActivityForResult(inte, SWITCH_LANGUAGE);
			return true;
		case R.id.result_folder:
			final boolean fileManagerAvailable = isIntentAvailable(this,
					"org.openintents.action.PICK_FILE");
			if (!fileManagerAvailable) {
				Toast.makeText(
						getApplicationContext(),
						"To open and export recorded files or "
								+ "draft data you can install the OI File Manager, "
								+ "it allows you to browse your SDCARD directly on your mobile device.",
								Toast.LENGTH_LONG).show();
				Intent goToMarket = new Intent(Intent.ACTION_VIEW)
				.setData(Uri
						.parse("market://details?id=org.openintents.filemanager"));
			} else {
				Intent openResults = new Intent(
						"org.openintents.action.PICK_FILE");
				openResults.setData(Uri.parse("file://"
						+ mOutputDir));
				startActivity(openResults);
			}

			break;
		case R.id.issue_tracker:

			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://github.com/borisdb/TodoDetector/issues"));
			startActivity(browserIntent);
			return true;
		default:
			// Do nothing

			break;
		}

		return false;
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

}
