
claimApp.controller('ClaimCtrl', function($scope, $http, ClaimService, $location, $window) {
	
	$scope.claim = new Object();
	$scope.claim.claimStatement="";
	$scope.claim.arguments = [];
	$scope.claim.keywords = [];
	$scope.claim.originalOwnerId=-1;
	$scope.claim.originalOwnerUsername = "";
	$scope.editMode = false;
	$scope.user = {
			userId: null,
			username: "",
			password: "",
			firstName: "",
			lastName: "",
			emailAddress: "",
			loggedIn: false	
	};
	$scope.createNewAcctFlag = false;
	$scope.newUser = {
			firstName: "",
			lastName: "",
			username: "",
			password: "",
			emailAddress: "",
			loggedIn: false
		};
	$scope.confirmPassword = "";
	
 
    //for saving a claim
    $scope.saveStatement = function(){
    	if($scope.claim.originalOwnerId=-1){
    		$scope.claim.originalOwnerId = $scope.user.userId;
    		$scope.claim.originalOwnerUsername = $scope.user.username;
    	}
    	var test = $http.post("http://localhost:8080/Prototype/prototype/claim/create", $scope.claim);
    	alert("message saved!");
    	$scope.editMode=false;
    };  
    
    $scope.addToArgumentArray = function(){
    	var argument = new Object();
    	argument.argName="default name";
    	argument.premises = [];
    	argument.ownerId = $scope.user.userId;
    	argument.ownerUsername = $scope.user.username;
    	argument.editable = true;
    	argument.valid = false;
    	argument.sound = false;
    	argument.validCount = 0;
    	argument.invalidCount = 0;
    	$scope.claim.arguments.push(argument);
    };
    
    $scope.deleteArgument = function(index){
    	$scope.claim.arguments.splice(index, 1);
    	$scope.saveStatement();
    };
    
    $scope.setArgumentEditMode = function(index, editMode){//editMode is bool
    	if($scope.claim.arguments[index].ownerId === $scope.user.userId){
    		$scope.claim.arguments[index].editable = editMode;
    	}
    };
    
    $scope.addKeyword = function(){
    	var keyword = "keyword";
    	$scope.claim.keywords.push(keyword);
    };
    
    $scope.deleteKeyword = function(index){
    	$scope.claim.keywords.splice(index, 1);
    	$scope.saveStatement();
    };
    
    
    $scope.addToPremiseArray = function(argIndex){
    	var premise = new Object();
    	premise.claimStatement="default statement";
    	premise.originalOwnerId = $scope.user.userId;
    	premise.originalOwnerUsername = $scope.user.username;
    	$scope.claim.arguments[argIndex].premises.push(premise);
    };
    
    $scope.setEditMode = function(){
    	$scope.editMode = true;
    };
    
	$scope.topClaims = [];
	
	 $scope.loadTopClaims = function(){
		 $http.get("http://localhost:8080/Prototype/prototype/claim/topClaims")
		 .then(function(response){
			 $scope.topClaims = response.data;
		 });
	    };
	    
	    
		
		 $scope.getClaim = function(claimId){
			 $http.get("http://localhost:8080/Prototype/prototype/claim/" + claimId)
			 .then(function(response){
				 $scope.claim = response.data;
			 });
		    };
	    
	 $scope.createNewClaim = function(){
		 
	 };
	 
	 $scope.callClaimServiceFunction = function(){
		 alert("Going to call the claim service function, or not...maybe");
	 };
	 
	 $scope.openClaim = function(claimId){
		$window.location.href = '/Prototype/createForm.html?claimId=' + claimId;
		
	 };
	 
	 $scope.voteValidity = function(index, valid){
		 alert("trying to vote as valid");
		 if(valid){
			 if($scope.claim.arguments[index].validCount === undefined || $scope.claim.arguments[index].validCount === null){
				 $scope.claim.arguments[index].validCount = 1;
			 }
			 else {
				 $scope.claim.arguments[index].validCount += 1;
			 }
		 }
		 else{
			 if($scope.claim.arguments[index].invalidCount === undefined || $scope.claim.arguments[index].invalidCount === null){
				 $scope.claim.arguments[index].invalidCount = 1;
			 }
			 else {
				 $scope.claim.arguments[index].invalidCount += 1;
			 }
		 }
		 $scope.saveStatement();
	 };
	 

	 
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
	
	$scope.login = function () {
		alert("trying to log in..." + $scope.user.username + " : " +  $scope.user.password);
		var test = $http.post("http://localhost:8080/Prototype/prototype/login/login", $scope.user)
		.then(function(response){
			$scope.user = response.data;
			if($scope.user === undefined || $scope.user === null){
				alert("login not successful");
			}
			else{
				alert("login successful.");
				localStorage.setItem("userId", $scope.user.userId)
				$window.location.href = "/Prototype/index.html";
			}
		});
		
	};
	
	$scope.logout = function(){
		alert("trying to log out!");
		localStorage.clear();
		location.reload();
	};
	
	$scope.getUser = function(userId){
		var test = $http.get("http://localhost:8080/Prototype/prototype/login/"+userId)
		.then(function(response){
			$scope.user = response.data;
		});
	};
	
	$scope.setNewAcctFlag = function(flag){
		$scope.createNewAcctFlag = flag;
		if(flag){

		}
		else{
			$scope.newUser = null;
			$scope.confirmPassword = null;
		}
			
	};
	
	$scope.createNewUserAcct = function(){
		alert("trying to create new user account...");
		if($scope.newUser.password !== $scope.confirmPassword){
			alert("passwords don't match");
		}else{
			var test = $http.post("http://localhost:8080/Prototype/prototype/login/createUser", $scope.newUser);
		}
	};
	 
	 $scope.init = function(){
		 if(getPageName(window.location.toString()) == "Prototype/index.html"){
			 if(localStorage.getItem("userId") !== undefined && localStorage.getItem("userId") !== null){
				 $scope.getUser(localStorage.getItem("userId"));
			 }
			 $scope.loadTopClaims();
		 }
		 if(getPageName(window.location.toString()) == "Prototype/createForm.html"){
			 //$scope.user.userId = localStorage.getItem("userId");
			 $scope.getUser(localStorage.getItem("userId"));
			 var claimId = getUrlVariable("claimId");
			 $scope.getClaim(claimId);
		 }
	 };
	 
	 $scope.init();
    
    
});