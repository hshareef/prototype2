/**
 * 
 */
claimApp.service('ClaimService',function(){
	
	var dog = "fido";

	this.saveClaim = function(){
		
	};
	
	this.printDog = function(){
		alert(dog);
	};
	
	this.outsiderCall = function(){
		alert("currently in the claim service!!");
		return "I am your grandpa!";
	};
});