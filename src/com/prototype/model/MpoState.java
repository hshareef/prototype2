package com.prototype.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MPO_STATE")
public class MpoState {

	@Id
	@GeneratedValue
	@Column(name="MPO_STATE_ID")
	private Integer mpoStateId;
	
	@Column(name="MPO_STATUS_ID")
	private Integer mpoStatusId;
	
	@Column(name="MPO_ID")
	private Integer mpoId;
	
	@Column(name="CURRENT_FLAG")
	private boolean currentFlag;
	
	@Column(name="CREATED_USER")
	private String createdUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_TS",
		    columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
	private Date createdTs;
	
	@Column(name="UPDATED_USER")
	private String updatedUser;
	
	@Column(name="UPDATED_TS",
		    columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
	private Timestamp updatedTs;
	
	public Integer getMpoStateId() {
		return mpoStateId;
	}
	public void setMpoStateId(Integer mpoStateId) {
		this.mpoStateId = mpoStateId;
	}
	public Integer getMpoStatusId() {
		return mpoStatusId;
	}
	public void setMpoStatusId(Integer mpoStatusId) {
		this.mpoStatusId = mpoStatusId;
	}
	public Integer getMpoId() {
		return mpoId;
	}
	public void setMpoId(Integer mpoId) {
		this.mpoId = mpoId;
	}
	public boolean isCurrentFlag() {
		return currentFlag;
	}
	public void setCurrentFlag(boolean currentFlag) {
		this.currentFlag = currentFlag;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public Timestamp getUpdatedTs() {
		return updatedTs;
	}
	public void setUpdatedTs(Timestamp updatedTs) {
		this.updatedTs = updatedTs;
	}
	
	
}
