
claimApp.controller('ClaimCtrl', function($scope, $http, ClaimService, $location, $window) {
	
	
	$scope.claim = new Object();
	$scope.claim.claimStatement="";
	$scope.claim.arguments = [];
	$scope.editMode = false;
	
 
    
    $scope.saveStatement = function(){
    	var test = $http.post("http://localhost:8080/Prototype/prototype/claim/create", $scope.claim);
    };
    
    $scope.addToArgumentArray = function(){
    	var argument = new Object();
    	argument.argName="default name";
    	argument.premises = [];
    	$scope.claim.arguments.push(argument);
    };
    
    
    $scope.addToPremiseArray = function(argIndex){
    	var premise = new Object();
    	premise.claimStatement="default statement";
    	$scope.claim.arguments[argIndex].premises.push(premise);
    };
    
    $scope.setEditMode = function(isEditMode){
    	editMode = isEditMode;
    };
    
//    $scope.loadClaimPage(){
//    	location.assign("/Prototype/createForm.html");
//    };
    
	$scope.topClaims = [];
	
	 $scope.testCtrl = function(){
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
	 
	 $scope.init = function(){
		 if(getPageName(window.location.toString()) == "Prototype/index.html"){
			 $scope.testCtrl();
		 }
		 if(getPageName(window.location.toString()) == "Prototype/createForm.html"){
			 var claimId = getUrlVariable("claimId");
			 $scope.getClaim(claimId);
		 }
		 //alert("init function working");
		 //alert(getPageName(window.location.toString()));
		 //alert(getUrlVariable("claimId"));
	 };
	 
	 $scope.init();
    
    
});