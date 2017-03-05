package com.prototype.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.Argument;
import com.prototype.model.Claim;

public class ClaimDao {
	
	public void saveClaim(Claim claim){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		for(Argument arg : claim.getArguments()){
			for(Claim premise : arg.getPremises()){
				session.save(premise);
			}
			session.save(arg);
		}
		session.save(claim);
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		System.out.println("saved the following claim: " + claim.getClaimStatement());
	}
	
	public List<Claim> getTopClaims(){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Claim> topClaims = new ArrayList<Claim>();
		System.out.println("Hibernate attempting to load the claim...");
		
		Query query = session.createSQLQuery(getAllClaimsQuery()).addEntity(Claim.class);
		
		topClaims = query.list();
		for(Claim claim : topClaims){
			System.out.println(claim.getClaimStatement());
			ArrayList<Argument> arguments = new ArrayList<Argument>();
			claim.setArguments(arguments);
		}
		
		
		session.close();
		sessionFactory.close();
		return topClaims;
	}
	
	public String getAllClaimsQuery(){
		return "select * from claim";
	}
	
	

}
