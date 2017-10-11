
claimApp.controller('ClaimCtrl', function($scope, $http, ClaimService, $location, $window) {
	
	$scope.topClaims = [];
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
	
	$scope.searchString="";
	
	$scope.oppositeClaimSearchResults = [];
	$scope.oneUnsavedArgument = false;
	$scope.currentArgIndex = null;

	
 
    //for saving a claim
    $scope.saveStatement = function(){
    	if($scope.claim.originalOwnerId=-1){
    		$scope.claim.originalOwnerId = $scope.user.userId;
    		$scope.claim.originalOwnerUsername = $scope.user.username;
    	}
    	var test = $http.post("http://localhost:8080/Prototype/prototype/claim/create", $scope.claim).then(function(response){
        	alert("message saved!");
        	$scope.claim = response.data;
        	$scope.editMode=false;
        	$scope.oneUnsavedArgument = false;
        	//$scope.closeCreateClaimDialog(false);
        	$scope.closeDialog('theCreateClaimDialog', false);
        	$scope.openClaim($scope.claim.claimId);
    	});

    };  
    
    $scope.addBlankArg = function(){
    	var newArg = null;
		 $http.get("http://localhost:8080/Prototype/prototype/claim/argTemplate")
		 .then(function(response){
			 newArg = response.data;
			 newArg.ownerId = $scope.user.userId;
			 newArg.ownerUsername = $scope.user.username;
			 $scope.claim.arguments.push(newArg);
			 $scope.currentArgIndex = $scope.claim.arguments.length - 1;
		 });
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
    	argument.fallacyDetails = {
    			adHomniemUpvotes: 0,
    			adHomniemDownvotes: 0,
    			ignorantumUpvotes: 0,
    			ignorantumDownvotes: 0,
    			adpopulumUpvotes: 0,
    			adpopulumDownvotes: 0,
    			argFromAuthorityUpvotes: 0,
    			argFromAuthorityDownvotes: 0,
    			generalizationUpvotes: 0,
    			generalizationDownvotes: 0,
    			slipperySlopeUpvotes: 0,
    			slipperSlopeDownvotes: 0,
    			strawmanUpvotes: 0,
    			strawmanDownvotes: 0,
    			redHerringUpvotes: 0,
    			redHerringDownvotes: 0,
    			falseDichotomyUpvotes: 0,
    			falseDichotomyDownvotes: 0,
    			circularReasoningUpvotes: 0,
    			circularReasoningDownvotes: 0
    	};
    	$scope.claim.arguments.push(argument);
    	$scope.oneUnsavedArgument = true;
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
    
    $scope.addToCurrentArgPremiseArray = function(){
    	var premise = new Object();
    	premise.claimStatement="default statement";
    	premise.originalOwnerId = $scope.user.userId;
    	premise.originalOwnerUsername = $scope.user.username;
    	$scope.claim.arguments[$scope.currentArgIndex].premises.push(premise);
    };
    
    $scope.setEditMode = function(){
    	$scope.editMode = true;
    };
    
	
	
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
	 
	 $scope.searchClaims = function(){
		 alert($scope.searchString);
		 $http.get("http://localhost:8080/Prototype/prototype/claim/searchClaims/" + $scope.searchString)
		 .then(function(response){
			 $scope.topClaims = response.data;
		 });
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
	 
	 $scope.voteFallacy = function(type, index){
		 alert("inside the vote");
		 $scope.claim;
		 if(type === 'adHomniem'){
			 $scope.claim.arguments[index].fallacyDetails.adHomniemUpvotes =
				 $scope.claim.arguments[index].fallacyDetails.adHomniemUpvotes+1;
			 var t=9;
			 $scope.claim;
		 }
		 else if(type === 'ignorantum'){
			 $scope.claim.arguments[index].fallacyDetails.ignorantumUpvotes =
				 $scope.claim.arguments[index].fallacyDetails.ignorantumUpvotes+1;
		 }
		 else if(type === 'adpopulum'){
			 $scope.claim.arguments[index].fallacyDetails.adpopulumUpvotes +=1;	 
		 }
		 else if(type === 'argFromAuthority'){
			 $scope.claim.arguments[index].fallacyDetails.argFromAuthorityUpvotes +=1;
		 }
		 else if(type === 'generalization'){
			 $scope.claim.arguments[index].fallacyDetails.generalizationUpvotes +=1;
		 }
		 else if(type === 'slipperySlope'){
			 $scope.claim.arguments[index].fallacyDetails.slipperySlopeUpvotes +=1;
		 }
		 else if(type === 'strawman'){
			 $scope.claim.arguments[index].fallacyDetails.strawmanUpvotes +=1;
		 }
		 else if(type === 'redHerring'){
			 $scope.claim.arguments[index].fallacyDetails.redHerringUpvotes +=1;
		 }
		 else if(type === 'falseDichotomy'){
			 $scope.claim.arguments[index].fallacyDetails.falseDichotomyUpvotes +=1;
		 }
		 else if(type === 'circularReasoning'){
			 $scope.claim.arguments[index].fallacyDetails.circularReasoningUpvotes +=1;
		 }
		 $scope.saveStatement();
		 
	 };

//	 $scope.openOppositeClaimDialog = function(){
//		 var theOppoClaimDialog = document.getElementById('theOppoClaimDialog');
//		 theOppoClaimDialog.style.display = "block";
//	 };
//	 
//	 $scope.closeOppositeClaimDialog = function(){
//		 var theOppoClaimDialog = document.getElementById('theOppoClaimDialog');
//		 theOppoClaimDialog.style.display = "none";
//	 };
//	 
//	 $scope.openCreateClaimDialog = function(){
//		 var theCreateClaimDialog = document.getElementById('theCreateClaimDialog');
//		 theCreateClaimDialog.style.display = "block";
//	 };
//	 
//	 $scope.closeCreateClaimDialog = function(redirect){
//		 var theCreateClaimDialog = document.getElementById('theCreateClaimDialog');
//		 theCreateClaimDialog.style.display = "none";
//		 if(redirect){
//			 window.location.href = window.history.back(1);
//		 }
//	 };
	 
	 $scope.openDialog = function(dialogId, editArgIndex){
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "block";
		 
		 //if its the add argument dialog box
		 if(dialogId == "theEditArgumentDialog" && (editArgIndex === undefined || editArgIndex === null)){
			 $scope.addBlankArg();
		 }
		 else if(dialogId == "theEditArgumentDialog"){
			 $scope.currentArgIndex = editArgIndex;
		 }
		 

		 
	 };
	 
	 $scope.closeDialog = function(dialogId, redirect){
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "none";
		 if(redirect){
			 window.location.href = window.history.back(1);
		 }
	 };
	 
	 $scope.searchOppositeClaims = function(){
		 alert($scope.searchString);
		 $http.get("http://localhost:8080/Prototype/prototype/claim/searchClaims/" + $scope.searchString)
		 .then(function(response){
			 $scope.oppositeClaimSearchResults = response.data;
		 });
	 };
	 
	 $scope.setOppositeClaim = function(claimId, oppositeClaimId){
		alert("this claim id: " + claimId + "\nOpposite claim Id: " + oppositeClaimId); 
		var test = $http.post("http://localhost:8080/Prototype/prototype/claim/opposites/" + oppositeClaimId, $scope.claim);
		$scope.closeOppositeClaimDialog();
		//location.reload();
		
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
			 if(claimId === undefined || claimId === null){
				 $scope.openDialog('theCreateClaimDialog');
			 }
			 $scope.getClaim(claimId);
		 }
	 };
	 
	 $scope.init();
    
    
});