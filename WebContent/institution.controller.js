/**
 * 
 */
institutionApp.controller('InstitutionCtrl', function($scope, $http, $location, $window){
	
	$scope.institution = {name: null};
	$scope.postSaveFlag = false;
	
	$scope.saveNewInstitution = function(){
		$http.post("http://localhost:8080/Prototype/prototype/institution/create", $scope.institution).then(function(response){
	    	$scope.institution = response.data;
	    	$scope.formDisabled = true;
	    	$scope.postSaveFlag = true;
	    	
		});
	};
	
	
	$scope.closeInstitution = function(){
		$window.location.href = "/Prototype/index.html";
	};
	
	$scope.addAnotherInstitution = function(){
		$scope.institution = {name: null};
		$scope.formDisabled = false;
		$scope.postSaveFlag = false;
	};
	
	 function getUrlVariable(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
	};
	
	
	 $scope.init = function(){
		var institutionId = getUrlVariable("institutionId");
		 if(institutionId === undefined || institutionId === null){
			 $scope.formDisabled = false;
		 }
		 else{
			 $scope.formDisabled = true;
		 }
	 };
	 
	 $scope.init();
});