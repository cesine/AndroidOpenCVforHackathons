package com.androidmontreal.tododetector.network.interfaces;

import org.apache.http.HttpResponse;

public interface IAnsweringMachine extends Runnable {

	public void run();
	public void setResponse(HttpResponse response);
}
