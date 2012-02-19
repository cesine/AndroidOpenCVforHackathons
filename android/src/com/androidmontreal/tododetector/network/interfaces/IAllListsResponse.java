package com.androidmontreal.tododetector.network.interfaces;

import org.apache.http.HttpResponse;

import com.google.gson.JsonObject;

public interface IAllListsResponse {
	public void onAllListsNetworkResponseReceived(HttpResponse response);
}
