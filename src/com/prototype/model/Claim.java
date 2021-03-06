package com.prototype.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
//@XmlRootElement(name="claim")
public class Claim {

	@Id
	@GeneratedValue
	@Column(name="claim_id")
	private Integer claimId;
	@Column(name="claim_statement")
	private String claimStatement;
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="claim_id")
	private List<Argument> arguments;
	
	@ManyToMany(targetEntity=Claim.class)
	@JoinTable(name="CLAIM_OPPO_CLAIM_JT")
	private List<Claim> oppositeClaims;
	
	@ManyToMany(targetEntity=MediaResource.class)
	@JoinTable(name="CLAIM_MEDIA_RESOURCE_JT")
	private List<MediaResource> mediaResources;
	
	@ElementCollection
	private List<String> keywords;
	
	@ElementCollection
	private List<Integer> categoryIds;
	
	@Column(name="original_owner_id")
	private Integer originalOwnerId;//id of the user who first created this claim
	@Column(name="original_owner_username")
	private String originalOwnerUsername;
	
	@Column(name="CREATED_TS")
	private Date createdTs;
	
	@Column(name="UPDATED_TS")
	private Date updatedTs;
	
	@Column(name="used_as_premise")
	private boolean usedAsPremise; //flag to help determine if the claim can be deleted or not NEED TO DELETE THIS FIELD!!
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="CLAIM_ID")
	private List<ClaimState> stateHistory;
	
	
//	@Transient
//	private boolean canDelete;
	
	//the claim needs to have a state history, because if it was created as part of a preliminary argument, then it should be preliminary
	//when the argument is published, then the claims should be moved to published state as well
	

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

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
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

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public List<ClaimState> getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(List<ClaimState> stateHistory) {
		this.stateHistory = stateHistory;
	}
	
	public ClaimState getCurrentState() {
		for(ClaimState state : this.stateHistory) {
			if (state.getCurrentFlag()) {
				return state;
			}
		}
		return null;
	}

	
//	public boolean isCanDelete() {
//		return canDelete;
//	}
//
//	public void setCanDelete(boolean canDelete) {
//		this.canDelete = canDelete;
//	}
	
	
	
}
