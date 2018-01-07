package com.prototype.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MediaResource {

	@Id
	@GeneratedValue
	private Integer mediaResourceId;
	private Integer resourceTypeId;
	private String url;
	private Date publishDate;
	@ManyToOne
	private Author author;
	@ManyToOne
	private Institution institution;
	//private List<ClaimRef> claims; 
	//private List<Integer> argumentIds;
	
	public Integer getResourceTypeId() {
		return resourceTypeId;
	}
	public void setResourceTypeId(Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Institution getInstitution() {
		return institution;
	}
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
//	public List<ClaimRef> getClaims() {
//		return claims;
//	}
//	public void setClaims(List<ClaimRef> claims) {
//		this.claims = claims;
//	}
//	public List<Integer> getArgumentIds() {
//		return argumentIds;
//	}
//	public void setArgumentIds(List<Integer> argumentIds) {
//		this.argumentIds = argumentIds;
//	}	
	
}
