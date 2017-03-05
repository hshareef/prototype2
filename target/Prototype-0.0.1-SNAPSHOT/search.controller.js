//var app = angular.module('Module', []);
app.controller('SearchCtrl', function($scope, SearchService) {
    $scope.num1 = 5;
    $scope.num2 = 4;
    $scope.showAddNewClaim = false;

    
    $scope.sum = function() {
        return $scope.num1 + $scope.num2;
    };
    
    $scope.changeShowAddNewClaim = function(){
    	$scope.showAddNewClaim = !$scope.showAddNewClaim;
    };
    
    $scope.showTheAction = function() {
        alert("hello there!");
        SearchService.doThing();
    };
    
    
    
    
});