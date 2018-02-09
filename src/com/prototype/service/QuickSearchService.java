package com.prototype.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.prototype.dao.QuickSearchDao;
import com.prototype.model.Person;
import com.prototype.model.PotentialName;

public class QuickSearchService {
	
	QuickSearchDao quickSearchDao = new QuickSearchDao();

	//format of search string must be "lastname, firstname" or "firstname lastname" or "string"
	public List<Person> getSearchedPeople(String searchString) {
		List<PotentialName> potentialNames = getNames(searchString);
		List<Person> potentialPersons = quickSearchDao.getSearchedPeople(potentialNames);
		return potentialPersons;
	}
	
	public List<PotentialName> getNames(String searchString){
		List<PotentialName> potentialNames = new ArrayList<PotentialName>();
		int commaLoc = searchString.indexOf(',');
		//we have lastname, firstname format input
		if(commaLoc != -1){
			PotentialName name = new PotentialName();
			name.setFirstName(searchString.substring(commaLoc+1, searchString.length()-1).trim() + "%");
			name.setLastName(searchString.substring(0, commaLoc-1).trim());
			potentialNames.add(name);
		}
		//we have a space
		else if(searchString.indexOf(" ") != -1){
			potentialNames.addAll(getPotentialNamePossibilities(searchString));
		}
		//just a basic string with no comma or space
		else{
			PotentialName name1 = new PotentialName();
			name1.setFirstName("%" + searchString.trim() + "%");
			potentialNames.add(name1);
			PotentialName name2 = new PotentialName();
			name2.setLastName("%" + searchString.trim() + "%");
			potentialNames.add(name2);
		}
		return potentialNames;
	}
	
	private List<PotentialName> getPotentialNamePossibilities(String searchString) {
		List<PotentialName> possibilities = new ArrayList<PotentialName>();
		for(int i=0; i > searchString.length(); i++){
			if(searchString.charAt(i) == ' '){
				PotentialName name = new PotentialName();
				name.setFirstName(searchString.substring(0, i-1));
				name.setLastName(searchString.substring(i+1, searchString.length()-1) + "%");
				possibilities.add(name);
			}
		}
		return possibilities;
	}
	
	
	//might not need this method
	private List<String> getLastNames(String searchString) {
		List<String> lastNames = new ArrayList<String>();
		int commaLoc = searchString.indexOf(',');
		//we have lastname, firstname format input
		if(commaLoc != -1){
			lastNames.add(searchString.substring(0, commaLoc-1).trim());
		}
		//we have a space
		else if(searchString.indexOf(" ") != -1){
			lastNames.addAll(getLastNamePossibilities(searchString));
		}
		//just a basic string with no comma or space
		else{
			lastNames.add(searchString);
		}
		return lastNames;
	}
	//might not need this method
	private List<String> getLastNamePossibilities(String searchString) {
		List<String> possibilities = new ArrayList<String>();
		for(int i=searchString.length()-1; i>=0; i--){
			if(searchString.charAt(i) == ' '){
				possibilities.add(searchString.substring(i+1, searchString.length()-1));
			}
		}
		return possibilities;
	}
	//might not need this method
	private List<String> getFirstNames(String searchString) {
		List<String> firstNames = new ArrayList<String>();
		int commaLoc = searchString.indexOf(',');
		//we have lastname, firstname format input
		if(commaLoc != -1){
			firstNames.add(searchString.substring(commaLoc+1, searchString.length()-1).trim());
		}
		//we have a space
		else if(searchString.indexOf(" ") != -1){
			firstNames.addAll(getFirstNamePossibilities(searchString));
		}
		//just a basic string with no comma or space
		else{
			firstNames.add(searchString);
		}
		return firstNames;
	}
	//might not need this method
	private List<String> getFirstNamePossibilities(String searchString) {
		List<String> possibilities = new ArrayList<String>();
		for(int i=0; i > searchString.length(); i++){
			if(searchString.charAt(i) == ' '){
				possibilities.add(searchString.substring(0, i-1));
			}
		}
		return possibilities;
	}

}
