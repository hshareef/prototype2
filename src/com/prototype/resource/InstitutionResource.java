package com.prototype.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.prototype.model.Institution;
import com.prototype.service.InstitutionService;
 
@Path("/institution")
public class InstitutionResource {
	
	//make this a spring bean and autowire it
	InstitutionService institutionService = new InstitutionService();
	
	@Path("/create")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Institution saveInstitution(Institution institution){
		System.out.println("Saving " + institution.getName());
		institution = institutionService.saveInstitution(institution);
		return institution;
	}


}
