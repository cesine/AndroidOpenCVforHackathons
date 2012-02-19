package com.androidmontreal.tododetector.json.datatype;

import java.util.List;


public class Elements {
	private List<TodoElement> todos;
	private String name;
	private int id;

	public List<TodoElement> getListElements() {
		return todos;
	}

	public void setListElements(List<TodoElement> listElements) {
		this.todos = listElements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
