
app.controller('LoginCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	vm.createNewAcctFlag = false;
	vm.passwordsMatch = true;
	
	vm.login = function () {
		console.log(vm.user);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/login", vm.user)
		.then(function(response){
			vm.user = response.data;
			if(vm.user === undefined || vm.user === null || vm.user == ""){
				alert("login failed.");
			} else {
				alert("login successful.");
				localStorage.setItem("userId", vm.user.userId);
				localStorage.setItem("username", vm.user.username);
				localStorage.setItem("loginToken", vm.user.loginToken);
				localStorage.setItem("user", JSON.stringify(vm.user));
				$window.location.href = "/Prototype/#/";
			}
		});
		
	};
	
	vm.createNewUserAcct = function () {
		console.log(vm.newUser);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/createUser", vm.newUser)
		.then(function(response){
			vm.user = response.data.user;
			if(vm.user === undefined || vm.user === null || vm.user == ""){
				alert(response.data.message);
			} else {
				alert("login successful.");
				localStorage.setItem("userId", vm.user.userId);
				localStorage.setItem("username", vm.user.username);
				localStorage.setItem("loginToken", vm.user.loginToken);
				localStorage.setItem("user", JSON.stringify(vm.user));
				$window.location.href = "/Prototype/#/";
			}
		});
		
	};
	
	vm.checkPasswordsMatch = function() {
		vm.passwordsMatch = vm.newUser.password == vm.confirmPassword;
			
	};
	
	//this function current in home controller because accessed from home page - it may move
//	vm.logout = function(){
//		alert("trying to log out yoyo!");
//		//localStorage.clear();
//		localStorage.removeItem("bunz");
//		localStorage.removeItem("userId");
//		localStorage.removeItem("username");
//		localStorage.removeItem("loginToken");
//		localStorage.removeItem("user");
////		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/createUser", vm.newUser)
////		.then(function(response){
////			vm.user = response.data;
////			if(vm.user === undefined || vm.user === null || vm.user == ""){
////				alert("login failed.");
////			} else {
////				alert("login successful.");
////				localStorage.setItem("userId", vm.user.userId);
////				localStorage.setItem("username", vm.user.username);
////				localStorage.setItem("loginToken", vm.user.loginToken);
////				localStorage.setItem("user", JSON.stringify(vm.user));
////				$window.location.href = "/Prototype/#/";
////			}
////		});
//		//location.reload();
//	};
	
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