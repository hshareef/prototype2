package com.prototype.service;

import java.util.List;

import com.prototype.dao.LookupDao;
import com.prototype.model.Lookup;

public class LookupService {

	LookupDao lookupDao = new LookupDao();
	
	public List<Lookup> getLookups(String lookupType){
		return lookupDao.getLookups(lookupType);
	}
}
