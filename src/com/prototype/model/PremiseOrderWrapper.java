package com.prototype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PREMISE_ORDER")
public class PremiseOrderWrapper {
	
	@Id
	@GeneratedValue
	@Column(name="PREMISE_ORDER_ID")
	private Integer premiseOrderMapperId;
	
	@Column(name="MPO_ID")
	private Integer missedPremiseObjectionId;
	
	@Column(name="PREMISE_ID")
	private Integer claimId; //aka premise id
	
	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;
	
	
	public Integer getPremiseOrderMapperId() {
		return premiseOrderMapperId;
	}
	public void setPremiseOrderMapperId(Integer premiseOrderMapperId) {
		this.premiseOrderMapperId = premiseOrderMapperId;
	}
	public Integer getMissedPremiseObjectionId() {
		return missedPremiseObjectionId;
	}
	public void setMissedPremiseObjectionId(Integer missedPremiseObjectionId) {
		this.missedPremiseObjectionId = missedPremiseObjectionId;
	}
	public Integer getClaimId() {
		return claimId;
	}
	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	

}
