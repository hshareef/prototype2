/**
 * 
 */

authorApp.controller('AuthorCtrl', function($scope, $http, $location, $window){
	
	$scope.author = {firstName: null, lastName: null};
	$scope.formDisabled;
	$scope.postSaveFlag = false;
	
	$scope.saveNewAuthor = function(){
		$http.post("http://localhost:8080/Prototype/prototype/author/create", $scope.author).then(function(response){
	    	$scope.author = response.data;
	    	$scope.formDisabled = true;
	    	$scope.postSaveFlag = true;
	    	
		});
	};
	
	$scope.closeAuthor = function(){
		$window.location.href = "/Prototype/index.html";
	};
	
	$scope.addAnotherAuthor = function(){
		$scope.author = {firstName: null, lastName: null};
		$scope.formDisabled = false;
		$scope.postSaveFlag = false;
	};
	
	 function getUrlVariable(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
	};
	
	
	 $scope.init = function(){
		var authorId = getUrlVariable("authorId");
		 if(authorId === undefined || authorId === null){
			 $scope.formDisabled = false;
		 }
		 else{
			 $scope.formDisabled = true;
		 }
	 };
	 
	 $scope.init();
	 
	 
});