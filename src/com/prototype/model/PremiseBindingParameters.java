package com.prototype.model;

import com.prototype.common.CommonConstants.PremiseBindingTypes;

public class PremiseBindingParameters {

	Integer parentId;
	Integer premiseId;
	PremiseBindingTypes premiseBindingType;
	
	public PremiseBindingParameters (Integer parentId, Integer premiseId, PremiseBindingTypes premiseBindingType){
		this.parentId = parentId;
		this.premiseId = premiseId;
		this.premiseBindingType = premiseBindingType;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getPremiseId() {
		return premiseId;
	}
	public void setPremiseId(Integer premiseId) {
		this.premiseId = premiseId;
	}
	public PremiseBindingTypes getPremiseBindingType() {
		return premiseBindingType;
	}
	public void setPremiseBindingType(PremiseBindingTypes premiseBindingType) {
		this.premiseBindingType = premiseBindingType;
	}
	
	
	
}
