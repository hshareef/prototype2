package com.prototype.dao;

import java.util.ArrayList;
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
import com.prototype.model.FallacyDetails;
import com.prototype.model.MediaResource;
import com.prototype.model.MissedPremiseObjection;
import com.prototype.model.PremiseBindingParameters;

public class ClaimDao {
	
	public Claim saveClaim(Claim claim){
		//List<PremiseBindingParameters> premisesToBind = new ArrayList<PremiseBindingParameters>();
		Set<Integer> premisesToBindIds = new HashSet<Integer>();
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		for(Argument arg : claim.getArguments()){
			for(Claim premise : arg.getPremises()){
				if(premise.getClaimId() == null){
					//Claim premiseClaim = new Claim();
					//premiseClaim.setClaimStatement(premise.getClaimStatement());
					//session.save(premiseClaim);
					//premise.setClaimId(premiseClaim.getClaimId());
					session.saveOrUpdate(premise);
				}
				else {
					premisesToBindIds.add(premise.getClaimId());
					//premisesToBind.add(new PremiseBindingParameters(arg.getArgumentId(), premise.getClaimId(), CommonConstants.PremiseBindingTypes.ARG));
					//bindPremise(arg.getArgumentId(), premise.getClaimId(), session, CommonConstants.PremiseBindingTypes.ARG);
				}
			}
			if(arg.getStateHistory() != null){
				for(ArgumentState state : arg.getStateHistory()){
					session.saveOrUpdate(state);
				}
			}
			if(arg.getFallacyDetails() == null){
				FallacyDetails fd = new FallacyDetails();
				arg.setFallacyDetails(fd);
			}
			session.saveOrUpdate(arg.getFallacyDetails());
			
			if(arg.getMissedPremiseObjections() != null && arg.getMissedPremiseObjections().size() > 0){
				for(MissedPremiseObjection mpo : arg.getMissedPremiseObjections()){
					session.saveOrUpdate(mpo);
					for(Claim premise : mpo.getMissedPremises()){
						if(premise.getClaimId() == null){
							session.saveOrUpdate(premise);
						}
						else {
							premisesToBindIds.add(premise.getClaimId());
							//premisesToBind.add(new PremiseBindingParameters(mpo.getMissedPremiseObjectionId(), premise.getClaimId(), CommonConstants.PremiseBindingTypes.MPO));
							//bindPremise(mpo.getMissedPremiseObjectionId(), premise.getClaimId(), session, CommonConstants.PremiseBindingTypes.MPO);
						}
					}
					
				}
			}
			
			session.saveOrUpdate(arg);
		}
		if(claim.getOppositeClaims() != null){
			for(Claim oppositeClaim : claim.getOppositeClaims()){
				//session.saveOrUpdate(oppositeClaims);
				premisesToBindIds.add(oppositeClaim.getClaimId());
				//premisesToBind.add(new PremiseBindingParameters(claim.getClaimId(), oppositeClaims.getClaimId(), CommonConstants.PremiseBindingTypes.OPPO));
				//bindPremise(claim.getClaimId(), oppositeClaims.getClaimId(), session, CommonConstants.PremiseBindingTypes.OPPO);
			}
		}
		session.saveOrUpdate(claim);
		session.getTransaction().commit();
		List<PremiseBindingParameters> premisesToBind = getAllBindingParameters(premisesToBindIds, claim);
		bindAllPremises(premisesToBind, session);
		session.close();
		sessionFactory.close();
		System.out.println("saved the following claim: " + claim.getClaimStatement());
		return claim;
	}
	
