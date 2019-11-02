package com.prototype.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prototype.common.ArgumentConstants;
import com.prototype.common.MpoConstants;
import com.prototype.dao.ClaimDao;
import com.prototype.model.Argument;
import com.prototype.model.ArgumentState;
import com.prototype.model.Claim;
import com.prototype.model.ClaimRef;
import com.prototype.model.FallacyDetails;
import com.prototype.model.MissedPremiseObjection;
import com.prototype.model.MpoState;
import com.prototype.validators.ClaimValidator;

public class ClaimService {
	
	ClaimDao claimDao = new ClaimDao();
	
	//need to autowire
	ClaimValidator claimValidator = new ClaimValidator();
	
	public Claim saveClaim(Claim claim){
		if(claimValidator.validateClaim(claim).size()==0){
			for(Argument arg : claim.getArguments()){
				arg.determineValidity();
			}
			addPrelimStates(claim);
			claim = claimDao.saveClaim(claim);
			return claim;
		}
		else{
			System.out.println("Claim invlid, cannot save.");
			return null;
		}
	}
	
	public Argument saveArgument(Argument arg){
		return claimDao.saveArgument(arg);
	}
	
	public List<Claim> getTopClaims(int categoryId){
		return claimDao.getTopClaims(categoryId);
	}

	public Claim getClaim(Integer claimId) {
		return claimDao.getClaim(claimId);
	}

	public List<Claim> getSearchedClaims(String searchString) {
		List<String> words = new ArrayList<String>(Arrays.asList(searchString.split(" ")));
		words.add(0, searchString);
		if(!checkSingleStopword(searchString)){
			words = removeStopwords(words);
		}
		return claimDao.getSearchedClaims(words);
	}
	
	public List<ClaimRef> getSearchedClaimRefs(String searchString) {
		List<String> words = new ArrayList<String>(Arrays.asList(searchString.split(" ")));
		words.add(0, searchString);
		if(!checkSingleStopword(searchString)){
			words = removeStopwords(words);
		}
		return claimDao.getSearchedClaimRefs(words);
	}
	
	public List<String> removeStopwords(List<String> words){
		//need to redo this - have a table of stopwords and then load them to a hashmap
		//get stopwords here:   http://xpo6.com/list-of-english-stop-words/
		Map stopWords = new HashMap<String, Integer>();
		stopWords.put("a", 1);
		stopWords.put("the", 1);
		stopWords.put("an", 1);
		stopWords.put("it", 1);
		stopWords.put("he", 1);
		stopWords.put("she", 1);
		stopWords.put("him", 1);
		stopWords.put("her", 1);
		stopWords.put("they", 1);
		stopWords.put("them", 1);
		stopWords.put("so", 1);
		stopWords.put("then", 1);
		for(int i = 0; i < words.size(); i++){
			if(stopWords.get(words.get(i)) != null) {
				words.remove(i);
				i--;
			}
		}
		return words;
	}
	
	public boolean checkSingleStopword(String searchString){
		//need to redo this - have a table of stopwords and then load them to a hashmap
		//get stopwords here:   http://xpo6.com/list-of-english-stop-words/
		Map stopWords = new HashMap<String, Integer>();
		stopWords.put("a", 1);
		stopWords.put("the", 1);
		stopWords.put("an", 1);
		stopWords.put("it", 1);
		stopWords.put("he", 1);
		stopWords.put("she", 1);
		stopWords.put("him", 1);
		stopWords.put("her", 1);
		stopWords.put("they", 1);
		stopWords.put("them", 1);
		stopWords.put("so", 1);
		stopWords.put("then", 1);
		if(stopWords.get(searchString) != null){
			return true;
		}
		return false;
	}

	public Claim setOppositeClaim(Claim claim, Integer oppositeClaimId) {
		Claim oppoClaim = getClaim(oppositeClaimId);
		checkAndSetOppositeClaim(claim, oppoClaim);
		checkAndSetOppositeClaim(oppoClaim, claim);
		clearOppositeClaimOpposites(claim);//this is to avoid an infinite loop, as the claim A will have an opposite, B, which has an opposite (A), which has opposite (B) ...
		return claim;
		
	}
	
