package com.prototype.common;

public class ArgumentConstants {
	
	public enum States{
		
		PRELIM(1, "PR", "Preliminary"),
		PUBLISHED(2, "PB", "Published");
		
		private int id;
		private String code;
		private String desc;
		
		States(int id, String code, String desc){
			this.id = id;
			this.code = code;
			this.desc = desc;
		}
	}

}
