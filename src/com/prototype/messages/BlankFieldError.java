package com.prototype.messages;

public class BlankFieldError extends Error {
	
	private String fieldName;
	
	public BlankFieldError(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String getMessage(){
		return "The field \"" + fieldName + "\" cannot be blank.";
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
}