	private List<PremiseBindingParameters> getAllBindingParameters(Set<Integer> premiseIds, Claim claim){
		List<PremiseBindingParameters> premisesToBindParameters = new ArrayList<PremiseBindingParameters>();
		for(Argument arg : claim.getArguments()){
			for(Claim premise : arg.getPremises()){
				if(premiseIds.contains(premise.getClaimId())){
					premisesToBindParameters.add(new PremiseBindingParameters(arg.getArgumentId(), premise.getClaimId(), CommonConstants.PremiseBindingTypes.ARG));
				}
			}
			if(arg.getMissedPremiseObjections() != null && arg.getMissedPremiseObjections().size() > 0){
				for(MissedPremiseObjection mpo : arg.getMissedPremiseObjections()){
					for(Claim premise : mpo.getMissedPremises()){
						if(premiseIds.contains(premise.getClaimId())){
							premisesToBindParameters.add(new PremiseBindingParameters(mpo.getMissedPremiseObjectionId(), premise.getClaimId(), CommonConstants.PremiseBindingTypes.MPO));
						}
					}
				}
			}
			
		}
		
		if(claim.getOppositeClaims() != null){
			for(Claim oppositeClaim : claim.getOppositeClaims()){
				if(premiseIds.contains(oppositeClaim.getClaimId())){
					premisesToBindParameters.add(new PremiseBindingParameters(claim.getClaimId(), oppositeClaim.getClaimId(), CommonConstants.PremiseBindingTypes.OPPO));
				}
			}
		}
		
		return premisesToBindParameters;
	}
	
