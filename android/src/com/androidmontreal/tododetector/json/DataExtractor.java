package com.androidmontreal.tododetector.json;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;

import android.content.ContextWrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DataExtractor {
	private DataExtractor(){} // Locked
	public static JsonObject getJsonObjectFromEntity(HttpEntity httpEntity) {

		JsonParser parsing = new JsonParser();
		JsonElement output = null;
		JsonObject outj = null;
		
		try {
			output = parsing.parse(new InputStreamReader(httpEntity.getContent()));
			outj = output.getAsJsonObject();
			//outj.remove("lyre");

		} catch (JsonSyntaxException e) {

			e.printStackTrace();
		} catch (JsonIOException e) {

			e.printStackTrace();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return outj;
		
	}
	public static JsonArray getJsonArrayFromEntity(HttpEntity httpEntity) {

		JsonParser parsing = new JsonParser();
		JsonElement output = null;
		JsonArray outj = null;
		
		try {
			output = parsing.parse(new InputStreamReader(httpEntity.getContent()));
				outj = output.getAsJsonArray();

		} catch (JsonSyntaxException e) {

			e.printStackTrace();
		} catch (JsonIOException e) {

			e.printStackTrace();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return outj;
		
	}
	
}
