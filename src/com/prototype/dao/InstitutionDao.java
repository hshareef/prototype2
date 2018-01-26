package com.prototype.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.Institution;

public class InstitutionDao {

	public Institution saveInstitution(Institution institution) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(institution);
		
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		return institution;
	}

}
