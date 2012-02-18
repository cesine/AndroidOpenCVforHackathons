package com.androidmontreal.tododetector.network;

import org.apache.http.HttpResponse;

import com.androidmontreal.tododetector.network.interfaces.IAnsweringMachine;
import com.androidmontreal.tododetector.network.interfaces.INetworkResponse;

public class MessageMachine implements IAnsweringMachine {

	private INetworkResponse callbackActivity;
	private HttpResponse response;
 
	public MessageMachine(INetworkResponse callbackActivity) {
		this.callbackActivity = callbackActivity;
	}
 
	public void run() {
		callbackActivity.onNetworkResponseReceived(response);
	}
 
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}
