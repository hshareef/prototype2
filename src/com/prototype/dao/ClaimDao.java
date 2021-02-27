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

public class ClaimDao {
	
	public Claim saveClaim(Claim claim){
		//List<PremiseBindingParameters> premisesToBind = new ArrayList<PremiseBindingParameters>();
		Set<Integer> premisesToBindIds = new HashSet<Integer>();
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		if (claim.getStateHistory() != null) {
			for(ClaimState state : claim.getStateHistory()) {
				session.saveOrUpdate(state);
			}
		}
		for(Argument arg : claim.getArguments()){
			for(Claim premise : arg.getPremises()){
				if(premise.getClaimId() == null){
					//Claim premiseClaim = new Claim();
					//premiseClaim.setClaimStatement(premise.getClaimStatement());
					//session.save(premiseClaim);
					//premise.setClaimId(premiseClaim.getClaimId());
					premise.setUsedAsPremise(true);
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
		if(claim.getClaimId() == null){
			claim.setCreatedTs(Calendar.getInstance().getTime());
			claim.setUpdatedTs(claim.getCreatedTs());
		}
		else{
			claim.setUpdatedTs(Calendar.getInstance().getTime());
		}
		session.saveOrUpdate(claim);
		session.getTransaction().commit();
		saveMpoPremiseOrders(claim, session);
		List<PremiseBindingParameters> premisesToBind = getAllBindingParameters(premisesToBindIds, claim);
		bindAllPremises(premisesToBind, session);
		session.close();
		sessionFactory.close();
		System.out.println("saved the following claim: " + claim.getClaimStatement());
		return claim;
	}
	
	private void saveMpoPremiseOrders(Claim claim, Session session) {
		if(claim != null && claim.getArguments() != null){
			for(Argument arg : claim.getArguments()){
				if(arg.getMissedPremiseObjections() != null && arg.getMissedPremiseObjections().size() > 0){
					for(MissedPremiseObjection mpo : arg.getMissedPremiseObjections()){
						if(mpo.getAllMpoPremises() != null && mpo.getPremiseOrder() == null){
							session.beginTransaction();
							mpo.setPremiseOrder(new ArrayList<PremiseOrderWrapper>());
							for(int i = 0 ; i < mpo.getAllMpoPremises().size() ; i++){
								PremiseOrderWrapper order = new PremiseOrderWrapper();
								if(mpo.getAllMpoPremises().get(i).getClaimId() != null){
									order.setClaimId(mpo.getAllMpoPremises().get(i).getClaimId());
								}
								else{
									for(Claim premise : mpo.getMissedPremises()){
										if(premise.getClaimStatement().equals(mpo.getAllMpoPremises().get(i).getClaimStatement())){
											order.setClaimId(premise.getClaimId());
										}
									}
								}
								order.setMissedPremiseObjectionId(mpo.getMissedPremiseObjectionId());
								order.setSequenceNumber(i);
								session.saveOrUpdate(order);
							}
							session.getTransaction().commit();
						}
					}
				}
			}
		}
		
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
	
	public List<Claim> getTopClaims(int categoryId){
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Claim> topClaims = new ArrayList<Claim>();
		System.out.println("Hibernate attempting to load the claim...");
		
		Query query = session.createSQLQuery(getTopClaimsByCategoryQuery()).addEntity(Claim.class);
		query.setInteger("categoryId", categoryId);

		topClaims = query.list();
		for(Claim claim : topClaims){
			System.out.println(claim.getClaimStatement());
			ArrayList<Argument> arguments = new ArrayList<Argument>();
			claim.setArguments(arguments);
			ArrayList <String> keywords = new ArrayList<String>();
			claim.setKeywords(keywords);
			claim.setCategoryIds(null);
			ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
			claim.setOppositeClaims(oppositeClaims);
			ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
			claim.setMediaResources(mediaResources);
			ArrayList <ClaimState> stateHistory = new ArrayList<ClaimState>();
			claim.setStateHistory(stateHistory);
		}
		
		
		session.close();
		sessionFactory.close();
		return topClaims;
	}
	
	public String getAllClaimsQuery(){
		return "select * from claim";
	}
	
	//currently returns all the claims, need to somehow sort by importance, maybe by views?
	public String getTopClaimsByCategoryQuery(){
		//maybe instead of select * we can select id and statement?
		StringBuffer query = new StringBuffer("select clm.*");
		query.append(" from Prototype.Claim clm");
		query.append(" join Prototype.claim_categoryIds cci on clm.claim_id = cci.claim_claim_id");
		query.append(" join prototype.claim_state cs on cs.claim_id = clm.claim_id and cs.current_flag = 1 and cs.claim_status_id = 2 \n");//only want published claims
		query.append(" where categoryIds = :categoryId");
		query.append(" order by created_ts");
		return query.toString();
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
	     Hibernate.initialize(claim.getCategoryIds());
	     Hibernate.initialize(claim.getMediaResources());
	     Hibernate.initialize(claim.getStateHistory());
		
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
		 		premise.setCategoryIds(null);
		 		ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
		 		premise.setOppositeClaims(oppositeClaims);
		 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
		 		premise.setMediaResources(mediaResources);
	    		premise.setStateHistory(new ArrayList<ClaimState>());
		     }

		     Hibernate.initialize(argument.getMissedPremiseObjections());
		     for(MissedPremiseObjection objection : argument.getMissedPremiseObjections()){
		    	 Hibernate.initialize(objection.getMissedPremises());
		    	 Hibernate.initialize(objection.getPremiseOrder());
		    	 Hibernate.initialize(objection.getStateHistory());
		    	 for(Claim premise : objection.getMissedPremises()){
		    		 //don't want to load all the sub-info, just leave blank
		    		 premise.setArguments(new ArrayList<Argument>());
		    		 premise.setKeywords(new ArrayList<String>());
		    		 premise.setCategoryIds(null);
		    		 premise.setOppositeClaims(new ArrayList<Claim>());
		    		 premise.setMediaResources(new ArrayList<MediaResource>());
		    		 premise.setStateHistory(new ArrayList<ClaimState>());
		    	 }
		     }
		}
		
		Hibernate.initialize(claim.getOppositeClaims());
	     for(Claim oppo : claim.getOppositeClaims()){
	 		ArrayList<Argument> premiseArguments = new ArrayList<Argument>();
	 		oppo.setArguments(premiseArguments); 
	 		ArrayList<String> keywords = new ArrayList<String>();
	 		oppo.setKeywords(keywords);
	 		oppo.setCategoryIds(null);
	 		ArrayList <Claim> oppositeClaims = new ArrayList<Claim>();
	 		oppo.setOppositeClaims(oppositeClaims);
	 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
	 		oppo.setMediaResources(mediaResources);
	 		oppo.setStateHistory(new ArrayList<ClaimState>());
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
			claim.setCategoryIds(null);
			ArrayList <String> keywords = new ArrayList<String>();
			claim.setKeywords(keywords);
			claim.setOppositeClaims(new ArrayList<Claim>());
	 		ArrayList <MediaResource> mediaResources = new ArrayList<MediaResource>();
	 		claim.setMediaResources(mediaResources);
	 		claim.setStateHistory(new ArrayList<ClaimState>());
		}
		
		
		session.close();
		sessionFactory.close();
		return searchedClaims;
		
	}

	private String generateSearchSql(List<String> words) {
		
		
		StringBuffer query = new StringBuffer();
		query.append(" select  claim.*, null as occurance \n");
		query.append(" from prototype.claim claim \n");
		query.append(" join prototype.claim_state cs on cs.claim_id = claim.claim_id and cs.current_flag = 1 and cs.claim_status_id = 2 \n");//only want published claims
		query.append(" where claim_statement like concat('% ', :param0, ' %')\n");
		query.append(" or claim_statement like concat(:param0, ' %')\n");
		query.append(" or claim_statement like concat('% ', :param0, '_')\n");
		query.append(" or claim_statement like concat('% ', :param0)\n");
		query.append(" or claim_statement like :param0");
		query.append(" union \n");
		query.append("( \n");
		query.append("select claim.*, count(claim_id) as occurance from \n");
		query.append("( \n");
		for(int i = 1; i < words.size(); i++){
			query.append(" select  claim.* from prototype.claim \n");
			query.append(" join prototype.claim_state cs on cs.claim_id = claim.claim_id and cs.current_flag = 1 and cs.claim_status_id = 2 \n");//only want published claims
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

	public List<Claim> getUserClaims(Integer userId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Claim> userClaims = new ArrayList<Claim>();
		System.out.println("Hibernate attempting to load the claims for user " + userId + "...");
		
		Query query = session.createSQLQuery(getClaimsByUserQuery()).addEntity(Claim.class);
		query.setInteger("userId", userId);

		userClaims = query.list();
		for(Claim claim : userClaims){
			System.out.println(claim.getClaimStatement());
		    Hibernate.initialize(claim.getStateHistory());
			claim.setArguments(new ArrayList<Argument>());
			claim.setKeywords(new ArrayList<String>());
			Hibernate.initialize(claim.getCategoryIds());
			claim.setOppositeClaims(new ArrayList<Claim>());
			claim.setMediaResources(new ArrayList<MediaResource>());
		}
		session.close();
		sessionFactory.close();
		return userClaims;
	}

	private String getClaimsByUserQuery() {
		//maybe instead of select * we can select id and statement?
		StringBuffer query = new StringBuffer("select clm.*");
		query.append(" from Prototype.Claim clm");
		query.append(" join prototype.claim_state cs on cs.claim_id = clm.claim_id and cs.current_flag = 1 and cs.claim_status_id in (1,2) \n");//only want pending or published claims
		query.append(" where clm.original_owner_id = :userId");
		query.append(" order by created_ts desc");
		return query.toString();
	}

	//loads the claims that contain arguments from a given user
	public List<Claim> getClaimsForUserArgs(Integer userId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Claim> userClaims = new ArrayList<Claim>();
		System.out.println("Hibernate attempting to load the claims for user " + userId + "...");
		
		Query query = session.createSQLQuery(getClaimsForUserArgsQuery()).addEntity(Claim.class);
		query.setInteger("userId", userId);

		userClaims = query.list();
		for(Claim claim : userClaims){
			System.out.println(claim.getClaimStatement());
		    Hibernate.initialize(claim.getStateHistory());
		    Hibernate.initialize(claim.getArguments());//need to see if we can only load for user, not all arguments
		    for (Argument argument : claim.getArguments()) {
		    	//argument.setPremises(new ArrayList<Claim>());
		    	Hibernate.initialize(argument.getStateHistory());
		    	argument.setMissedPremiseObjections(new ArrayList<MissedPremiseObjection>());
		    	Hibernate.initialize(argument.getPremises());
		    	for (Claim premise : argument.getPremises()) {
		    		premise.setArguments(new ArrayList<Argument>());
		    		Hibernate.initialize(premise.getStateHistory());
		    		//premise.setStateHistory(new ArrayList<ClaimState>());
		    		premise.setOppositeClaims(new ArrayList<Claim>());
		    		premise.setCategoryIds(new ArrayList<Integer>());
		    		premise.setKeywords(new ArrayList<String>());
		    		premise.setMediaResources(new ArrayList<MediaResource>());
		    	}
		    }
			claim.setKeywords(new ArrayList<String>());
			claim.setCategoryIds(null);
			claim.setOppositeClaims(new ArrayList<Claim>());
			claim.setMediaResources(new ArrayList<MediaResource>());
		}
		session.close();
		sessionFactory.close();
		return userClaims;		
	}

	private String getClaimsForUserArgsQuery() {
		StringBuffer query = new StringBuffer("select distinct clm.*");
		query.append(" from Prototype.Claim clm");
		query.append(" join prototype.claim_state cs on cs.claim_id = clm.claim_id and cs.current_flag = 1 and cs.claim_status_id = 2 \n");//only want published claims
		query.append(" join prototype.argument ag on ag.claim_id = clm.claim_id and ag.owner_id = :userId \n");
		query.append(" join prototype.argument_state ags on ags.argument_id = ag.argument_id and ags.current_flag = 1 and ags.argument_status_id in (1,2) \n");//only want pending or published arguments (no deleted)
		query.append(" order by created_ts desc");
		return query.toString();
	}

	public List<ClaimRef> getClaimRefsForUser(Integer userId) {
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<ClaimRef> userClaims = new ArrayList<ClaimRef>();
		System.out.println("Hibernate attempting to load the claims for user " + userId + "...");
		
		Query query = session.createSQLQuery(getClaimsForUserQuery()).addEntity(ClaimRef.class);
		query.setInteger("userId", userId);

		userClaims = query.list();
		session.close();
		sessionFactory.close();
		return userClaims;
	}

	private String getClaimsForUserQuery() {
		StringBuffer query = new StringBuffer("select clm.*");
		query.append(" from Prototype.Claim clm");
		query.append(" join prototype.claim_state cs on cs.claim_id = clm.claim_id and cs.current_flag = 1 and cs.claim_status_id = 2 \n");//only want published claims
		query.append(" join prototype.argument ag on ag.claim_id = clm.claim_id \n");
		query.append(" join prototype.argument_state ags on ags.argument_id = ag.argument_id and ags.current_flag = 1 and ags.argument_status_id in (1,2) \n");//only want pending or published arguments
		query.append(" where ag.owner_id = :userId");
		query.append(" order by created_ts desc");
		return query.toString();
	}

//	public boolean claimUsedAsPremise(Integer claimId) {
//		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		System.out.println("Determinging if " + claimId + " used as a premise...");
//		Query query = session.createSQLQuery(getClaimUsedAsPremiseQuery());
//		query.setInteger("claimId", claimId);
//		List result = query.list();
//		session.close();
//		sessionFactory.close();
//		return result.size() > 0;
//	}
//	
//	public String getClaimUsedAsPremiseQuery(){
//		return " select 1 from Prototype.argument_premise_jt where premises_claim_id = :claimId limit 1";
//	}
	
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
