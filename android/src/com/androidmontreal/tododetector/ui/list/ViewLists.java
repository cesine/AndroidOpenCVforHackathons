package com.androidmontreal.tododetector.ui.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.androidmontreal.tododetector.R;
import com.androidmontreal.tododetector.TodoDetectorPrefs;
import com.androidmontreal.tododetector.json.DataExtractor;
import com.androidmontreal.tododetector.json.datatype.*;
import com.androidmontreal.tododetector.network.NetworkChatter;
import com.androidmontreal.tododetector.network.interfaces.INetworkResponse;
import com.androidmontreal.tododetector.ui.MainPortal;
import com.androidmontreal.tododetector.ui.toaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ViewLists extends SherlockListActivity implements INetworkResponse {

	/*******************************************************
	 *                                                     *
	 *                   Class Variables                   *
	 *                                                     *
	 *******************************************************/
	// Inter-thread Message Handler
	private final Handler mMessageChannel = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setSubtitle(getString(R.string.actListSubtitle));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME );

		requestDataSync();

	}

	private ListAdapter convertToListAdapter(List<OneList> communicatedObject) {		
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapElement = null;
		for (OneList i : communicatedObject) {
			mapElement = new HashMap<String, Object>();
			mapElement.put("title", i.getName());
			mapElement.put("value", Long.valueOf(i.getId()));
			mapList.add(mapElement);
		}
		SimpleAdapter returnValue = new SimpleAdapter(
				this, mapList, android.R.layout.simple_list_item_1,
				new String[] { "title" },
				new int[] { android.R.id.text1 });
		return returnValue;
	}

	private List<OneList> getListFromServer() {
		// TODO Auto-generated method stub
		List<OneList> listOfLists = new ArrayList<OneList>();
		OneList lFakeTodo = new OneList();
		lFakeTodo.setId(5);
		lFakeTodo.setName("Faaaaake");
		listOfLists.add(lFakeTodo);
		return listOfLists;
	}
	@Override
	@SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

		toaster.printMessage(this, "Long value to text: " + ((Long)map.get("value")).toString());

		Intent i = new Intent(getActivity(), ViewOneList.class);
		i.putExtra("listId", ((Long)map.get("value")).longValue()); 
		startActivity(i);
	}

	// Exit from Menubar
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		this.finish();
		return true;
	}




	/** IMPORTED CODE **/

	/*******************************************************
	 *                                                     *
	 *                   Data Requesting                   *
	 *                                                     *
	 *******************************************************/
	// JSON Data decryption happens in a network-approach-coded library (from google)
	// basically, we need to process that data in another thread and keep any laggy
	// stuff from the UI thread.
	private void requestDataSync() {
		toaster.printMessage(this, "attempting datasync");
		String lBaseURL = getValueFromPreference(getString(R.string.strServerBaseURL));
		if(lBaseURL.equalsIgnoreCase("")){
			toaster.printMessage(this, "You have not configured the base URL correctly. RTFM or GTFO");
			//return;
		}
		NetworkChatter.getAllTheLists(lBaseURL, this);
	}
	private void processResponseInThread(HttpResponse response){

		final HttpResponse argument = response;

		new Thread(new Runnable() {
			public void run() {
				JsonObject lNetResponse = null;
				AllTheLists returnData = null;
				try {
					lNetResponse = DataExtractor.getJsonObjectFromEntity(argument.getEntity());
					returnData = 
							(new Gson()).fromJson(lNetResponse, AllTheLists.class);
				} catch (com.google.gson.JsonSyntaxException e) {
					try {
						lNetResponse = DataExtractor.getJsonObjectFromEntity(argument.getEntity());
						AllTheList badReturnData = 
								(new Gson()).fromJson(lNetResponse, AllTheList.class);
						List<OneList> goodList = new ArrayList<OneList>();
						goodList.add(badReturnData.getTodoListDTO());
						returnData = new AllTheLists();
						returnData.setTodoListDTO(goodList);
					} catch (com.google.gson.JsonSyntaxException subE) {
						Log.d("ViewLists", "i blame etienne");
					}
				}

				mMessageChannel.post(new ServerDataRunnable(returnData));
			}
		}).start();
	}
	private class ServerDataRunnable implements Runnable {
		ServerDataRunnable(AllTheLists returnData) {
			communicatedObject = returnData;
		}
		protected AllTheLists communicatedObject = null;
		public void run() {
			processServerData(communicatedObject);
		}
	}
	protected void processServerData(AllTheLists communicatedObject) {
		toaster.printMessage(this, "did it! "+communicatedObject.getTodoListDTO().get(0).getName());
		setListAdapter(convertToListAdapter(communicatedObject.getTodoListDTO()));
	}

	/*******************************************************
	 *                                                     *
	 *                   Utility Methods                   *
	 *                                                     *
	 *******************************************************/
	private ViewLists getActivity() {return this;}
	private String getValueFromPreference(String pKey) {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(pKey, "");
	}
	public void onNetworkResponseReceived(HttpResponse response) {
		processResponseInThread(response);
	}
}
