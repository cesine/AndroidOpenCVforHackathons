/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidmontreal.opencv;


import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;


public class AndroidOpenCVforHackathonsActivity extends Activity implements PictureCallback
{
	private static final String TAG = "OpenCVforHackathons";
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "=onCreate=");
		
        /* Create a TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
        setContentView(R.layout.main);
        TextView  tv = (TextView) findViewById(R.id.textview1);
        tv.setText( stringFromJNI() );
        
    }

    @Override
	protected void onDestroy() {
    	Log.d(TAG, "=onDestroy=");
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "===onLowMemory===");
		super.onLowMemory();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "====onPause====");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "==onResume==");
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "==onStop==");
		super.onStop();
	}

	public void onCaptureClick(View v) {
		Button capture = (Button) findViewById(R.id.capture);
		capture.setEnabled(false);

		// Take picture
		OpenCVPreview previewView = (OpenCVPreview) findViewById(R.id.preview);
		Camera camera = previewView.getCamera();
		camera.takePicture(null, null, this);

    }
    public void onPictureTaken(byte[] data, Camera camera) {
		/*
		 * Do some thing
		 */
		finish();
	}
    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native String  stringFromJNI();

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String  unimplementedStringFromJNI();


    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.HelloJni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("opencv_sample");
    }
}
