package com.prototype.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.User;

public class LoginDao {

	public User getUser(String username, String password){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List <User> users;
		Query query = ((SQLQuery) session.createSQLQuery(getUserQuery()).setParameter("sn", username).setParameter("pw", password)).addEntity(User.class);
		users = query.list();
		
		session.close();
		sessionFactory.close();
		if (users.size() > 0){
			return users.get(0);
		}else{
			return null;
		}
		
	}
	
	private String getUserQuery(){
		return "select * from User where username= :sn and password= :pw";
	}
	
	//FOR DEVELOPMENT PURPOSES ONLY!
	public void saveUser(String username, String password, String firstName, String lastName, String emailAddress){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmailAddress(emailAddress);
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(user);
		
		session.getTransaction().commit();
		
		session.close();
		sessionFactory.close();
	}

	public void createNewUser(User newUser) {
		
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(newUser);
		
		session.getTransaction().commit();
		
		session.close();
		sessionFactory.close();
	}

	public boolean checkEmailAddressUsed(String emailAddress) {
		String sql = "select * from User where emailAddress = :emailAddress";
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List <User> users;
		Query query = ((SQLQuery) session.createSQLQuery(sql).setParameter("emailAddress", emailAddress)).addEntity(User.class);
		users = query.list();
		session.close();
		sessionFactory.close();
		if(users.size() > 0){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean checkUsernameAvailable(String username) {
		String sql = "select * from User where username = :username";
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List <User> users;
		Query query = ((SQLQuery) session.createSQLQuery(sql).setParameter("username", username)).addEntity(User.class);
		users = query.list();
		session.close();
		sessionFactory.close();
		if(users.size() > 0){
			return false;
		}
		else{
			return true;
		}
	}
	
}
