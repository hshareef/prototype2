package com.prototype.service;

import java.util.ArrayList;
import java.util.List;

import com.prototype.messages.Message;
import com.prototype.model.Argument;
import com.prototype.model.Claim;
import com.prototype.rules.CheckAddingArgToPublishedClaimRule;
import com.prototype.rules.CheckArgPremisesPublishedWhenPublishingArg;
import com.prototype.rules.CheckClaimPendingWhenLinkingOppoClaimRule;

public class RulesService {
	
	public List<Message> runAddArgRules (Claim claim) {
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(CheckAddingArgToPublishedClaimRule.runRule(claim));
		//can add other rules to be run when adding an arg to a claim
		printMessages(messages);
		return messages;
	}

	public List<Message> runSetOppositeClaimRules(Claim claim) {
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(CheckClaimPendingWhenLinkingOppoClaimRule.runRule(claim));
		//can add other rules to be run when linking an opposite claim
		printMessages(messages);
		return messages;
	}

	public List<Message> publishArgRules(Argument arg) {
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(CheckArgPremisesPublishedWhenPublishingArg.runRule(arg));
		printMessages(messages);
		return messages;
	}
	
	//TODO: delete this and return messages to UI
	private void printMessages (List<Message> messages) {
		for (Message message : messages) {
			System.out.println("We recieved a rule failure: " + message.getMessage());
		}
	}

}
