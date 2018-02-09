package com.prototype.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.MediaResource;

public class MediaResourceDao {
	
	public MediaResource saveMediaResource(MediaResource mediaResource){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(mediaResource);
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		return mediaResource;
	}

}
