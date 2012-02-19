package com.androidmontreal.tododetector.network;

import org.apache.http.client.methods.HttpGet;

import android.os.Handler;

import com.androidmontreal.tododetector.network.interfaces.INetworkResponse;

public class NetworkChatter {

	/*public static void getRemoteData(String pURL, INetworkResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(pURL));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}*/

	public static void getOneList(String pURL, long pListID, INetworkResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(pURL, "lists/"+pListID));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}
	public static void getAllTheLists(String pURL, INetworkResponse listeningActivity) {
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
