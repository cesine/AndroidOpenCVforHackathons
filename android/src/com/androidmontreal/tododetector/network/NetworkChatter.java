package com.androidmontreal.tododetector.network;

import org.apache.http.client.methods.HttpGet;

import android.os.Handler;

import com.androidmontreal.tododetector.network.interfaces.IAllListsResponse;
import com.androidmontreal.tododetector.network.interfaces.ICreateList;
import com.androidmontreal.tododetector.network.interfaces.IOneListResponse;

public class NetworkChatter {

	/*public static void getRemoteData(String pURL, INetworkResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(pURL));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}
	*/
	public static void createOneList(String pURL, String pName, ICreateList listeningActivity) {
		// Future list creation
	}

	public static void getOneList(String pURL, long pListID, IOneListResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(pURL, "lists/"+pListID));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}
	public static void getAllTheLists(String pURL, IAllListsResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(pURL, "lists"));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}

	private static String generateCommandURL(String pURL, String userCommand) {
		return (getBaseURL().concat("/"+userCommand));
	}

	private static String getBaseURL() {
		return "http://10.40.20.133:8080/todo";
	}
}
