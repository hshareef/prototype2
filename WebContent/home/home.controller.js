
app.controller('HomeCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService, LookupService) {
	
	var vm = this;
	  
	vm.testUrl = ConfigService.getSettings().url;
	vm.topClaims = [];
	vm.editMode = false;
	vm.user = {
			userId: null,
			username: "",
			password: "",
			firstName: "",
			lastName: "",
			emailAddress: "",
			loggedIn: false	
	};
	vm.createNewAcctFlag = false;
	vm.newUser = {
			firstName: "",
			lastName: "",
			username: "",
			password: "",
			emailAddress: "",
			loggedIn: false
		};
	vm.confirmPassword = "";
	
	vm.searchString="";
	
	vm.newMissedPremiseStatement = "";

    
    vm.setEditMode = function(){
    	vm.editMode = true;
    };
    
	
	
	 vm.loadTopClaims = function(){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/topClaims/1")
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	    };
	    
	    
		
//		 vm.getClaim = function(claimId){
//			 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
//			 .then(function(response){
//				 vm.claim = response.data;
//				 vm.argumentViewingIndex = vm.getFirstPublishedArg();
//			 });
//		    };

	 
	 vm.callClaimServiceFunction = function(){
		 alert("Going to call the claim service function, or not...maybe");
	 };
	 
	 vm.openClaim = function(claimId){
		//$window.location.href = '/Prototype/createForm.html?claimId=' + claimId;
		$window.location.href = '/Prototype/#/claim/' + claimId;
	 };
	 
	 vm.searchClaims = function(){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	 };
	 
//	 vm.openDialog = function(dialogId, argIndex, dialogMode, mpoIndex){
//		 var theDialogBox = document.getElementById(dialogId);
//		 theDialogBox.style.display = "block";
//		 
//		 //if its the add argument dialog box
//		 if(dialogId == "theCreateNewArgumentDialog"){
//			 vm.addBlankArg();
//		 }
//		 else if(dialogId == "theEditArgumentDialog"){
//			 vm.currentArgIndex = argIndex;
//		 }
//		 else if(dialogId == "theCreateNewMediaResourceDialog"){
//			 vm.newMediaResource = 
//				 {
//					resourceTypeId : 1,
//					url : "",
//					publishDate : "",
//					author : null,
//					institution : null
//				 };
//		 }
//		 else if(dialogId == "theAddMpoDialog"){
//			 vm.argumentViewingIndex = argIndex;
//			 vm.mpoViewingIndex = mpoIndex;
//			 vm.orderMpoPremises();
//			 if(dialogMode == null || dialogMode == "addNewMpo"){
//				 vm.mpoEditable = true;
//			 	 vm.initializeBlankMpo();
//		 	 }
//			 else if (dialogMode == "viewMpo") {
//				 vm.mpoEditable = false;
//			 }
//		 }
//		 
//
//		 
//	 };
	 
//	 vm.closeDialog = function(dialogId, redirect, destination){
//		 var theDialogBox = document.getElementById(dialogId);
//		 theDialogBox.style.display = "none";
//		 if(dialogId == "theCreateNewMediaResourceDialog"){
//			 vm.newMediaResource = null;
//		 }
//		 if(destination !== undefined && destination !== null){
//			 window.location.href = ConfigService.getSettings().url + "/Prototype/" + destination;
//		 }
//		 else if(redirect){
//			 window.location.href = window.history.back(1);
//		 }
//	 };
	 
	 function getUrlVariable(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
	};
	
	function getPageName(longString){
		if(longString.indexOf("?") > 0){
			return longString.substring(longString.indexOf("Prototype"), longString.indexOf("?")); 
		}
		else{
			return longString.substring(longString.indexOf("Prototype"));
		}
	};
	
	vm.login = function () {
		alert("trying to log in..." + vm.user.username + " : " +  vm.user.password);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/login", vm.user)
		.then(function(response){
			vm.user = response.data;
			if(vm.user === undefined || vm.user === null){
				alert("login not successful");
			}
			else{
				alert("login successful.");
				localStorage.setItem("userId", vm.user.userId)
				$window.location.href = "/Prototype/index.html";
			}
		});
		
	};
	
	vm.logout = function(){
		alert("trying to log out!");
		localStorage.clear();
		location.reload();
	};
	
	vm.getUser = function(userId){
		var test = $http.get(ConfigService.getSettings().url + "/Prototype/prototype/login/"+userId)
		.then(function(response){
			vm.user = response.data;
		});
	};
	
	vm.setNewAcctFlag = function(flag){
		vm.createNewAcctFlag = flag;
		if(flag){

		}
		else{
			vm.newUser = null;
			vm.confirmPassword = null;
		}
			
	};
	
	vm.createNewUserAcct = function(){
		alert("trying to create new user account...");
		if(vm.newUser.password !== vm.confirmPassword){
			alert("passwords don't match");
		}else{
			var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/createUser", vm.newUser);
		}
	};
	
	vm.loadClaimsForCategory = function(id) {
		//alert("loading claims for category " + id);
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/topClaims/" + id)
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	};
	
	vm.activateCategoryButton = function(description) {
		var categoryButtons = document.getElementsByClassName("category-button");
		for(var i = 0 ; i < categoryButtons.length ; i++){
			if(categoryButtons[i].innerHTML == description){
				categoryButtons[i].style.backgroundColor = "var(--dark-accent-2)";
			}
			else {
				categoryButtons[i].style.backgroundColor = "var(--dark-accent-1-saved)";
			}
		}
	};
	 
	 vm.init = function(){
		 LookupService.getLookup("CLAIM_CATEGORIES_LKUP").then(function(response){
			 
			 vm.categoryOptions = response;
			 if(localStorage.getItem("userId") !== undefined && localStorage.getItem("userId") !== null){
				 vm.getUser(localStorage.getItem("userId"));
			 }
			 vm.loadTopClaims();
			 vm.activateCategoryButton("Law"); //default to politics
		 });
	 };
	 
	 vm.init();
    
    
});