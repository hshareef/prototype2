/**
 * 
 */
app.service('LookupService',function($http, ConfigService, $q){
	
	//var mappedLookups = {};
	
	this.getTestMessage = function(){
		return "You are connected to the lookupService!";
	};
	
//	this.getOptionById = function(lookupType, id) {
//		var option = (this.mappedLookups[lookupType])[id];
//		return option;
//	};
	
//	function addMappedLookup (lookupType, lookups){
//		var lookupObj = {};
//		for (var i = 0 ; i < lookups.length ; i++) {
//			lookupObj[lookups[i].id] = lookups[i];
//		}
//		mappedLookups[lookupType] = lookupObj;
//	};
	
	this.getMappedLookup = function (lookups){
		var lookupObj = {};
		for (var i = 0 ; i < lookups.length ; i++) {
			lookupObj[lookups[i].id] = lookups[i];
		}
		return lookupObj;
	};
	
	this.getLookup = function(lookupType){
		var deferred = $q.defer();
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/lookup/" + lookupType)
		 .then(function(response){
			 lookups = response.data;
			 //addMappedLookup(lookupType, lookups);
			 console.log(lookups);
			 deferred.resolve(lookups);
		 });
		 return deferred.promise;
	};
	
});