
mediaResourceApp.controller('MediaResourceCtrl', function($scope, $http, $location, $window) {
	
	$scope.mediaResource = {title: null, url: null};
	$scope.formDisabled;
	$scope.postSaveFlag = false;
	$scope.testWord="";
	
	$scope.testTheTestWord = function(){
		alert($scope.testWord);
		$http.get("http://localhost:8080/Prototype/prototype/quickSearch/searchPeople/" + $scope.testWord)
		 .then(function(response){
			 $scope.results = response.data;
		 });
	};
	
	 $scope.getClaim = function(claimId){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
		 .then(function(response){
			 $scope.claim = response.data;
			 $scope.argumentViewingIndex = $scope.getFirstPublishedArg();
		 });
	    };
	
	$scope.saveMediaResource = function(){
		$http.post("http://localhost:8080/Prototype/prototype/mediaResource/create", $scope.mediaResource).then(function(response){
	    	$scope.mediaResource = response.data;
	    	$scope.formDisabled = true;
	    	$scope.postSaveFlag = true;
	    	
		});
	};
	
	$scope.closeMediaResource = function(){
		$window.location.href = "/Prototype/index.html";
	};
	
	$scope.addAnotherMediaResource = function(){
		$scope.mediaResource = {title: null, url: null};
		$scope.formDisabled = false;
		$scope.postSaveFlag = false;
	};
	
	 function getUrlVariable(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
	};
	
	
	 $scope.init = function(){
		var mediaResourceId = getUrlVariable("mediaResourceId");
		 if(mediaResourceId === undefined || mediaResourceId === null){
			 $scope.formDisabled = false;
		 }
		 else{
			 $scope.formDisabled = true;
		 }
	 };
	 
	 $scope.init();
    
});