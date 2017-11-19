package com.prototype.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ArgumentState {
	
	@Id
	@GeneratedValue
	private Integer argumentStateId;
	private Timestamp createdTs;
	private Timestamp updatedTs;
	private Integer argumentStatusId;
	private Boolean currentFlag;
	
	public Integer getArgumentStateId() {
		return argumentStateId;
	}
	public void setArgumentStateId(Integer argumentStateId) {
		this.argumentStateId = argumentStateId;
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
	public Integer getArgumentStatusId() {
		return argumentStatusId;
	}
	public void setArgumentStatusId(Integer argumentStatusId) {
		this.argumentStatusId = argumentStatusId;
	}
	public Boolean getCurrentFlag() {
		return currentFlag;
	}
	public void setCurrentFlag(Boolean currentFlag) {
		this.currentFlag = currentFlag;
	}
	
	

}