	private void bindAllPremises(List<PremiseBindingParameters> premises, Session session){
		for(PremiseBindingParameters premise : premises){
			bindPremise(premise.getParentId(), premise.getPremiseId(), session, premise.getPremiseBindingType());
		}
	}
	
//	public void bindPremise(Integer argId, Integer mpoId, Integer premiseId, Session session){
//		if (argId != null && mpoId != null){
//			System.out.println("Premise must be mapped to only arg or mpo, not both");
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (argId == null && mpoId == null){
//			System.out.println("Premise must be mapped to an arg or mpo");
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		String sql = "";
//		
//		if(argId != null){
//			sql = "insert into ARGUMENT_PREMISE_JT (ARGUMENT_ARGUMENT_ID, PREMISES_CLAIM_ID) values (:argId, :premiseId)";
//			Query query = session.createSQLQuery(sql);
//			query.setParameter("argId", argId);
//			query.setParameter("premiseId", premiseId);
//			query.executeUpdate();
//		}
//		
//		if(mpoId != null){
//			sql = "insert into MPO_PREMISE_JT (MPO_MPO_ID, MISSEDPREMISES_CLAIM_ID) values (:mpoId, :premiseId)";
//			Query query = session.createSQLQuery(sql);
//			query.setParameter("mpoId", mpoId);
//			query.setParameter("premiseId", premiseId);
//			query.executeUpdate();
//		}
//		
//	}
	
	public void bindPremise(Integer parentId, Integer premiseId, Session session, PremiseBindingTypes premiseBindingType){
		//NEED TO FIX THIS SO IT DOESN'T ADD DUPLICATE ROWS!!!!!
		
		String sql = "";
		
		if(premiseBindingType.code.equals(CommonConstants.PremiseBindingTypes.ARG.code)){
			sql = "insert into ARGUMENT_PREMISE_JT (ARGUMENT_ARGUMENT_ID, PREMISES_CLAIM_ID) values (:parentId, :premiseId)";
			
		}
		
		else if(premiseBindingType.code.equals(CommonConstants.PremiseBindingTypes.MPO.code)){
			sql = "insert into MPO_PREMISE_JT (MPO_MPO_ID, MISSEDPREMISES_CLAIM_ID) values (:parentId, :premiseId)";
		}
		else if(premiseBindingType.code.equals(CommonConstants.PremiseBindingTypes.OPPO.code)){
			sql = "insert into CLAIM_OPPO_CLAIM_JT (CLAIM_CLAIM_ID, OPPOSITECLAIMS_CLAIM_ID) values (:parentId, :premiseId)";
		}
		
		Query query = session.createSQLQuery(sql);
		query.setParameter("parentId", parentId);
		query.setParameter("premiseId", premiseId);
		query.executeUpdate();
		
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
			ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
			claim.setOppositeClaims(oppositeClaims);
			ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
			claim.setMediaResources(mediaResources);
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
	     Hibernate.initialize(claim.getMediaResources());
	     
		
		for(Argument argument : claim.getArguments()){
			 Hibernate.initialize(argument.getFallacyDetails());
		     Hibernate.initialize(argument.getPremises());
		     Hibernate.initialize(argument.getStateHistory());
		     //for all the argument premises for which we dont want to load the data, just fill with empty data to avoid  lazyload exception
		     for(Claim premise : argument.getPremises()){
		 		ArrayList<Argument> premiseArguments = new ArrayList<Argument>();
		 		premise.setArguments(premiseArguments); 
		 		ArrayList<String> keywords = new ArrayList<String>();
		 		premise.setKeywords(keywords);
		 		ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
		 		premise.setOppositeClaims(oppositeClaims);
		 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
		 		premise.setMediaResources(mediaResources);
		     }

		     Hibernate.initialize(argument.getMissedPremiseObjections());
		     for(MissedPremiseObjection objection : argument.getMissedPremiseObjections()){
		    	 Hibernate.initialize(objection.getMissedPremises());
		    	 for(Claim premise : objection.getMissedPremises()){
		    		 //don't want to load all the sub-info, just leave blank
		    		 premise.setArguments(new ArrayList<Argument>());
		    		 premise.setKeywords(new ArrayList<String>());
		    		 premise.setOppositeClaims(new ArrayList<Claim>());
		    		 premise.setMediaResources(new ArrayList<MediaResource>());
		    	 }
		     }
		}
		
		Hibernate.initialize(claim.getOppositeClaims());
	     for(Claim oppo : claim.getOppositeClaims()){
	 		ArrayList<Argument> premiseArguments = new ArrayList<Argument>();
	 		oppo.setArguments(premiseArguments); 
	 		ArrayList<String> keywords = new ArrayList<String>();
	 		oppo.setKeywords(keywords);
	 		ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
	 		oppo.setOppositeClaims(oppositeClaims);
	 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
	 		oppo.setMediaResources(mediaResources);
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
			claim.setOppositeClaims(new ArrayList<Claim>());
	 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
	 		claim.setMediaResources(mediaResources);
		}
		
		
		session.close();
		sessionFactory.close();
		return searchedClaims;
		
	}

	private String generateSearchSql(List<String> words) {
		
		
		StringBuffer query = new StringBuffer();
		query.append(" select  claim.*, null as occurance \n");
		query.append(" from prototype.claim claim where claim_statement like concat('% ', :param0, ' %')\n");
		query.append(" or claim_statement like concat(:param0, ' %')\n");
		query.append(" or claim_statement like concat('% ', :param0, '_')\n");
		query.append(" or claim_statement like concat('% ', :param0)\n");
		query.append(" or claim_statement like :param0");
		query.append(" union \n");
		query.append("( \n");
		query.append("select claim.*, count(claim_id) as occurance from \n");
		query.append("( \n");
		for(int i = 1; i < words.size(); i++){
			query.append(" select  * from prototype.claim \n");
			query.append("where ( claim_statement like concat('% ', :param"+i+", ' %') \n");
			query.append("OR claim_statement like concat(:param"+i+", ' %') \n");
			query.append("OR claim_statement like concat('% ', :param"+i+") \n");
			query.append("OR claim_statement like concat('% ', :param"+i+", '_')) \n");
			query.append(" and claim_statement not like concat('% ', :param0, ' %') \n");
			if(i != words.size()-1){
				query.append(" union all \n");
			}
		}
		query.append(") claim \n");
		query.append("group by claim_id order by occurance desc \n");
		query.append(") \n");
		System.out.println(query.toString());
		return query.toString();
	}
	
	private String generateClaimRefSearchSql(List<String> words) {
		
		
		StringBuffer query = new StringBuffer();
		query.append(" select  claim.*, null as occurance \n");
		query.append(" from prototype.claim_ref claim where claim_statement like concat('% ', :param0, ' %')\n");
		query.append(" or claim_statement like concat(:param0, ' %')\n");
		query.append(" or claim_statement like concat('% ', :param0, '_')\n");
		query.append(" or claim_statement like concat('% ', :param0)\n");
		query.append(" or claim_statement like :param0");
		query.append(" union \n");
		query.append("( \n");
		query.append("select claim.*, count(claim_id) as occurance from \n");
		query.append("( \n");
		for(int i = 1; i < words.size(); i++){
			query.append(" select  * from prototype.claim_ref \n");
			query.append("where ( claim_statement like concat('% ', :param"+i+", ' %') \n");
			query.append("OR claim_statement like concat(:param"+i+", ' %') \n");
			query.append("OR claim_statement like concat('% ', :param"+i+") \n");
			query.append("OR claim_statement like concat('% ', :param"+i+", '_')) \n");
			query.append(" and claim_statement not like concat('% ', :param0, ' %') \n");
			if(i != words.size()-1){
				query.append(" union all \n");
			}
		}
		query.append(") claim \n");
		query.append("group by claim_id order by occurance desc \n");
		query.append(") \n");
		System.out.println(query.toString());
		return query.toString();
	}

	public Argument saveArgument(Argument arg) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		for(Claim premise : arg.getPremises()){
			session.saveOrUpdate(premise);
		}
		if(arg.getStateHistory() != null){
			for(ArgumentState state : arg.getStateHistory()){
				session.saveOrUpdate(state);
			}
		}
		if(arg.getFallacyDetails() == null){
			FallacyDetails fd = new FallacyDetails();
			arg.setFallacyDetails(fd);
		}
		session.saveOrUpdate(arg.getFallacyDetails());
		session.saveOrUpdate(arg);
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		System.out.println("saved the following argument: " + arg.getArgName());
		return arg;
		
	}

