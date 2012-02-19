package com.androidmontreal.tododetector.ui.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.androidmontreal.tododetector.R;
import com.androidmontreal.tododetector.json.DataExtractor;
import com.androidmontreal.tododetector.json.datatype.*;
import com.androidmontreal.tododetector.network.NetworkChatter;
import com.androidmontreal.tododetector.network.interfaces.INetworkResponse;
import com.androidmontreal.tododetector.ui.toaster;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ViewLists extends SherlockListActivity  implements INetworkResponse {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<TodosElement> data = getListFromServer();

		setListAdapter(convertToListAdapter(data));

	}

	private ListAdapter convertToListAdapter(List<TodosElement> data) {		
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapElement = null;
		for (TodosElement i : data) {
			mapElement = new HashMap<String, Object>();
			mapElement.put("title", i.getTodoname());
			mapElement.put("value", Long.valueOf(i.getTodoid()));
			mapList.add(mapElement);
		}
		SimpleAdapter returnValue = new SimpleAdapter(
				this, mapList, android.R.layout.simple_list_item_1,
				new String[] { "title" },
				new int[] { android.R.id.text1 });
		return returnValue;
	}

	private List<TodosElement> getListFromServer() {
		// TODO Auto-generated method stub
		List<TodosElement> listOfLists = new ArrayList<TodosElement>();
		TodosElement lFakeTodo = new TodosElement();
		lFakeTodo.setTodoid(5);
		lFakeTodo.setTodoname("Faaaaake");
		listOfLists.add(lFakeTodo);
		return listOfLists;
	}
    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

        toaster.printMessage(this, "Long value to text: " + ((Long)map.get("value")).toString());
        
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
			return;
		}
		NetworkChatter.getRemoteData(lBaseURL, this);
	}
	private void processResponseInThread(HttpResponse response){

		final HttpResponse argument = response;
		
		new Thread(new Runnable() {
			public void run() {
				JsonObject lNetResponse = null;
				lNetResponse = DataExtractor.getJsonObjectFromEntity(argument.getEntity());
				Elements returnData = 
						(new Gson()).fromJson(lNetResponse, Elements.class);
				mMessageChannel.post(new ServerDataRunnable(returnData));
			}
		}).start();
	}
	private class ServerDataRunnable implements Runnable {
		ServerDataRunnable(Elements pObject) {
			communicatedObject = pObject;
		}
		protected Elements communicatedObject = null;
		public void run() {
			processServerData(communicatedObject);
		}
	}
	protected void processServerData(Elements communicatedObject) {
		toaster.printMessage(this, "did it! "+communicatedObject.getListElements().get(0).getImageurl());
	}
	
	public void onNetworkResponseReceived(HttpResponse response) {
		processResponseInThread(response);
	}
}
