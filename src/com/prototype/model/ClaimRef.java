package com.prototype.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ClaimRef {
	
	@Id
	@GeneratedValue
	private Integer claimRefId;
	private Integer claimId;
	private String claimStatement;
	
	public Integer getClaimRefId() {
		return claimRefId;
	}
	public void setClaimRefId(Integer claimRefId) {
		this.claimRefId = claimRefId;
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
	
	
}
