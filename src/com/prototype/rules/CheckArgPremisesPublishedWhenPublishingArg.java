package com.prototype.rules;

import java.util.ArrayList;
import java.util.List;

import com.prototype.common.ClaimConstants;
import com.prototype.messages.Error;
import com.prototype.messages.Message;
import com.prototype.model.Argument;
import com.prototype.model.Claim;

public class CheckArgPremisesPublishedWhenPublishingArg {
	
	public static String ERROR_MESSAGE = "Cannot publish an argument if any of its premises are pending! Publish all premises first!";
	
	public static List<Message> runRule(Argument arg) {
		List<Message> messages = new ArrayList<Message>();
		if (arg.getPremises() != null && arg.getPremises().size() > 0) {
			for (Claim premise : arg.getPremises()) {
				if (premise.getStateHistory() == null 
						|| premise.getStateHistory().size() == 0 
						|| premise.getCurrentState().getClaimStatusId() != ClaimConstants.States.PUBLISHED.id) {	
					messages.add(new Error(ERROR_MESSAGE));
					break;
				}
			}
		}
		return messages;
	}
}
