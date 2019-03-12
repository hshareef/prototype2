
var testApp = angular.module('testModule', ['ngRoute', 'ui.tree']);


testApp.config(function($routeProvider) {
  $routeProvider
  
    .when('/test', {
    templateUrl : 'test.html',
    controller  : 'TestCtrl as vm'
  })
 
  
  ;//.otherwise({redirectTo: '/home'});
});

