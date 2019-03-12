/**
 * 
 */
app.service('ConfigService',function(){
	
	var test = 5;
	var settings = {
			url: 'http://localhost:8080'
	};
	
	this.getSettings = function(){
		return settings;
	};
	
});