package com.androidmontreal.tododetector.network;

import org.apache.http.HttpResponse;

import com.androidmontreal.tododetector.network.interfaces.IAnsweringMachine;
import com.androidmontreal.tododetector.network.interfaces.IAllListsResponse;
import com.androidmontreal.tododetector.network.interfaces.IOneListResponse;

public class MessageMachine implements IAnsweringMachine {

	private IAllListsResponse mALCallbackActivity = null;
	private IOneListResponse mOLCallbackActivity = null;
	private HttpResponse response;

	public MessageMachine(IAllListsResponse callbackActivity) {
		mOLCallbackActivity = null;
		this.mALCallbackActivity = callbackActivity;
	}

	public MessageMachine(IOneListResponse callbackActivity) {
		this.mOLCallbackActivity = callbackActivity;
		mALCallbackActivity = null;
	}

	public void run() {
		if(mALCallbackActivity == null)
			mOLCallbackActivity.onOneListNetworkResponseReceived(response);
		else if(mOLCallbackActivity==null)
			mALCallbackActivity.onAllListsNetworkResponseReceived(response);
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}
