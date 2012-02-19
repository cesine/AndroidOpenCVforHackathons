package com.androidmontreal.tododetector.network;

import org.apache.http.HttpResponse;

import com.androidmontreal.tododetector.network.interfaces.IAnsweringMachine;
import com.androidmontreal.tododetector.network.interfaces.IAllListsResponse;
import com.androidmontreal.tododetector.network.interfaces.IOneListResponse;

public class MessageMachine implements IAnsweringMachine {

	private IAllListsResponse mALCallbackActivity;
	private IOneListResponse mOLCallbackActivity;
	private HttpResponse response;

	public MessageMachine(IAllListsResponse callbackActivity) {
		this.mALCallbackActivity = callbackActivity;
	}

	public MessageMachine(IOneListResponse callbackActivity) {
		this.mOLCallbackActivity = callbackActivity;
	}
 
	public void run() {
		mALCallbackActivity.onAllListsNetworkResponseReceived(response);
	}
 
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}
