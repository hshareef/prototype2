
app.controller('LoginCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	vm.createNewAcctFlag = false;
	vm.passwordsMatch = true;
	
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
	
	vm.createNewUserAcct = function(){
		//alert("trying to create new user account...");
		vm.checkPasswordsMatch();
		if(!vm.passwordsMatch){
			alert("passwords don't match");
		}else{
			$http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/createUser", vm.newUser);
		}
	};
	
	vm.checkPasswordsMatch = function() {
		vm.passwordsMatch = vm.newUser.password == vm.confirmPassword;
			
	};
	
	vm.logout = function(){
		alert("trying to log out!");
		localStorage.clear();
		location.reload();
	};
	
	vm.setNewAcctFlag = function(flag){
		vm.createNewAcctFlag = flag;
		if(flag){

		}
		else{
			vm.newUser = null;
			vm.confirmPassword = null;
		}
			
	};
    
});