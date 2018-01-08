package com.prototype.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="Argument")
@Entity
public class Argument {
	
	@Id
	@GeneratedValue
	private Integer argumentId;
//	private Integer claimId;
	private String argName;
	//@ElementCollection(targetClass=ClaimRef.class)
	@ManyToMany(targetEntity=Claim.class)
	private List<Claim> premises;
	
	public List<ArgumentState> getStateHistory() {
		return stateHistory;
	}


	public void setStateHistory(List<ArgumentState> stateHistory) {
		this.stateHistory = stateHistory;
	}


	@ElementCollection(targetClass=ArgumentState.class)
	private List<ArgumentState> stateHistory;
	
	private Integer ownerId;
	private String ownerUsername;
	
	private Boolean editable;

	//private ArrayList<String> keywords;
	
	private Boolean valid;
	private Boolean sound;
	
	private Integer validCount;
	private Integer invalidCount;
	
	@OneToOne(targetEntity = FallacyDetails.class)
	private FallacyDetails fallacyDetails;
	
	//private ArgumentWrapper arguments;
//	private ArrayList<String> keywords;
//	private ArrayList<Claim> similarClaims;
//	private ArrayList<Claim> oppositeClaims;
	
	public void determineValidity(){
		if(validCount == null){
			validCount = 0;
		}
		if(invalidCount == null){
			invalidCount = 0;
		}
		if(validCount + invalidCount == 0){
			this.valid = false;
		}
		else if(validCount/(validCount + invalidCount)>0.9){
			this.valid = true;
		}
		else
			this.valid = false;
	}


	public Integer getValidCount() {
		return validCount;
	}

	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}

	public Integer getInvalidCount() {
		return invalidCount;
	}

	public void setInvalidCount(Integer invalidCount) {
		this.invalidCount = invalidCount;
	}


	
	public Integer getArgumentId() {
		return argumentId;
	}
	public void setArgumentId(Integer argumentId) {
		this.argumentId = argumentId;
	}
//	public Integer getClaimId() {
//		return claimId;
//	}
//	public void setClaimId(Integer claimId) {
//		this.claimId = claimId;
//	}
	

	public List<Claim> getPremises() {
		return premises;
	}
	public void setPremises(List<Claim> premises) {
		this.premises = premises;
	}
	public String getArgName() {
		return argName;
	}
	public void setArgName(String argName) {
		this.argName = argName;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerUsername() {
		return ownerUsername;
	}
	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}


	public Boolean getValid() {
		return valid;
	}


	public void setValid(Boolean valid) {
		this.valid = valid;
	}


	public Boolean getSound() {
		return sound;
	}


	public void setSound(Boolean sound) {
		this.sound = sound;
	}


	public FallacyDetails getFallacyDetails() {
		return fallacyDetails;
	}


	public void setFallacyDetails(FallacyDetails fallacyDetails) {
		this.fallacyDetails = fallacyDetails;
	}
	
	
	
	

//	public ArrayList<String> getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(ArrayList<String> keywords) {
//		this.keywords = keywords;
//	}
	
	//test source tree
	
	

}
