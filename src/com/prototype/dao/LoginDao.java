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
		Query query = ((SQLQuery) session.createSQLQuery(getUserQuery()).setParameter("sn", username)).addEntity(User.class);
		users = query.list();
		session.close();
		sessionFactory.close();
		if (users.size() > 1) {
			System.out.println("Error: found mutiple users with same username");
			return null;
		} else if (users.size()  == 1){
			return users.get(0);
		}else{
			return null;
		}
		
	}
	
	private String getUserQuery(){
		return "select * from user where username= :sn";
	}
	
	public boolean updateToken (int userId, String loginToken) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String sql = "update Prototype.user set login_token = :login_token, token_expiration = TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP) where user_id = :user_id";
		Query query = session.createSQLQuery(sql);
		query.setParameter("login_token", loginToken);
		query.setParameter("user_id", userId);
		int numUpdates = query.executeUpdate();
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		return numUpdates > 0;
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
		String sql = "select * from User where email_address = :emailAddress";
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

	public User getUser(Integer userId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		User user =	(User)session.get(User.class, userId);
		
		
		session.close();
		sessionFactory.close();

		return user;
	}
	
}
