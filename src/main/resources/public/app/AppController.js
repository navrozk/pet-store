(function() {
	
	'use strict';

	angular.module('petStoreApp').controller('AppController', [
	    '$scope', '$q', 'authService', '$location', '$rootScope', function ($scope, $q, authService, $location, $rootScope) {

		$rootScope.$on("$routeChangeStart", function (event, next) {
			if (next.originalPath == '/login') {
				authService.resetUser();
				return;
			}

			if (!authService.currentUser() || !authService.currentUser().authenticated) {
				authService.resetUser();
				$location.url('/login');
			}
		});
	}]);
	
})();
