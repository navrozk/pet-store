(function() {
	
	angular.module('petStoreApp').controller('LoginController', [
	    '$scope', '$location', 'authService', '$http', '$rootScope', function ($scope, $location, authService, $http, $rootScope) {
		
		$scope.dataLoading = false;

		$scope.login = function () {
			
			$scope.dataLoading = true;
			
			authService.login($scope.credentials)
				.then(function (data) {
					if (data.authenticated) {
						$location.path("/pet-store");
						$scope.error = false;
					} else {
						$location.path("/login");
						$scope.error = true;
					}
					$scope.dataLoading = false;
				}, function () {
					$location.path("/login");
					$scope.error = true;

					$scope.dataLoading = false;
				});
		};
		
	}]);
	
})();