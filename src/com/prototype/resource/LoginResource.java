package com.prototype.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.prototype.model.Claim;
import com.prototype.model.User;
import com.prototype.service.LoginService;
 
@Path("/login")
public class LoginResource {
	
	//autowire this!!
	LoginService loginService = new LoginService();


	@Path("/login")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public User login(User loginUser){

		System.out.println("in the login resource...");
		User user = loginService.login(loginUser.getUsername(), loginUser.getPassword());
		return user;
	}
	
	@Path("/createUser")
	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public String createNewUser(User newUser){

		System.out.println("trying to create a new user...");
		return loginService.createNewUser(newUser);
	}

	@Path("/{userId}")
	@GET
	@Produces("application/json")
	public User getUser(@PathParam("userId") Integer userId) {
		User user = loginService.getUser(userId);
		System.out.println("In the get user method");
		return user;
	}

	
	
}
