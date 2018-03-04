package com.prototype.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="missed_premise_objection")
public class MissedPremiseObjection {
	
	@Id
	@GeneratedValue
	@Column(name="missed_premise_objection_id")
	private Integer missedPremiseObjectionId;
	
	@Column(name="name")
	private String name;
	
	@ManyToMany(targetEntity=Claim.class)
	private List<Claim> missedPremises;
	
	//need to add a state history and a work flow
	

	public List<Claim> getMissedPremises() {
		return missedPremises;
	}
	public Integer getMissedPremiseObjectionId() {
		return missedPremiseObjectionId;
	}
	public void setMissedPremiseObjectionId(Integer missedPremiseObjectionId) {
		this.missedPremiseObjectionId = missedPremiseObjectionId;
	}
	public void setMissedPremises(List<Claim> missedPremises) {
		this.missedPremises = missedPremises;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	
}
