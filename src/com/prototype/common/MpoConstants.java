package com.prototype.common;

public class MpoConstants {

	public enum States{
		
		PRELIM(1, "PR", "Preliminary"),
		PUBLISHED(2, "PB", "Published"),
		DELETED(3, "DE", "Deleted");
		
		public int ID;
		public String CODE;
		public String DESC;
		
		States(int id, String code, String desc){
			this.ID = id;
			this.CODE = code;
			this.DESC = desc;
		}
	}
}
