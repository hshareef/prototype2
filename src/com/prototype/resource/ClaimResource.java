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
import com.prototype.service.ClaimService;
 
@Path("/claim")
public class ClaimResource {
	
	ClaimService claimService = new ClaimService();

 
	@Path("/temp/{c}")
	@GET
	@Produces("application/xml")
	public String convertCtoFfromInput(@PathParam("c") Double c) {
		Double fahrenheit;
		Double celsius = c;
		fahrenheit = ((celsius * 9) / 5) + 32;
 
		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
	}
	
	@Path("/create")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Claim saveClaim(Claim claim){
		int j=0;
		System.out.println("Hello there you rabbit!!");
		System.out.println(claim.getClaimStatement());
		j++;
		if(j==5){
			return null;
		}
		claim = claimService.saveClaim(claim);
		return claim;
	}
	
	@Path("/opposites/{oppositeClaimId}")
	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public String setOppositeClaims(Claim claim, @PathParam("oppositeClaimId") Integer oppositeClaimId){
		claimService.setOppositeClaim(claim, oppositeClaimId);
		return "hello";
	}
	
	@Path("/topClaims")
	@GET
	@Produces("application/json")
	public List<Claim> getTopClaims() {
		System.out.println("Attempting to get the top claims...");
		return claimService.getTopClaims();
	}
	
	@Path("/searchClaims/{searchString}")
	@GET
	@Produces("application/json")
	public List<Claim> getSearchedClaims(@PathParam("searchString") String searchString) {
		System.out.println("Attempting to search for claims with search string " +searchString+"...");
		List <Claim> searchedClaims = claimService.getSearchedClaims(searchString);
		return searchedClaims;
	}
	
	@Path("/{claimId}")
	@GET
	@Produces("application/json")
	public Claim getClaim(@PathParam("claimId") Integer claimId) {
		Claim claim  = claimService.getClaim(claimId);
		return claim;
	}
	
	@Path("/argTemplate")
	@GET
	@Produces("application/json")
	public Argument getArgTemplate() {
		Argument arg  = claimService.getArgumentTemplate();
		return arg;
	}
	
	@Path("/updateState/{targetStateId}")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Argument updateArgumentState(Argument argument, @PathParam("targetStateId") Integer targetStateId){
		argument = claimService.updateArgState(argument, targetStateId);
		return argument;
	}
	
	@Path("/delete/{claimId}")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public boolean deleteClaim(@PathParam("claimId") Integer claimId){
		return claimService.deleteClaim(claimId);
	}
}
