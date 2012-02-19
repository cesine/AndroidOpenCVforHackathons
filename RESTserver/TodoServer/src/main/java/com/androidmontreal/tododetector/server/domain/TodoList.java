package com.androidmontreal.tododetector.server.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "TODOLIST")
@XmlRootElement
public class TodoList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name ;
	
	@OneToMany
	private List<Todo> todos = new ArrayList<Todo>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

}
