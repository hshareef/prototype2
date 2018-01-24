package com.prototype.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.Author;

public class AuthorDao {
	
	public Author saveAuthor(Author author){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(author);
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		return author;
	}

}
