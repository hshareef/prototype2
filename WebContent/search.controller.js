//var app = angular.module('Module', []);
app.controller('SearchCtrl', function($scope, $http, SearchService) {
    $scope.num1 = 5;
    $scope.num2 = 4;
    $scope.showAddNewClaim = false;
    $scope.restServiceString;
    $scope.dog = {age: "13", name:"Ernesto"};
    $scope.catName = "mokn";
    $scope.greeting = {id: 15, content: "hello, dog"};

    
    $scope.sum = function() {
        return $scope.num1 + $scope.num2;
    };
    
    $scope.changeShowAddNewClaim = function(){
    	$scope.showAddNewClaim = !$scope.showAddNewClaim;
    };
    
    $scope.showTheAction = function() {
        alert("hello there dude!");
        $http.get('http://rest-service.guides.spring.io/greeting').
        then(function(response) {
            $scope.greeting = response.data;
        });
        alert($scope.greeting.content);
        SearchService.doThing();
        $http.get("http://localhost:8080/Prototype/prototype/ctofservice/testJson").
        then(function(response){
        	$scope.dog = response.data;
        });
        alert(dog.name);
        $http.get("http://localhost:8080/Prototype/prototype/ctofservice/cat").
        then(function(response){
        	$scope.catName = response.data;
        });
        alert(catName);
    };
    
    
    
    
});