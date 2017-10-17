package com.prototype.model;

import java.sql.Timestamp;

public class ArgumentState {
	
	private Integer argumentStateId;
	private Timestamp stateTs;
	private Integer argumentStatusId;
	
	public Integer getArgumentStateId() {
		return argumentStateId;
	}
	public void setArgumentStateId(Integer argumentStateId) {
		this.argumentStateId = argumentStateId;
	}
	public Timestamp getStateTs() {
		return stateTs;
	}
	public void setStateTs(Timestamp stateTs) {
		this.stateTs = stateTs;
	}
	public Integer getArgumentStatusId() {
		return argumentStatusId;
	}
	public void setArgumentStatusId(Integer argumentStatusId) {
		this.argumentStatusId = argumentStatusId;
	}
	
	

}
