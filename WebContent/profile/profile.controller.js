/**
 * 
 */

app.controller('ProfileCtrl', function($scope, $http, $routeParams, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	
	vm.testUrl = ConfigService.getSettings().url;
	 
});