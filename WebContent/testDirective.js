/**
 * 
 */


testScopeApp.directive('tripleButton', function(){
	
	var directive = {};
	//directive.restrict = 'E';
	
	
	directive.scope = {
			name: '@'	
	};
	
	directive.templateUrl = 'testDirective.template.html';
	
	directive.controller = ['$scope', function($scope){
		
		//var vm = this;//angular.extend(this, $controller('ClaimCtrl', {$scope: $scope}));
		
		$scope.test = function(){
			alert("test");
		};
	}];
	
	return directive;
	
	
	
});