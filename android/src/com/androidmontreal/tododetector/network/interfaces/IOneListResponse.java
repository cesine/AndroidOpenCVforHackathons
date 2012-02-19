package com.androidmontreal.tododetector.network.interfaces;

import org.apache.http.HttpResponse;

import com.google.gson.JsonObject;

public interface IOneListResponse {
	public void onOneListNetworkResponseReceived(HttpResponse response);
}
