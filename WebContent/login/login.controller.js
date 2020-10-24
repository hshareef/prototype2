
app.controller('LoginCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService) {
	
	var vm = this;
	vm.createNewAcctFlag = false;
	vm.passwordsMatch = true;
	
	vm.login = function () {
		vm.user = {};
		vm.user.username = 'fartman';
		vm.user.password = 'ppppfffft';
		alert("trying to log in..." + vm.user.username + " : " +  vm.user.password);
		console.log(vm.user);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/debug", vm.user)
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
		vm.user = {};
		vm.user.username = 'fartman';
		vm.user.password = 'ppppfffft';
		alert("trying to log in..." + vm.user.username + " : " +  vm.user.password);
		console.log(vm.user);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/debug", vm.user)
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
	
//	vm.createNewUserAcct = function () {
//		console.log(vm.newUser);
//		alert("yoyoy");
//		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/debug", vm.newUser)
//		.then(function(response){
//			alert("got promise");
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
//		});
//		
//	};
	
//	vm.createNewUserAcct = function(){
//		vm.checkPasswordsMatch();
//		if(!vm.passwordsMatch){
//			alert("passwords don't match");
//		} else {
//			var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/login2", vm.newUser)
//			.then(function(response){
//				alert("we got a response");
////				vm.user = response.data;
////				if(vm.user === undefined || vm.user === null){
////					alert("user creation failed.");
////				}
////				else{
////					alert("user created successfully.");
////					localStorage.setItem("userId", vm.user.userId);
////					localStorage.setItem("username", vm.user.username);
////					localStorage.setItem("user", JSON.stringify(vm.user));
////					$window.location.href = "/Prototype/#/";
////				}
//			});
//		}
//	};
	
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