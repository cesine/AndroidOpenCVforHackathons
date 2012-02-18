package com.androidmontreal.tododetector.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.androidmontreal.tododetector.network.interfaces.IAnsweringMachine;
 
import android.os.Handler;
 
public class AsynchronousSender extends Thread {
 
	private HttpClient httpClient = null;
	
	private HttpUriRequest request;
	private Handler handler;
	private IAnsweringMachine wrapper;
 
	protected AsynchronousSender(HttpUriRequest request, Handler handler, IAnsweringMachine wrapper) {
		this.request = request;
		this.handler = handler;
		this.wrapper = wrapper;
	}
 
	public void run() {
		try {
			final HttpResponse response;
			
			// Asynchronous calls. Each call might actually be
			// separated by a LONG period of time.
			if(httpClient == null)
				httpClient = new DefaultHttpClient();
			synchronized (httpClient) {
				response = getClient().execute(request);
			}
			// process response
			wrapper.setResponse(response);
			handler.post(wrapper);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	private HttpClient getClient() {
		return httpClient;
	}
 
}
