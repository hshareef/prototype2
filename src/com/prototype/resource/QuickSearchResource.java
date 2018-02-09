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

import com.prototype.model.Person;
import com.prototype.service.QuickSearchService;
 
@Path("/quickSearch")
public class QuickSearchResource {
	
	//make this a spring bean and autowire it
	QuickSearchService quickSearchService = new QuickSearchService();
	
	
	
	@Path("/searchPeople/{searchString}")
	@GET
	@Produces("application/json")
	public List<Person> getSearchedPeople(@PathParam("searchString") String searchString) {
		System.out.println("Attempting to search for people with search string " +searchString+"...");
		//List <ClaimRef> searchedClaims = claimService.getSearchedClaimRefs(searchString);
		List <Person> searchedPeople = quickSearchService.getSearchedPeople(searchString);
		return searchedPeople;
	}

}
