package com.prototype.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.prototype.model.Lookup;
import com.prototype.service.LookupService;

@Path("/lookup/{lookupType}")
public class LookupResource {
	
	//need to later autowire this
	LookupService lookupService = new LookupService();


	@GET
	@Produces("application/json")
	public List<Lookup> getLookups(@PathParam("lookupType") String lookupType) {
		return lookupService.getLookups(lookupType);
	}
}
