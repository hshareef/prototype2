package com.prototype.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
//@XmlRootElement(name="claim")
public class Claim {

	@Id
	@GeneratedValue
	private Integer claimId;
	private String claimStatement;
	@ElementCollection(targetClass=Argument.class)
	private List<Argument> arguments;
	
	@ElementCollection
	private List<String> keywords;
	
	
	//private ArgumentWrapper arguments;
//	private ArrayList<String> keywords;
//	private ArrayList<Claim> similarClaims;
//	private ArrayList<Claim> oppositeClaims;
	
	public Claim()
	{
		
	}
	
	public Integer getClaimId() {
		return claimId;
	}
	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}
	public String getClaimStatement() {
		return claimStatement;
	}
	public void setClaimStatement(String claimStatement) {
		this.claimStatement = claimStatement;
	}
//	public ArrayList<String> getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(ArrayList<String> keywords) {
//		this.keywords = keywords;
//	}
//	public ArrayList<Claim> getSimilarClaims() {
//		return similarClaims;
//	}
//	public void setSimilarClaims(ArrayList<Claim> similarClaims) {
//		this.similarClaims = similarClaims;
//	}
//	public ArrayList<Claim> getOppositeClaims() {
//		return oppositeClaims;
//	}
//	public void setOppositeClaims(ArrayList<Claim> oppositeClaims) {
//		this.oppositeClaims = oppositeClaims;
//	}


	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	

//	public ArgumentWrapper getArguments() {
//		return arguments;
//	}
//
//	public void setArguments(ArgumentWrapper arguments) {
//		this.arguments = arguments;
//	}
	
	
	
	
}
