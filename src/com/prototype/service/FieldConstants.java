package com.prototype.service;

public class FieldConstants {
	public enum ClaimFields{
		ClaimStatement("Claim Statement");

		public String value;
		
		ClaimFields(String value){
			this.value = value;
		}
	}
	
	public enum ArgumentFields{
		ArgumentName("Argument Name"),
		ArgumentPremise("Premise");
		
		public String value;
		
		ArgumentFields(String value){
			this.value = value;
		}	
	}
}
