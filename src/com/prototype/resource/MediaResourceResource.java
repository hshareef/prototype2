package com.prototype.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.prototype.model.MediaResource;
import com.prototype.service.MediaResourceService;
 
@Path("/mediaResource")
public class MediaResourceResource {
	
	//make this a spring bean and autowire it
	MediaResourceService mediaResourceService = new MediaResourceService();
	
	@Path("/create")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public MediaResource saveMediaResource(MediaResource mediaResource){
		System.out.println("Saving " + mediaResource.getTitle());
		mediaResource = mediaResourceService.saveMediaResource(mediaResource);
		return mediaResource;
	}


}
