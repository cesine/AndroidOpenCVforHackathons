package com.androidmontreal.opencv;

import android.app.Application;

public class AndroidOpenCVforHackathonsApp extends Application {
	public String lastMessage;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lastMessage="No message";
	}
	
	public String getLastMessage(){
		return this.lastMessage;
	}
	public void setLastMessage( String lastMessage){
		this.lastMessage = lastMessage;
	}
}
