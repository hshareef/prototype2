/**
 * 
 */

testScopeApp.controller('TestScopeCtrl', function($scope) {
	var vm = this;
	
	vm.dog = "fido";
	vm.count = 0;
	vm.test = function(){
		vm.count++;
	};
	
	vm.testObj = {
		things: [{
			name: "jack"
		}, 
		{
			name: "sally"
		}, 
		{
			name: "pope"
		}, 
		{
			name: "cory"
		}]
	};
});
