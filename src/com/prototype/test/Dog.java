package com.prototype.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Dog {
	
	String name;
	int age;
	
	public Dog(){
		
	}
	
	public Dog(String name, int age){
		this.name = name;
		this.age = age;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getAge(){
		return age;
	}
	
	public void setAge(int age){
		this.age = age;
	}
	
	public String toString(){
		return getName() + " is " + getAge() + " years old.";
	}

}
