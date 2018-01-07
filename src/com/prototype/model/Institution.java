package com.prototype.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Institution {
	
	private String name;
	@ManyToOne
	private Person ceo;
	@Id	
	@GeneratedValue
	private Integer institutionId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Person getCeo() {
		return ceo;
	}
	public void setCeo(Person ceo) {
		this.ceo = ceo;
	}
	public Integer getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	
}
