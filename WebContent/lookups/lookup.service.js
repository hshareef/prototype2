/**
 * 
 */
app.service('LookupService',function($http, ConfigService, $q){
	
	this.getTestMessage = function(){
		return "You are connected to the lookupService!";
	};
	
	this.getFakeOptions = function(){
		return ['pig', 'ass', 'horse', 'kitten'];
	};
	
//	this.getLookup = function(lookupType){
//		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/lookup/" + lookupType)
//		 .then(function(response){
//			 lookups = response.data;
//			 console.log(lookups);
//			 return lookups;
//		 });
//	};
	
	this.getLookup = function(lookupType){
		var deferred = $q.defer();
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/lookup/" + lookupType)
		 .then(function(response){
			 lookups = response.data;
			 console.log(lookups);
			 deferred.resolve(lookups);
		 });
		 return deferred.promise;
	};
	
});