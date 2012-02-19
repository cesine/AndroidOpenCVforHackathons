package com.androidmontreal.tododetector.server.service;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import com.androidmontreal.tododetector.server.domain.Todo;
import com.androidmontreal.tododetector.server.domain.TodoList;
import com.kanawish.hibernate.HibernateUtil;
import com.kanawish.hibernate.Transactionnal;

@Path("todo")
public class TodoService {

	@Transactionnal
	List<TodoList> dbGetLists() {
		List<TodoList> list = 
			(List<TodoList>)HibernateUtil.getCurrentSession().createQuery("from TodoList as l").list();
		return list ;
	}
	
	@Path("/lists")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TodoList> getLists() {
		return dbGetLists() ;
	}
	
	@Transactionnal
	TodoList dbGetList(Long id) { 
		return (TodoList) HibernateUtil.getCurrentSession().get(TodoList.class, id);
	}

	@Path("/lists/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TodoList getList(@PathParam("id") long id) {
		return dbGetList(id);
	}
	
	@Transactionnal
	private long dbCreateList() {
		TodoList list = new TodoList();
		HibernateUtil.getCurrentSession().saveOrUpdate(list);
		return list.getId();
	}
	
	@Path("/lists")
	@PUT
	public long createList( ) {
		return dbCreateList() ;
	}
	
	// TODO: Delete list
	@Transactionnal
	void dbDeleteList(Long id ) {
		Session currentSession = HibernateUtil.getCurrentSession();
		TodoList list = (TodoList) currentSession.load(TodoList.class, id);
		currentSession.delete(list);
	}
	
	@Path("/lists/{id}")
	@DELETE
	public void deleteList( @PathParam("id") long id ) {
		dbDeleteList(id);
	}
	
	@Transactionnal
	long dbAddNewTodo(Long listId, byte[] image) {
		Session session = HibernateUtil.getCurrentSession();
		TodoList foundList = (TodoList) session.load(TodoList.class, listId);
		Todo todo = new Todo();
		todo.setChecked(false);
		todo.setImage(image);
		session.save(todo);
		foundList.getTodos().add(todo);
		session.save(foundList);
		
		return todo.getId(); 
	}
	
	@PUT
	@Path("/lists/{id}/item")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_HTML)
	public long addNewTodo( @PathParam("id") long listId, byte[] data ) {
		return dbAddNewTodo(listId, data);
	}
	
	@Transactionnal
	void dbCheckItem(Long todoId) {
		Session session = HibernateUtil.getCurrentSession();
		Todo foundTodo = (Todo) session.load(Todo.class, todoId);
		foundTodo.setChecked(true);
		session.saveOrUpdate(foundTodo);
	}
	
	@PUT
	@Path("/lists/{id}/item/check")
	public void checkItem( @PathParam("id") long todoId ) {
		dbCheckItem(todoId);
	}
	
}
