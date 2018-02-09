package com.prototype.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Institution {
	
	private String name;
	@ManyToMany(targetEntity=Person.class)
	private List<InstitutionMember> people;
	
	@Id	
	@GeneratedValue
	@Column(name="institution_id")
	private Integer institutionId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<InstitutionMember> getPeople() {
		return people;
	}
	public void setPeople(List<InstitutionMember> people) {
		this.people = people;
	}
	public Integer getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	
}
