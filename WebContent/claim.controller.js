
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
    	$scope.claim.arguments.push(argument);
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
    	$scope.claim.arguments[argIndex].premises.push(premise);
    };
    
    $scope.setEditMode = function(){
    	$scope.editMode = true;
    };
    
//    $scope.loadClaimPage(){
//    	location.assign("/Prototype/createForm.html");
//    };
    
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
		 //var msg = ClaimService.outsiderCall();
		 //alert(msg);
	 };
	 
	 $scope.openClaim = function(claimId){
		//alert("i might open the claim with claim id..." + claimId);
		//$location.absUrl() = '/Prototype/createForm.html';
		$window.location.href = '/Prototype/createForm.html?claimId=' + claimId;
		
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
			alert("retrieved");
			$scope.user = response.data;
			alert($scope.user.emailAddress);
			alert($scope.user);
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
			 //$scope.user.userId = localStorage.getItem("userId");
			 $scope.getUser(localStorage.getItem("userId"));
			 $scope.loadTopClaims();
		 }
		 if(getPageName(window.location.toString()) == "Prototype/createForm.html"){
			 //$scope.user.userId = localStorage.getItem("userId");
			 $scope.getUser(localStorage.getItem("userId"));
			 var claimId = getUrlVariable("claimId");
			 $scope.getClaim(claimId);
		 }
		 //alert("init function working");
		 //alert(getPageName(window.location.toString()));
		 //alert(getUrlVariable("claimId"));
	 };
	 
	 $scope.init();
    
    
});