package com.androidmontreal.tododetector.network;

import org.apache.http.client.methods.HttpGet;

import android.os.Handler;

import com.androidmontreal.tododetector.network.interfaces.INetworkResponse;

public class NetworkChatter {

	public static void getRemoteData(String userCode, INetworkResponse listeningActivity) {
		HttpGet reqData = new HttpGet(generateCommandURL(userCode));
		(new AsynchronousSender(reqData, new Handler(), new MessageMachine(listeningActivity))).start();
	}

	private static String generateCommandURL(String userCode) {
		return (getBaseURL().concat(userCode));
	}

	private static String getBaseURL() {
		return "https://www.videotron.com/api/1.0/internet/usage/wired/";
	}
}
