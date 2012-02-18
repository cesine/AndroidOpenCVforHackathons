package com.androidmontreal.tododetector.ui;

import android.content.Context;
import android.widget.Toast;

public class toaster {

	// Constructeur barré
	private toaster(){}
	
	// Fonction "accélérée" pour afficher un micromessage à l'écran
	public static void printMessage(Context pAppContext, String pMessage) {
		CharSequence text = pMessage;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(pAppContext, text, duration);
		toast.show();
	}
}
