package com.androidmontreal.tododetector.network.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteOrder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DataDownload {

	private DataDownload () {} // locked
	public static String DownloadFileToCache(String targetDirectory, String URI) {
		try {
			if(new File(targetDirectory+"/"+getFileName(URI)).exists())
				return (targetDirectory+"/"+getFileName(URI));
			new DefaultHttpClient().execute(new HttpGet(URI)).getEntity().writeTo(
					new FileOutputStream(targetDirectory+"/"+getFileName(URI))
					);
			return targetDirectory+"/"+getFileName(URI);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ByteArrayOutputStream DownloadFileToByte(String URI) {
		try {
			ByteArrayOutputStream moo = new ByteArrayOutputStream();
			//moo.connect(decodeTarget);
			new DefaultHttpClient().execute(new HttpGet(URI)).getEntity().writeTo(moo);
			return moo;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getFileName(String pFilePath){
		return pFilePath.substring(pFilePath.lastIndexOf("/")).replace("/", "");
	}

	public static String getArtPathFromServer(int album_id) {

		return null;
	}
}