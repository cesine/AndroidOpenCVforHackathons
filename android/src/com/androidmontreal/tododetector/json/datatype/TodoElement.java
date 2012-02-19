package com.androidmontreal.tododetector.json.datatype;

public class TodoElement {
	private int id;
	private boolean checked;
	private String imageUrl;
	public TodoElement(boolean pChecked, String pImageurl) {
		imageUrl=pImageurl;
		checked=pChecked;
	}
	public String getImageurl() {
		return imageUrl;
	}
	public void setImageurl(String imageurl) {
		this.imageUrl = imageurl;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
