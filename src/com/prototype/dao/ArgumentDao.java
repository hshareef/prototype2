package com.prototype.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import com.prototype.common.CommonConstants;
import com.prototype.common.CommonConstants.PremiseBindingTypes;
import com.prototype.model.Argument;
import com.prototype.model.ArgumentState;
import com.prototype.model.Claim;
import com.prototype.model.ClaimRef;
import com.prototype.model.ClaimState;
import com.prototype.model.FallacyDetails;
import com.prototype.model.MediaResource;
import com.prototype.model.MissedPremiseObjection;
import com.prototype.model.PremiseBindingParameters;
import com.prototype.model.PremiseOrderWrapper;

public class ArgumentDao {

	public Argument saveArgument(Argument arg) {
		//DO NOT SAVE MPO's here! Saving MPO should call its own saveMPO function
		//Set<Integer> premisesToBindIds = new HashSet<Integer>();
		List<Integer> premiseIdsToBind = new ArrayList<Integer>();
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		for(Claim premise : arg.getPremises()){
			if(premise.getClaimId() == null){
				session.saveOrUpdate(premise);
			}
			else {
				premiseIdsToBind.add(premise.getClaimId());
			}
		}
		if(arg.getStateHistory() != null){
			for(ArgumentState state : arg.getStateHistory()){
				session.saveOrUpdate(state);
			}
		}
		
		session.saveOrUpdate(arg);
		session.getTransaction().commit();
		//bindings must be done at the end once all components (args, premises, etc) are already created
		bindPremises(arg.getArgumentId(), premiseIdsToBind, session);
		session.close();
		sessionFactory.close();
		System.out.println("Saved the argument: " + arg.getArgName());
		return arg;
	}
	
	private void bindPremises(Integer argumentId, List<Integer> premiseIdsToBind, Session session) {
		for (Integer premiseId : premiseIdsToBind) {
			bindPremise(argumentId, premiseId, session);
		}
	}
	
	
	private void bindPremise(Integer argumentId, Integer premiseId, Session session){
		
		String sql = "insert into ARGUMENT_PREMISE_JT (ARGUMENT_ARGUMENT_ID, PREMISES_CLAIM_ID) values (:argumentId, :premiseId)";
		Query query = session.createSQLQuery(sql);
		query.setParameter("argumentId", argumentId);
		query.setParameter("premiseId", premiseId);
		query.executeUpdate();
		
	}
	
	//loads the claims that contain arguments from a given user
//	public List<Argument> getUserArguments(Integer userId) {
//		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		List<Argument> userArguments = new ArrayList<Argument>();
//		System.out.println("Hibernate attempting to load the claims for user " + userId + "...");
//		
//		Query query = session.createSQLQuery(getUserArgumentsQuery()).addEntity(Argument.class);
//		query.setInteger("userId", userId);
//
//		userArguments = query.list();
//		for(Argument argument : userArguments){
//			for (Claim premise : argument.getPremises()) {
//				premise.setArguments(new ArrayList<Argument>());
//				premise.setStateHistory(new ArrayList<ClaimState>());
//				premise.setKeywords(new ArrayList<String>());
//				premise.setCategoryIds(null);
//				premise.setOppositeClaims(new ArrayList<Claim>());
//				premise.setMediaResources(new ArrayList<MediaResource>());
//			}
//		}
//		session.close();
//		sessionFactory.close();
//		return userArguments;		
//	}

//	private String getUserArgumentsQuery() {
//		StringBuffer query = new StringBuffer("select ag.*");
//		query.append(" from Prototype.argument ag");
//		query.append(" join prototype.argument_state ags on ags.argument_id = ag.argument_id and cs.current_flag = 1 and ags.argument_status_id in (1,2) \n");//only want pending or published arguments
//		query.append(" where ag.owner_id = :userId");
//		query.append(" order by created_ts desc");
//		return query.toString();
//	}

}
