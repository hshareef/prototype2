package com.prototype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="institution_member")
public class InstitutionMember {
	
	@Id
	@Column(name="institution_member_id")
	private Integer institutionMemberId;
	@OneToOne
	private Person person;
	@Column(name="position_id")
	private Integer positionId; //can be ceo, president, other, etc.
	@Column(name="other_position")
	private String otherPosition; //if positionId is for other, specify it in this string
	public Integer getPositionId() {
		return positionId;
	}
	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}
	public String getOtherPosition() {
		return otherPosition;
	}
	public void setOtherPosition(String otherPosition) {
		this.otherPosition = otherPosition;
	}
	
	

}
