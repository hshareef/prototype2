package com.prototype.validators;

import java.util.ArrayList;
import java.util.List;

import com.prototype.messages.BlankFieldError;
import com.prototype.messages.Message;
import com.prototype.model.Argument;
import com.prototype.model.Claim;
import com.prototype.service.FieldConstants;

public class ClaimValidator extends Validator {
	
	//need to autowire this
	ArgumentValidator argValidator = new ArgumentValidator();
	
	public List<Message> validateClaim(Claim claim){
		ArrayList <Message> messages = new ArrayList<>();
		if(claim.getClaimStatement() == null || claim.getClaimStatement().length() < 1){
			BlankFieldError err = new BlankFieldError(FieldConstants.ClaimFields.ClaimStatement.value);
			messages.add(err);
		}
		if(claim.getArguments() != null && claim.getArguments().size() > 0){
			for(Argument arg : claim.getArguments()){
				messages.addAll(argValidator.validateArgument(arg));
			}
		}
		return messages;
	}
}
