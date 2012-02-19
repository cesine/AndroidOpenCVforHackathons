package com.androidmontreal.tododetector.ui;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.androidmontreal.tododetector.json.datatype.Elements;
import com.androidmontreal.tododetector.json.DataExtractor;
import com.androidmontreal.tododetector.network.NetworkChatter;
import com.androidmontreal.tododetector.network.interfaces.IAllListsResponse;
import com.androidmontreal.tododetector.pref.PreferenceConstants;
import com.androidmontreal.tododetector.service.ImageUploadService;
import com.androidmontreal.tododetector.ui.list.ViewLists;
import com.androidmontreal.tododetector.R;
import com.androidmontreal.tododetector.TodoDetectorPrefs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.view.View;
import android.widget.Toast;

public class MainPortal extends SherlockActivity  {

	/*******************************************************
	 *                                                     *
	 *                   Class Variables                   *
	 *                                                     *
	 *******************************************************/
	
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
	

	/*******************************************************
	 *                                                     *
	 *                  Application Code                   *
	 *                                                     *
	 *******************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem prefsMenu = menu.add("Preferences");
		prefsMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		prefsMenu.setIcon(R.drawable.ic_prefs_enabled_scaled);
		prefsMenu.setOnMenuItemClickListener(getPrefsInvokeClickListener());
		

		 return super.onCreateOptionsMenu(menu);
	}	
	
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
	
	public void onListProtoClick(View v) {
		toaster.printMessage(this, "listproto click");

        Intent intent = new Intent(getActivity(), ViewLists.class);
        startActivity(intent);
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

		saveStateToPreferences();
		super.onDestroy();
	}
	
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	
	/*******************************************************
	 *                                                     *
	 *                  UI Event Handlers                  *
	 *                                                     *
	 *******************************************************/
	private OnMenuItemClickListener mPrefsInvokeClickListener = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {
			Intent i = new Intent(getActivity(), TodoDetectorPrefs.class);
			startActivity(i);
			return false;
		}
	};
	private OnMenuItemClickListener getPrefsInvokeClickListener() {
		return mPrefsInvokeClickListener;
	}

	/*******************************************************
	 *                                                     *
	 *                   Utility Methods                   *
	 *                                                     *
	 *******************************************************/
	private MainPortal getActivity() {return this;}
	private String getValueFromPreference(String pKey) {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(pKey, "");
	}



}
