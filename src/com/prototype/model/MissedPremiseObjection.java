package com.prototype.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	 
	 @OneToMany
	 @JoinColumn(name="MPO_ID")
	 private List<PremiseOrderWrapper> premiseOrder;
	 
	 @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	 @JoinColumn(name="MPO_ID")
	 private List<MpoState> stateHistory;
	 
	 @Transient
	 private User owner;
	 
	 @Column(name="OWNER_ID")
	 private Integer ownerId;
	 
	 @Transient
	 private List<Claim> allMpoPremises; //this is used just for tracking the order of unsaved missed premises
	
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

	public List<PremiseOrderWrapper> getPremiseOrder() {
		return premiseOrder;
	}
	public void setPremiseOrder(List<PremiseOrderWrapper> premiseOrder) {
		this.premiseOrder = premiseOrder;
	}
	
	public List<MpoState> getStateHistory() {
		return stateHistory;
	}
	public void setStateHistory(List<MpoState> stateHistory) {
		this.stateHistory = stateHistory;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public List<Claim> getAllMpoPremises() {
		return allMpoPremises;
	}
	public void setAllMpoPremises(List<Claim> allMpoPremises) {
		this.allMpoPremises = allMpoPremises;
	}
	
}
