package com.prototype.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MPO")
public class MissedPremiseObjection {
	
	@Id
	@GeneratedValue
	@Column(name="MPO_ID")
	private Integer missedPremiseObjectionId;
	
	@Column(name="NAME")
	private String name;
	
	@ManyToMany(targetEntity=Claim.class)
	@JoinTable(name="MPO_PREMISE_JT")
	private List<Claim> missedPremises;
	
	 @ManyToOne
	 private Argument argument;
	
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
	public Argument getArgument() {
		return argument;
	}
	public void setArgument(Argument argument) {
		this.argument = argument;
	}

	
	
	
}
