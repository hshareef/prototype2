package com.prototype.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prototype.common.ArgumentConstants;
import com.prototype.dao.ClaimDao;
import com.prototype.model.Argument;
import com.prototype.model.ArgumentState;
import com.prototype.model.Claim;
import com.prototype.model.ClaimRef;
import com.prototype.model.FallacyDetails;
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
	
	public List<Claim> getTopClaims(){
		return claimDao.getTopClaims();
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

	public void setOppositeClaim(Claim claim, Integer oppositeClaimId) {
		Claim oppoClaim = getClaim(oppositeClaimId);
		checkAndSetOppositeClaim(claim, oppoClaim);
		checkAndSetOppositeClaim(oppoClaim, claim);
		
	}
	
	public void checkAndSetOppositeClaim(Claim claim1, Claim claim2){
		boolean alreadyExists = false;
		for(Claim oppo : claim1.getOppositeClaims()){
			if(oppo.getClaimId() == claim2.getClaimId()){
				alreadyExists = true;
				break;
			}
		}
		if(!alreadyExists){
			claim1.getOppositeClaims().add(claim2);
			saveClaim(claim1);
		}
	}

	public Argument getArgumentTemplate() {
		Argument arg = new Argument();
		arg.setArgName("default name from template");
		arg.setEditable(true);
		arg.setSound(false);
		arg.setValid(false);
		arg.setValidCount(0);
		arg.setInvalidCount(0);
		arg.setFallacyDetails(new FallacyDetails());
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
	
}
