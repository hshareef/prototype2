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
		//fahrenheit = ((celsius * 9) / 5) + 32;
		fahrenheit = 10.0;
 
		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
	}
	
	@Path("/create")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Claim createClaim(Claim claim){
		System.out.println(claim.getClaimStatement());
		claim = claimService.createClaim(claim);
		return claim;
	}
	
	@Path("/save")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Claim saveClaim(Claim claim){
		System.out.println(claim.getClaimStatement());
		claim = claimService.saveClaim(claim);
		return claim;
	}
	
	@Path("/opposites/{oppositeClaimId}")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Claim setOppositeClaims(Claim claim, @PathParam("oppositeClaimId") Integer oppositeClaimId){
		claim = claimService.setOppositeClaim(claim, oppositeClaimId);
		return claim;
	}
	
	@Path("/topClaims/{categoryId}")
	@GET
	@Produces("application/json")
	public List<Claim> getTopClaims(@PathParam("categoryId") Integer categoryId) {
		System.out.println("Attempting to get the top claims...");
		return claimService.getTopClaims(categoryId);
	}
	
//	@Path("/claimsForUserArgs/{userId}")
//	@GET
//	@Produces("application/json")
//	public List<Claim> getClaimsForUserArgs(@PathParam("userId") Integer userId) {
//		return claimService.getClaimsForUserArgs(userId);
//	}
	
	@Path("/userClaims/{userId}")
	@GET
	@Produces("application/json")
	public List<Claim> getUserClaims(@PathParam("userId") Integer userId) {
		System.out.println("Attempting to get user's claims...");
		return claimService.getUserClaims(userId);
	}
	
	@Path("/searchClaims/{searchString}")
	@GET
	@Produces("application/json")
	public List<Claim> getSearchedClaims(@PathParam("searchString") String searchString) {
		System.out.println("Attempting to search for claims with search string " +searchString+"...");
		//List <ClaimRef> searchedClaims = claimService.getSearchedClaimRefs(searchString);
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
	
	//why is this here? should it be in argument resource?
	@Path("/updateState/{targetStateId}")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public ArgumentResponse updateArgumentState(Argument argument, @PathParam("targetStateId") Integer targetStateId){
		ArgumentResponse ar = claimService.updateArgState(argument, targetStateId);
		return ar;
	}
	
	@Path("/updateClaimStatus/{claimId}/{targetStatusId}")
	@GET
	@Produces("application/json")
	public boolean updateClaimStatus(@PathParam("claimId") Integer claimId, @PathParam("targetStatusId") Integer targetStatusId){
		return claimService.updateClaimStatus(claimId, targetStatusId);
	}
	
	@Path("/delete/{claimId}")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public boolean deleteClaim(@PathParam("claimId") Integer claimId){
		return claimService.deleteClaim(claimId);
	}
}
