package com.prototype.rules;

import java.util.ArrayList;
import java.util.List;

import com.prototype.common.ClaimConstants;
import com.prototype.messages.Error;
import com.prototype.messages.Message;
import com.prototype.model.Claim;

public class CheckClaimPendingWhenLinkingOppoClaimRule {
	
	public static String ERROR_MESSAGE = "Cannot set opposite claims for pending claims! Publish claim first!";
	
	public static List<Message> runRule(Claim claim) {
		List<Message> messages = new ArrayList<Message>();
		if(claim.getCurrentState().getClaimStatusId() == ClaimConstants.States.PRELIM.id) {
			messages.add(new Error(ERROR_MESSAGE));
		}
		return messages;
	}

}