	public Claim checkAndSetOppositeClaim(Claim claim1, Claim claim2){
		boolean alreadyExists = false;
		for(Claim oppo : claim1.getOppositeClaims()){
			if(oppo.getClaimId() == claim2.getClaimId()){
				alreadyExists = true;
				break;
			}
		}
		if(!alreadyExists){
			claim1.getOppositeClaims().add(claim2);
			claim1 = saveClaim(claim1);
		}
		return claim1;
	}

	private void clearOppositeClaimOpposites(Claim claim) {
		//clear all info except opposite claim id and claim statement
		for(Claim oppo : claim.getOppositeClaims()){
			oppo.setArguments(null);
			oppo.setKeywords(null);
			oppo.setMediaResources(null);
			oppo.setOppositeClaims(null);
			oppo.setOriginalOwnerId(null);
			oppo.setOriginalOwnerUsername(null);
			oppo.setUsedAsPremise(false);
		}
		
	}

	public Argument getArgumentTemplate() {
		Argument arg = new Argument();
		arg.setArgName("");
		arg.setEditable(true);
		arg.setSound(false);//need to remove
		arg.setValid(false);//need to remove
		arg.setValidCount(0);//need to remove
		arg.setInvalidCount(0);//need to remove
		arg.setFallacyDetails(new FallacyDetails());//need to remove
		arg.setPremises(new ArrayList<Claim>());
		arg.setStateHistory(new ArrayList<ArgumentState>());
		addArgState(arg, ArgumentConstants.States.PRELIM.id);
		return arg;
	}
	
	private void addArgState(Argument arg, Integer statusId){
		//first set all current flags to false (since the new state is going to be the current one)
		if(arg.getStateHistory() != null && arg.getStateHistory().size() > 0){
			for(ArgumentState state : arg.getStateHistory()){
				if(state.getCurrentFlag()){
					state.setCurrentFlag(false);
				}
			}
		}
		ArgumentState state = new ArgumentState();
		state.setArgumentStatusId(statusId);
		state.setCurrentFlag(true);
		//need to add createdTs and updatedTs as well!!
		arg.getStateHistory().add(state);
	}

	public Argument updateArgState(Argument argument, Integer targetStateId) {
		//can run rule checks here if needed
		
		addArgState(argument, targetStateId);
		argument = saveArgument(argument);
		return argument;
		
	}
	
	public boolean deleteClaim(Integer claimId){
		return claimDao.deleteClaim(claimId);
	}
	
	private void addPrelimStates(Claim claim){
		//add prelim state for claim if needed
		
		//add prelim state for arguments if needed
		
		//add prelim state for mpo's
		addMpoPrelimStates(claim);
	}
	
	private void addMpoPrelimStates(Claim claim){
		if(claim != null && claim.getArguments() != null && claim.getArguments().size() > 0){
			for(Argument arg : claim.getArguments()){
				if(arg.getMissedPremiseObjections() != null && arg.getMissedPremiseObjections().size() > 0){
					for(MissedPremiseObjection mpo : arg.getMissedPremiseObjections()){
						if(mpo.getStateHistory() == null || mpo.getStateHistory().size() == 0){
							MpoState state = new MpoState();
							state.setMpoId(mpo.getMissedPremiseObjectionId());
							state.setMpoStatusId(MpoConstants.States.PRELIM.ID);
							state.setCurrentFlag(true);
							//need to revisit this user audit info
							if(mpo.getOwner() != null && mpo.getOwner().getUsername() != null){
								state.setCreatedUser(mpo.getOwner().getUsername());
								state.setUpdatedUser(claim.getOriginalOwnerUsername());
							}
							if(mpo.getStateHistory() == null){
								List<MpoState> stateHistory = new ArrayList<MpoState>();
								stateHistory.add(state);
								mpo.setStateHistory(stateHistory);
							}
							else{
								mpo.getStateHistory().add(state);
							}
						}
					}
				}
			}
		}
	}
	
}
