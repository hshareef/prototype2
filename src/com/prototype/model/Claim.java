package com.prototype.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
//@XmlRootElement(name="claim")
public class Claim {

	@Id
	@GeneratedValue
	private Integer claimId;
	private String claimStatement;
	@ElementCollection(targetClass=Argument.class)
	private List<Argument> arguments;
	
	@ElementCollection(targetClass=Claim.class)
	private List<Claim> oppositeClaims;
	
	
	@ElementCollection
	private List<String> keywords;
	
	private Integer originalOwnerId;//id of the user who first created this claim
	private String originalOwnerUsername;
	
	private boolean usedAsPremise; //flag to help determine if the claim can be deleted or not
	


	public Claim()
	{
		
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

	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Integer getOriginalOwnerId() {
		return originalOwnerId;
	}

	public void setOriginalOwnerId(Integer originalOwnerId) {
		this.originalOwnerId = originalOwnerId;
	}

	public String getOriginalOwnerUsername() {
		return originalOwnerUsername;
	}

	public void setOriginalOwnerUsername(String originalOwnerUsername) {
		this.originalOwnerUsername = originalOwnerUsername;
	}

	public List<Claim> getOppositeClaims() {
		return oppositeClaims;
	}

	public void setOppositeClaims(List<Claim> oppositeClaims) {
		this.oppositeClaims = oppositeClaims;
	}

	public boolean isUsedAsPremise() {
		return usedAsPremise;
	}

	public void setUsedAsPremise(boolean usedAsPremise) {
		this.usedAsPremise = usedAsPremise;
	}
	
}
