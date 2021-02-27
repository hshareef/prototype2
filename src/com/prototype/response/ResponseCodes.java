package com.prototype.response;

public enum ResponseCodes {

	SUCCESS(200, "SC", "Success"),
	FAILURE(500, "FL", "Failure");
	
	public int id;
	public String code;
	public String desc;
	
	ResponseCodes(int id, String code, String desc){
		this.id = id;
		this.code = code;
		this.desc = desc;
	}
}
