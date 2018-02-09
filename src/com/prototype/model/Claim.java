package com.prototype.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
//@XmlRootElement(name="claim")
public class Claim {

	@Id
	@GeneratedValue
	@Column(name="claim_id")
	private Integer claimId;
	@Column(name="claim_statement")
	private String claimStatement;
	@ElementCollection(targetClass=Argument.class)
	private List<Argument> arguments;
	
	@ManyToMany(targetEntity=Claim.class)
	private List<Claim> oppositeClaims;
	
	@ManyToMany(targetEntity=MediaResource.class)
	private List<MediaResource> mediaResources;
	
	@ElementCollection
	private List<String> keywords;
	
	@Column(name="original_owner_id")
	private Integer originalOwnerId;//id of the user who first created this claim
	@Column(name="original_owner_username")
	private String originalOwnerUsername;
	
	@Column(name="used_as_premise")
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

	public List<MediaResource> getMediaResources() {
		return mediaResources;
	}

	public void setMediaResources(List<MediaResource> mediaResources) {
		this.mediaResources = mediaResources;
	}
	
	
	
}
