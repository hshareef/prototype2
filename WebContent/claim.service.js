/**
 * 
 */
claimApp.service('ClaimService',function($window){

	
	this.claimService = {
			
		testServiceObject: function(){
			alert("inside the test service object");
		},
	
		openClaim: function(claimId){
			$window.location.href = '/Prototype/createForm.html?claimId=' + claimId;
		}
			
	};
	return this.claimService;
});