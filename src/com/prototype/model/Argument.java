package com.prototype.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="Argument")
@Entity
@Table(name="ARGUMENT")
public class Argument {
	
	@Id
	@GeneratedValue
	@Column(name="ARGUMENT_ID")
	private Integer argumentId;
//	private Integer claimId;
	@Column(name="ARG_NAME")
	private String argName;
	
	 @ManyToOne
	 private Claim claim;//we can probably delete this
	 
	 @Transient
	 private Integer claimId;
	 
	 @Transient
	 private String claimStatement;
	 
	 //need this somewhere in the app
	 //https://stackoverflow.com/questions/5907501/when-annotating-a-class-with-component-does-this-mean-it-is-a-spring-bean-and
	 
	//@ElementCollection(targetClass=ClaimRef.class)
	@ManyToMany(targetEntity=Claim.class)
	@JoinTable(name="ARGUMENT_PREMISE_JT")
	private List<Claim> premises;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="ARGUMENT_ID")
	private List<MissedPremiseObjection> missedPremiseObjections;
	

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="ARGUMENT_ID")
	private List<ArgumentState> stateHistory;
	
	public List<ArgumentState> getStateHistory() {
		return stateHistory;
	}


	public void setStateHistory(List<ArgumentState> stateHistory) {
		this.stateHistory = stateHistory;
	}


	
	@Column(name="owner_id")
	private Integer ownerId;
	
	@Column(name="owner_username")
	private String ownerUsername;
	
	private Boolean editable;

	//private ArrayList<String> keywords;
	
	private Boolean valid;
	private Boolean sound;
	
	@Column(name="valid_count")
	private Integer validCount;
	
	@Column(name="invalid_count")
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


	public List<MissedPremiseObjection> getMissedPremiseObjections() {
		return missedPremiseObjections;
	}


	public void setMissedPremiseObjections(
			List<MissedPremiseObjection> missedPremiseObjections) {
		this.missedPremiseObjections = missedPremiseObjections;
	}
	
	public Integer getClaimId() {
		return claimId;
	}


	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}


	public String getClaimStatement() {
		return claimStatement;
	}


	public void setClaimStatement(String claimStatement) {
		this.claimStatement = claimStatement;
	}


	public Claim getClaim() {
		return claim;
	}


	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	

//	public ArrayList<String> getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(ArrayList<String> keywords) {
//		this.keywords = keywords;
//	}
	
	//test source tree
	
	

}
