
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
	    
	 $scope.createNewClaim = function(){
	 };
	 
	 $scope.callClaimServiceFunction = function(){
		 alert("Going to call the claim service function, or not...maybe");
		 //var msg = ClaimService.outsiderCall();
		 //alert(msg);
	 };
	 
	 $scope.openClaim = function(){
		alert("i might open the claim...");
		//$location.absUrl() = '/Prototype/createForm.html';
		$window.location.href = '/Prototype/createForm.html';
		
	 };
    
    
});