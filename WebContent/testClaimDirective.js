/**
 * 
 */


app.directive('tripleButtonTwo', function(){
	
	var directive = {};
	//directive.restrict = 'E';
	
	
	directive.scope = {
			name: '@'	
	};
	
	directive.template = '<button>{{name}}</button>';
	
	return directive;
	
	
	
});