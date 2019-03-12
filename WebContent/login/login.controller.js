
app.controller('LoginCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	
	vm.login = function () {
		alert("trying to log in..." + vm.user.username + " : " +  vm.user.password);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/login", vm.user)
		.then(function(response){
			vm.user = response.data;
			if(vm.user === undefined || vm.user === null){
				alert("login not successful");
			}
			else{
				alert("login successful.");
				localStorage.setItem("userId", vm.user.userId)
				$window.location.href = "/Prototype/#/";
			}
		});
		
	};
	
	vm.logout = function(){
		alert("trying to log out!");
		localStorage.clear();
		location.reload();
	};
    
});