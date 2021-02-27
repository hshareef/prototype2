package com.prototype.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name="claim_state")
public class ClaimState {
	
	@Id
	@GeneratedValue
	@Column(name="claim_state_id")
	private Integer claimStateId;
	@Column(name="created_ts")
	private Timestamp createdTs;
	@Column(name="updated_ts")
	private Timestamp updatedTs;
	@Column(name="claim_status_id")
	private Integer claimStatusId;
	@Column(name="current_flag")
	private Boolean currentFlag;
	public Integer getClaimStateId() {
		return claimStateId;
	}
	public void setClaimStateId(Integer claimStateId) {
		this.claimStateId = claimStateId;
	}
	public Timestamp getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Timestamp createdTs) {
		this.createdTs = createdTs;
	}
	public Timestamp getUpdatedTs() {
		return updatedTs;
	}
	public void setUpdatedTs(Timestamp updatedTs) {
		this.updatedTs = updatedTs;
	}
	public Integer getClaimStatusId() {
		return claimStatusId;
	}
	public void setClaimStatusId(Integer claimStatusId) {
		this.claimStatusId = claimStatusId;
	}
	public Boolean getCurrentFlag() {
		return currentFlag;
	}
	public void setCurrentFlag(Boolean currentFlag) {
		this.currentFlag = currentFlag;
	}
	
	

}
