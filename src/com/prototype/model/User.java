package com.prototype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	@Id
	@GeneratedValue
	@Column(name="user_id")
	private Integer userId;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="email_address")
	private String emailAddress;
	@Column(name="username")
	private String username;
	@Transient
	private String password;
	@Transient
	private boolean loggedIn;
	@Column(name="salt")
	private String salt;
	@Column(name="hashed_string")
	private String hashedString;
	@Column(name="login_token")
	private String loginToken;
	@Column(name="token_expiration")
	private Date tokenExpiration;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHashedString() {
		return hashedString;
	}
	public void setHashedString(String hashedString) {
		this.hashedString = hashedString;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public Date getTokenExpiration() {
		return tokenExpiration;
	}
	public void setTokenExpiration(Date tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}	
	
}
