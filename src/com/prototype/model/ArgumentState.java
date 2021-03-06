package com.prototype.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="argument_state")
public class ArgumentState {
	
	@Id
	@GeneratedValue
	@Column(name="argument_state_id")
	private Integer argumentStateId;
	@Column(name="created_ts")
	private Timestamp createdTs;
	@Column(name="updated_ts")
	private Timestamp updatedTs;
	@Column(name="argument_status_id")
	private Integer argumentStatusId;
	@Column(name="current_flag")
	private Boolean currentFlag;
	
	@ManyToOne
	private Argument argument;//delete this?
	
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
	public Argument getArgument() {
		return argument;
	}
	public void setArgument(Argument argument) {
		this.argument = argument;
	}
	
	

}
