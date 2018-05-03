package com.prototype.common;

public class CommonConstants {
	
	public enum PremiseBindingTypes{
		
		ARG(1, "ARG", "Argument"),
		MPO(2, "MPO", "Missed Premise Objection"),
		OPPO(3, "OPPO", "Opposite Claims");
		
		public int id;
		public String code;
		public String desc;
		
		PremiseBindingTypes(int id, String code, String desc){
			this.id = id;
			this.code = code;
			this.desc = desc;
		}
	}

}
