package com.prototype.common;

public class ArgumentConstants {
	
	public enum States{
		
		PRELIM(1, "PR", "Preliminary"),
		PUBLISHED(2, "PB", "Published"),
		DELETED(3, "DE", "Deleted");
		
		public int id;
		public String code;
		public String desc;
		
		States(int id, String code, String desc){
			this.id = id;
			this.code = code;
			this.desc = desc;
		}
	}

}
