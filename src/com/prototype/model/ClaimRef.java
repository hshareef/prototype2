package com.prototype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="claim_ref")
public class ClaimRef {
	//we need this class as an entity because even if we dont save them to DB, we are creating these via hibernate on load
	
	@Id
	@GeneratedValue
	@Column(name="claim_ref_id")
	private Integer claimRefId;
	@Column(name="claim_id")
	private Integer claimId;
	@Column(name="claim_statement")
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
