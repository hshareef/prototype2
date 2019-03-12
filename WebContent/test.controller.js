/**
 * 
 */

testApp.controller('TestCtrl', function($scope, $http, $routeParams, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	
	vm.testUrl = ConfigService.getSettings().url;
	
	vm.message = "this is comming from the TestCtrl!!";
	 
});