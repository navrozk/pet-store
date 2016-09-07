(function() {
	
	'use strict';

	angular.module('petStoreApp').factory('authService', ['$http', '$q', function ($http, $q) {
		var currUser;

		return {
			login: function (credentials) {
				var fd = new FormData();
				fd.append('username', credentials.username);
				fd.append('password', credentials.password);

				var deferred = $q.defer();

				$http({
					url: '/login',
					method: 'POST',
					headers: {'Content-Type': undefined},
					data: fd
				}).then(function (response) {
					if (response.data.name !== null) {
						currUser = response.data;
					}
					deferred.resolve(currUser);
				}, function () {
					deferred.resolve(null);
				});

				return deferred.promise;
			},
			currentUser: function () {
				return currUser;
			},
			resetUser: function () {
				currUser = null;
			}
		};
		
	}]);
	
})();