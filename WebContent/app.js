
var app = angular.module('appModule', ['ngRoute', 'ui.tree']);


app.config(function($routeProvider) {
  $routeProvider

//  .when('/', {
//    templateUrl : 'home/home.html',
//    controller  : 'HomeCtrl as vm'
//  })
  .when('/home', {
    templateUrl : 'home/home.html',
    controller  : 'HomeCtrl as vm'
  })
  
    .when('/test', {
    templateUrl : 'test.html',
    controller  : 'TestCtrl as vm'
  })
  
    .when('/claim', {
    templateUrl : 'claim/claim.html',
    controller  : 'ClaimCtrl as vm'
  })

  .when('/claim/:claimId', {
    templateUrl : 'claim/claim.html',
    controller  : 'ClaimCtrl as vm'
  })
  
//  .when('/oppositeClaims', {
//    templateUrl : 'claim/claim.html',
//    controller  : 'ClaimCtrl as vm'
//  })
  
  .when('/login', {
    templateUrl : 'login/login.html',
    controller  : 'LoginCtrl as vm'
  })
  
    .when('/profile', {
    templateUrl : 'profile/profile.html',
    controller  : 'ProfileCtrl as vm'
  })
  
  .otherwise({redirectTo: '/home'});
});

