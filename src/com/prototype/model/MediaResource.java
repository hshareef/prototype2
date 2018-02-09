package com.prototype.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="media_resource")
public class MediaResource {

	@Id
	@GeneratedValue
	@Column(name="media_resource_id")
	private Integer mediaResourceId;
	@Column(name="resource_type_id")
	private Integer resourceTypeId;
	private String url;
	@Column(name="publish_date")
	private Date publishDate;
	@ManyToOne
	private Author author;
	@ManyToOne
	private Institution institution;
	private String title;
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
	public Integer getMediaResourceId() {
		return mediaResourceId;
	}
	public void setMediaResourceId(Integer mediaResourceId) {
		this.mediaResourceId = mediaResourceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