	public boolean deleteClaim(Integer claimId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Claim claim = getClaim(claimId);
		
		if(claim.getArguments().size() > 0
				|| claim.getOppositeClaims().size() > 0){
			System.out.println("Cannot delete claim \"" + claim.getClaimStatement() + "\" because it still contains arguments or opposite claims.");
			return false;
		}
		
		session.delete(claim);
		
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		System.out.println("Deleted the following claim: " + claim.getClaimStatement());
		return true;
		
	}

	public List<ClaimRef> getSearchedClaimRefs(List<String> words) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		ArrayList <ClaimRef> searchedClaimRefs = new ArrayList<ClaimRef>();
		String sql = generateClaimRefSearchSql(words);
		Query query = session.createSQLQuery(sql).addEntity(ClaimRef.class);
		for (int i=0; i<words.size(); i++){
			query.setString("param"+i,words.get(i));
		}
		System.out.println(query.toString());
		searchedClaimRefs = (ArrayList<ClaimRef>) query.list();
		for(ClaimRef claimRef : searchedClaimRefs){
			System.out.println(claimRef.getClaimStatement());
		}
		
		
		session.close();
		sessionFactory.close();
		return searchedClaimRefs;
	}
	
//	public void linkOppositeClaim(int claimId, int oppositeClaimId){
//		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		Query query = session.createSQLQuery("select OPPOSITE_CLAIM_ID from OPPOSITE_CLAIMS where CLAIM_ID = "
//				+ " and OPPOSITE_CLAIM_ID = ").addEntity(Integer.class);
//		List <Integer> oppositeClaims =  (ArrayList<Integer>) query.list();
//		
//		if(oppositeClaims.size() == 0){
//			Query saveQuery = session.createSQLQuery("insert into Prototype.OPPOSITE_CLAIMS"
//					+ " CLAIM_ID = "
//					+ " OPPOSITE_CLAIM_ID = ");
//			saveQuery.executeUpdate();
//		}
//		session.close();
//		sessionFactory.close();
//	}
}
