/**
 * 
 */

claimApp.directive('argumentSection', function(){
	
	var directive = {};
	//directive.restrict = 'E';
	
	
	directive.scope = {
			claimStatement: '=',
			argument: '=',
			index: '='
	};
	
	directive.templateUrl = "claim/directives/argumentSection.template.html";
	
	directive.controller = ['$scope', 'ClaimService', function($scope, ClaimService){
		
		$scope.test = function() { 
			alert("inside the directive controller");
			alert(scope.claimStatement);
			//ClaimService.testService();
			ClaimService.testServiceObject();
		};
		
		$scope.openClaim = function(claimId) { 
			ClaimService.openClaim(claimId);
		};
		
	}];
	
	
	return directive;
	
	
	
});