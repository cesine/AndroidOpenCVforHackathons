package com.androidmontreal.tododetector.server.service;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;

import com.androidmontreal.tododetector.server.domain.Todo;
import com.androidmontreal.tododetector.server.domain.TodoList;
import com.androidmontreal.tododetector.server.service.TodoService.TodoListDTO;
import com.kanawish.hibernate.HibernateUtil;
import com.kanawish.hibernate.Transactionnal;

@Path("todo")
public class TodoService {

	@XmlRootElement
	static public class TodoListDTO {
		long id;
		String name;
		List<TodoDTO> todos = new ArrayList<TodoDTO>();

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<TodoDTO> getTodos() {
			return todos;
		}

		public void setTodos(List<TodoDTO> todos) {
			this.todos = todos;
		}
	}
	
	@XmlRootElement
	static public class TodoDTO {
		Long id;
		Boolean checked;
		String imageUrl;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Boolean getChecked() {
			return checked;
		}

		public void setChecked(Boolean checked) {
			this.checked = checked;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
	}
	
	@Transactionnal
	List<TodoList> dbGetLists() {
		List<TodoList> list = 
			(List<TodoList>)HibernateUtil.getCurrentSession().createQuery("from TodoList as l").list();
		return list ;
	}
	
	@Path("/lists")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TodoListDTO> getLists() {
		List<TodoList> lists = dbGetLists();
		ArrayList<TodoListDTO> dtos = new ArrayList<TodoListDTO>();
		for( TodoList current : lists ) {
			TodoListDTO dto = new TodoListDTO();
			dto.setId(current.getId());
			dto.setName(current.getName());
			dtos.add(dto);
		}
		
		return dtos ;
	}
	
	@Transactionnal
	TodoList dbGetList(Long id) { 		
		TodoList todoList = (TodoList) HibernateUtil.getCurrentSession().load(TodoList.class, id);
		return todoList;
	}

	@Path("/lists/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TodoListDTO getList(@PathParam("id") long id) {
		TodoList list = dbGetList(id);
		TodoListDTO dto = new TodoListDTO();
		dto.setId(list.getId());
		dto.setName(list.getName()); 
		for( Todo current : list.getTodos() ) {
			TodoDTO todoDTO = new TodoDTO();
			todoDTO.setId(current.getId());
			todoDTO.setChecked(current.isChecked());
			todoDTO.setImageUrl("http://winniecooper.net/sam/2011/12/img/MIAWINNIE.jpg"); // TODO: Implement
			dto.getTodos().add(todoDTO);
		}
		
		return dto;
	}
	
	@Transactionnal
	private long dbCreateList( String name ) {
		TodoList list = new TodoList();
		list.setName(name);
		HibernateUtil.getCurrentSession().saveOrUpdate(list);
		return list.getId();
	}
	
	@Path("/lists/{name}")
	@PUT
	public long createList( @PathParam("name") String name ) {
		return dbCreateList(name) ;
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
	public byte[] dbGetImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("/lists/{id}/item/{itemId}/img")
	@Produces("image/*")
	public Response getImage(@PathParam(value = "id") long id, @PathParam(value = "itemId") long itemId ) {
		
		//TODO: Implement the db get
		byte[] imgData = dbGetImageData();

		// Test this return method.
		if (imgData != null) {
			// final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final InputStream bigInputStream = new ByteArrayInputStream(imgData);
			//.cacheControl(getCacheControl(true))
			return Response.ok(bigInputStream).build();
		}

		return Response.noContent().build();
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
