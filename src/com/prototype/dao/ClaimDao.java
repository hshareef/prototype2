package com.prototype.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import com.prototype.model.Argument;
import com.prototype.model.Claim;
import com.prototype.model.FallacyDetails;

public class ClaimDao {
	
	public void saveClaim(Claim claim){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		for(Argument arg : claim.getArguments()){
			for(Claim premise : arg.getPremises()){
				session.saveOrUpdate(premise);
			}
			if(arg.getFallacyDetails() == null){
				FallacyDetails fd = new FallacyDetails();
				arg.setFallacyDetails(fd);
			}
			session.saveOrUpdate(arg.getFallacyDetails());
			session.saveOrUpdate(arg);
		}
		session.saveOrUpdate(claim);
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
			ArrayList <String> keywords = new ArrayList<String>();
			claim.setKeywords(keywords);
		}
		
		
		session.close();
		sessionFactory.close();
		return topClaims;
	}
	
	public String getAllClaimsQuery(){
		return "select * from claim";
	}

	public Claim getClaim(Integer claimId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Claim claim;
		System.out.println("Hibernate attempting to load the claim with claim id " + claimId + "...");
		
		claim = (Claim)session.get(Claim.class, claimId);
		
		
	     Hibernate.initialize(claim.getArguments());
	     Hibernate.initialize(claim.getKeywords());
	     
		
		for(Argument argument : claim.getArguments()){
			 Hibernate.initialize(argument.getFallacyDetails());
		     Hibernate.initialize(argument.getPremises());
		     for(Claim premise : argument.getPremises()){
		 		ArrayList<Argument> premiseArguments = new ArrayList<Argument>();
		 		premise.setArguments(premiseArguments); 
		 		ArrayList<String> keywords = new ArrayList<String>();
		 		premise.setKeywords(keywords);
		     }
		}
		session.close();
		sessionFactory.close();
		return claim;
		
	}
}
