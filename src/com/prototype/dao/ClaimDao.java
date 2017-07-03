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

	public List<Claim> getSearchedClaims(List<String> words) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		ArrayList <Claim> searchedClaims = new ArrayList<Claim>();
		String sql = generateSearchSql(words);
		Query query = session.createSQLQuery(sql).addEntity(Claim.class);
		for (int i=0; i<words.size(); i++){
			query.setString("param"+i,words.get(i));
		}
		System.out.println(query.toString());
		searchedClaims = (ArrayList<Claim>) query.list();
		for(Claim claim : searchedClaims){
			System.out.println(claim.getClaimStatement());
			ArrayList<Argument> arguments = new ArrayList<Argument>();
			claim.setArguments(arguments);
			ArrayList <String> keywords = new ArrayList<String>();
			claim.setKeywords(keywords);
		}
		
		
		session.close();
		sessionFactory.close();
		return searchedClaims;
		
	}

	private String generateSearchSql(List<String> words) {
		
		
		StringBuffer query = new StringBuffer();
		query.append(" select  claim.*, null as occurance \n");
		query.append(" from prototype.claim claim where claimStatement like concat('% ', :param0, ' %')\n");
		query.append(" or claimStatement like concat(:param0, ' %')\n");
		query.append(" or claimStatement like concat('% ', :param0, '_')\n");
		query.append(" or claimStatement like concat('% ', :param0)\n");
		query.append(" or claimStatement like :param0");
		query.append(" union \n");
		query.append("( \n");
		query.append("select claim.*, count(claimId) as occurance from \n");
		query.append("( \n");
		for(int i = 1; i < words.size(); i++){
			query.append(" select  * from prototype.claim \n");
			query.append("where ( claimStatement like concat('% ', :param"+i+", ' %') \n");
			query.append("OR claimStatement like concat(:param"+i+", ' %') \n");
			query.append("OR claimStatement like concat('% ', :param"+i+") \n");
			query.append("OR claimStatement like concat('% ', :param"+i+", '_')) \n");
			query.append(" and claimStatement not like concat('% ', :param0, ' %') \n");
			if(i != words.size()-1){
				query.append(" union all \n");
			}
		}
		query.append(") claim \n");
		query.append("group by claimId order by occurance desc \n");
		query.append(") \n");
		System.out.println(query.toString());
		return query.toString();
	}
}
