package com.prototype.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.prototype.dao.LoginDao;
import com.prototype.model.User;

public class LoginService {
	
	//autowire this
	LoginDao loginDao = new LoginDao();

	public User login(String username, String password){
		User user = loginDao.getUser(username, password);
		if (user != null) {
			String passwordWithSalt = password + user.getSalt();
			boolean passwordValid = validatePassword(passwordWithSalt, user.getHashedString());
			if (passwordValid) {
				System.out.println("login success");
				String loginToken = generateString(24);
				loginDao.updateToken(user.getUserId(), loginToken);
				user.setLoginToken(loginToken);
				//clear this info out so that we dont pass it to the front end
				user.setHashedString(null);
				user.setSalt(null);
				user.setLoggedIn(true);//maybe get rid of this field later, we are now using a timestamp
				return user;
			} else {
				System.out.println("login failed. invalid credentials. Username: " + username);
				return null;
			}
		} else {
			System.out.println("login failed. User not found. Username: " + username);
			return null;
		}

//		if(user != null){
//			user.setLoggedIn(true);
//		}
//		return user;
	}
	
	private boolean validatePassword(String passwordWithSalt, String storedHashedString) {
//			MessageDigest digest = MessageDigest.getInstance("SHA-256");
//			byte[] hash = digest.digest(passwordWithSalt.getBytes(StandardCharsets.UTF_8));
//			StringBuffer hexString = new StringBuffer();
//
//	        for (int i = 0; i < hash.length; i++) {
//	            String hex = Integer.toHexString(0xff & hash[i]);
//	            if(hex.length() == 1) hexString.append('0');
//	            hexString.append(hex);
//	        }
//	        String passwordHashed = hexString.toString();
		String passwordHashed = getHashedString(passwordWithSalt);
        return passwordHashed.equals(storedHashedString);
	}
	
	private String getHashedString (String passwordWithSalt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(passwordWithSalt.getBytes(StandardCharsets.UTF_8));
			StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        String passwordHashed = hexString.toString();
	        return passwordHashed;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String generateString (int length) {
		StringBuffer tokenSb = new StringBuffer("");
		for (int i = 0 ; i < length ; i++) {
			int value = (int)(Math.random()*62);
			if (value <= 9) {
				value += 48;
			} else if (value <=35) {
				value += 55;
			} else if (value <= 61) {
				value +=61;
			}
			char cVal = (char)value;
			tokenSb.append(cVal);
		}
		return tokenSb.toString();
	}
	
	private void removeSensitiveUserInfo(User user) {
		user.setSalt(null);
		user.setPassword(null);
		user.setHashedString(null);
	}

	public User createNewUser(User newUser) {
		boolean emailAddressUsed = emailAddressUsed(newUser.getEmailAddress());
		boolean userNameAvail = usernameAvailable(newUser.getUsername());
		if(!emailAddressUsed && userNameAvail){
			newUser.setSalt(generateString(6));
			newUser.setHashedString(getHashedString(newUser.getPassword() + newUser.getSalt()));
			newUser.setLoginToken(generateString(24));
			loginDao.createNewUser(newUser);
			removeSensitiveUserInfo(newUser);
			return newUser;
			//return "Account created successfully!";
		}
//		else if(emailAddressUsed){
//			return "The email address " + newUser.getEmailAddress() + " is already in use.";
//		}
//		else if(!userNameAvail){
//			return "The username " + newUser.getUsername() + " is already in use. Please select a new username.";
//		}
		return null;//should never hit, but just a sanity check
		
	}
	
	public boolean emailAddressUsed(String emailAddress){
		return loginDao.checkEmailAddressUsed(emailAddress);
	}
	
	public boolean usernameAvailable(String username){
		return loginDao.checkUsernameAvailable(username);
	}

	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		if(userId != null){
			return loginDao.getUser(userId);
		}
		else{
			return getGuestUser();
		}
	}

	private User getGuestUser() {
		User guest = new User();
		guest.setUsername("Guest");
		guest.setFirstName("Guest");
		guest.setLastName("");
		guest.setLoggedIn(false);
		return guest;
	}
}
