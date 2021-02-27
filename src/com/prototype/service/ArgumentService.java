package com.prototype.service;

import java.util.ArrayList;
import java.util.List;

import com.prototype.common.ArgumentConstants;
import com.prototype.dao.ArgumentDao;
import com.prototype.dao.ClaimDao;
import com.prototype.model.Argument;
import com.prototype.model.ArgumentState;
import com.prototype.model.Claim;
import com.prototype.response.ArgumentResponse;
import com.prototype.response.ResponseCodes;

public class ArgumentService {
	
	ArgumentDao argumentDao = new ArgumentDao();
	ClaimDao claimDao = new ClaimDao();
	ClaimService claimService = new ClaimService();

	public List<Argument> getUserArguments(Integer userId) {
		List<Claim> claimsForUserArgs = claimDao.getClaimsForUserArgs(userId);
		List<Argument> userArguments = new ArrayList<Argument>();
		for(Claim claim : claimsForUserArgs) {
			for (Argument argument : claim.getArguments()) {
				if (argument.getOwnerId() == userId) {
					argument.setClaimId(claim.getClaimId());
					argument.setClaimStatement(claim.getClaimStatement());
					userArguments.add(argument);
				}
			}
		}
		return userArguments;
	}

	public ArgumentResponse saveArgument(Argument argument) {
		ArgumentResponse ar = new ArgumentResponse();
		//can run arg rules here and add to messages if needed
		//ex: ar.setMessages(arguemnt rules service.run blah);
		addPrelimStates(argument);
		addUserIdToPremises(argument);
		Argument arg = argumentDao.saveArgument(argument);
		ar.setCode(ResponseCodes.SUCCESS.id);
		ar.setArgument(arg);
		return ar;
	}

	private void addUserIdToPremises(Argument arg) {
		if (arg.getPremises() != null && arg.getPremises().size() > 0) {
			for (Claim premise : arg.getPremises()) {
				if (premise.getClaimId() == null) {
					//means we are creating premise 
					premise.setOriginalOwnerId(arg.getOwnerId());
					premise.setOriginalOwnerUsername(arg.getOwnerUsername());
				}
			}
		}		
	}

	private void addPrelimStates(Argument arg) {
		if (arg.getStateHistory() == null) {
			arg.setStateHistory(new ArrayList<ArgumentState>());
		}
		if (arg.getStateHistory().size() == 0) {
			ArgumentState prelimState = new ArgumentState();
			prelimState.setArgumentStatusId(ArgumentConstants.States.PRELIM.id);
			arg.getStateHistory().add(prelimState);
		}
		if (arg.getPremises() != null && arg.getPremises().size() > 0) {
			for (Claim premise : arg.getPremises()) {
				if (premise.getStateHistory() == null || premise.getStateHistory().size() == 0) {
					//premises are claims, so can use claimService
					claimService.addPrelimState(premise);
				}
			}
		}		
	}
}
