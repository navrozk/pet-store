(function() {
	
	'use strict';

	angular.module('petStoreApp').factory('petService', ['$http', function ($http) {
		return {
			get: function (id) {
				return $http({
					url: '/api/pet/' + id,
					method: 'GET'
				});
			},
			readAll: function () {
				return $http({
					url: '/api/pet',
					method: 'GET'
				});
			},
			read: function (query) {
				return $http({
					url: '/api/pet/' + query,
					method: 'GET'
				});
			},
			create: function (pet) {
				return $http({
					url: '/api/pet',
					data: pet,
					method: 'POST'
				});
			},
			deletePet: function (id) {
				return $http({
					url: '/api/pet/' + id,
					method: 'DELETE'
				});
			}
		}
	}]);
	
})();