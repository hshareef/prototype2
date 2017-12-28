package com.prototype.validators;

import java.util.ArrayList;
import java.util.List;

import com.prototype.messages.BlankFieldError;
import com.prototype.messages.Message;
import com.prototype.model.Argument;
import com.prototype.model.Claim;
import com.prototype.model.ClaimRef;
import com.prototype.service.FieldConstants;

public class ArgumentValidator extends Validator {

	public List<Message> validateArgument(Argument arg){
		ArrayList<Message> messages = new ArrayList<>();
		if(arg.getArgName() == null || arg.getArgName().length()==0){
			messages.add(new BlankFieldError(FieldConstants.ArgumentFields.ArgumentName.value));
		}
		for(ClaimRef premise : arg.getPremises()){
			if(premise.getClaimStatement() == null || premise.getClaimStatement().length()==0){
				messages.add(new BlankFieldError(FieldConstants.ArgumentFields.ArgumentPremise.value));
				break;
			}
		}
		return messages;
	}
	
}
