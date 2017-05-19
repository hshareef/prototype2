package com.prototype.service;

import java.util.ArrayList;
import java.util.List;

import com.prototype.dao.ClaimDao;
import com.prototype.model.Argument;
import com.prototype.model.Claim;

public class ClaimService {
	
	ClaimDao claimDao = new ClaimDao();
	
	public void saveClaim(Claim claim){
		for(Argument arg : claim.getArguments()){
			arg.determineValidity();
		}
		claimDao.saveClaim(claim);
	}
	
	public List<Claim> getTopClaims(){
		return claimDao.getTopClaims();
	}

	public Claim getClaim(Integer claimId) {
		return claimDao.getClaim(claimId);
	}

	
}
