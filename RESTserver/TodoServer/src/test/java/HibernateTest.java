

import java.util.Date;

import org.hibernate.Session;

import com.androidmontreal.tododetector.server.domain.Todo;
import com.androidmontreal.tododetector.server.domain.TodoList;
import com.kanawish.hibernate.HibernateUtil;


public class HibernateTest {

	public static void main(String[] args) {
		HibernateTest mgr = new HibernateTest();
		mgr.createAndStoreEvent();

		HibernateUtil.getSessionFactory().close();
	}

	private void createAndStoreEvent() {
		HibernateUtil.initSessionFactory("hibernate.todo.cfg.xml");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		for( int i = 0 ; i < 4 ; i++ )
		{
			TodoList todoList1 = new TodoList();
			
			Todo t1 = new Todo();
			t1.setChecked(false);
			Todo t2 = new Todo();
			t2.setChecked(false);
			
			todoList1.getTodos().add(t1);
			todoList1.getTodos().add(t2);
			
			session.save(t2);
			session.save(t1);
			
			session.save(todoList1);
		}
		session.getTransaction().commit();
	}

}
