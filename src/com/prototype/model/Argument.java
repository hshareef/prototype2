package com.prototype.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="Argument")
@Entity
public class Argument {
	
	@Id
	@GeneratedValue
	private Integer argumentId;
//	private Integer claimId;
	private String argName;
	@ElementCollection(targetClass=Claim.class)
	private List<Claim> premises;

	//private ArrayList<String> keywords;
	
	public Integer getArgumentId() {
		return argumentId;
	}
	public void setArgumentId(Integer argumentId) {
		this.argumentId = argumentId;
	}
//	public Integer getClaimId() {
//		return claimId;
//	}
//	public void setClaimId(Integer claimId) {
//		this.claimId = claimId;
//	}
	

	public List<Claim> getPremises() {
		return premises;
	}
	public void setPremises(List<Claim> premises) {
		this.premises = premises;
	}
	public String getArgName() {
		return argName;
	}
	public void setArgName(String argName) {
		this.argName = argName;
	}

//	public ArrayList<String> getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(ArrayList<String> keywords) {
//		this.keywords = keywords;
//	}
	
	

}
