package com.prototype.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.prototype.model.Argument;
import com.prototype.model.Claim;
import com.prototype.model.ClaimRef;
import com.prototype.response.ArgumentResponse;
import com.prototype.service.ArgumentService;
 
@Path("/argument")
public class ArgumentResource {
	
	ArgumentService argumentService = new ArgumentService();
	
	@Path("/userArguments/{userId}")
	@GET
	@Produces("application/json")
	public List<Argument> getUserArguments(@PathParam("userId") Integer userId) {
		System.out.println("Attempting to get user's arguments...");
		return argumentService.getUserArguments(userId);
	}
	
	@Path("/save")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public ArgumentResponse saveArgument(Argument argument) {
		System.out.println("Attempting to save argument...");
		return argumentService.saveArgument(argument);
	}
	//maybe move this over from claim resource
//	@Path("/argTemplate")
//	@GET
//	@Produces("application/json")
//	public Argument getArgTemplate() {
//		Argument arg  = claimService.getArgumentTemplate();
//		return arg;
//	}
	
	//maybe move this over from claim resource
//	@Path("/updateState/{targetStateId}")
//	@POST
//	@Produces("application/json")
//	@Consumes("application/json")
//	public Argument updateArgumentState(Argument argument, @PathParam("targetStateId") Integer targetStateId){
//		argument = claimService.updateArgState(argument, targetStateId);
//		return argument;
//	}
	
}
