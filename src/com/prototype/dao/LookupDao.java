package com.prototype.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import com.prototype.model.Lookup;

public class LookupDao {
	
	public List<Lookup> getLookups(String lookupType){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.createSQLQuery("select * from CLAIM_CATEGORIES_LKUP").addEntity(Lookup.class);
		//query.setParameter("lookupTable", lookupType);
		List<Lookup> lookups = query.list();
		//List<Object> stuff = query.list();
		int h = 3;
		return lookups;
	}

}
