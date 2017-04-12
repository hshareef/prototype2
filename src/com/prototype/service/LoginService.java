package com.prototype.service;

import com.prototype.dao.LoginDao;
import com.prototype.model.User;

public class LoginService {
	
	//autowire this
	LoginDao loginDao = new LoginDao();

	public User login(String username, String password){
		User user = loginDao.getUser(username, password);
		if(user != null){
			user.setLoggedIn(true);
		}
		return user;
	}

	public String createNewUser(User newUser) {
		boolean emailAddressUsed = emailAddressUsed(newUser.getEmailAddress());
		boolean userNameAvail = usernameAvailable(newUser.getUsername());
		if(!emailAddressUsed && userNameAvail){
			loginDao.createNewUser(newUser);
			return "Account created successfully!";
		}
		else if(emailAddressUsed){
			return "The email address " + newUser.getEmailAddress() + " is already in use.";
		}
		else if(!userNameAvail){
			return "The username " + newUser.getUsername() + " is already in use. Please select a new username.";
		}
		return null;//should never hit, but just a sanity check
		
	}
	
	public boolean emailAddressUsed(String emailAddress){
		return loginDao.checkEmailAddressUsed(emailAddress);
	}
	
	public boolean usernameAvailable(String username){
		return loginDao.checkUsernameAvailable(username);
	}
}
