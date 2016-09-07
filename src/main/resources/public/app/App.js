(function() {
	
	'use strict';

	angular.module('petStoreApp', [
		'ngRoute', 'ui.bootstrap', 'ngAnimate', 'myApp.version'
	]).config(function($routeProvider, $httpProvider) {
		$routeProvider.when('/pet-store', {
			templateUrl : 'petPage.html',
			controller : 'PetController',
			reloadOnSearch : false
		}).when('/login', {
			templateUrl : 'login.html',
			controller : 'LoginController',
		}).otherwise('/pet-store');

		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	});
	
})();