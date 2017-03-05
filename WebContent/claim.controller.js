
claimApp.controller('ClaimCtrl', function($scope, $http, ClaimService) {
	
	
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
    
    
});