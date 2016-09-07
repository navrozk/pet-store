(function() {
	
	'use strict';

	angular.module('petStoreApp').controller('PetController', [
	    '$scope', '$uibModal', 'petService', '$location', 'authService', '$q', function ($scope, $uibModal, petService, $location, authService, $q) {
	    
	    $scope.categories = [ { id: 1, name: 'DOG' }, { id: 2, name: 'CAT' }, { id: 3, name: 'FISH' }, { id: 4, name: 'HAMSTER' } ];
	    
	    $scope.tags = [ { id: 1, name: 'TORONTO' }, { id: 2, name: 'OTTAWA' }, { id: 3, name: 'MONTREAL' }, { id: 4, name: 'GTA' } ];
	    
	    $scope.statuses = [ "available", "pending", "sold" ];

		$scope.init = function () {
			$scope.pets = [];
			$scope.query = null;
			$scope.loadPets();
		}

		$scope.loadPets = function () {
			petService.readAll()
				.then(function (response) {
					$scope.pets = response.data;
					$scope.dataRetrieved = true;
				});
		}
		
		$scope.findPetById = function () {
			petService.read($scope.query)
				.then(function (response) {
					$scope.pets = [response.data];
					$scope.dataRetrieved = true;
				}, function() {
					$scope.dataRetrieved = false;
				});
		}

		$scope.deletePet = function (pet) {
			petService.deletePet(pet.id).then(function (response) {
				var index = $scope.pets.indexOf(pet);
				$scope.pets.splice(index, 1);
			});
		}

		var handleError = function (response) {
			$scope.serverErrors = response.data;
		};

		$scope.add = function () {
			$scope.pet = {};
			$scope.upload = {};
			$scope.serverErrors = {};

			$scope.performAction = function () {
				
				var pet = {
				  "id": null,
				  "category": $scope.pet.category,
				  "name": $scope.pet.name,
				  "photoUrls": [
				    $scope.pet.photoUrls
				  ],
				  "tags": [
				    $scope.pet.tag
				  ],
				  "status": $scope.pet.status
				};
				
				petService.create(pet)
					.then(function (response) {
						$scope.pet.url = $scope.upload.url;
						$scope.pet.id = response.data;
						$scope.pets.push($scope.pet);
						$scope.modalInstance.close();
					}, handleError);
			};

			$scope.modalInstance = $uibModal.open({
				templateUrl: 'addPetModal.html',
				windowClass: 'settings-modal',
				scope: $scope
			});

			$scope.$on('$destroy', function () {
				try {
					$scope.modalInstance.close();
				} catch (e) {
				}
			});

			$scope.handleError = function (response) {
				$scope.errorHandler.serverErrors = response;
			};
		};

		$scope.$watch('query', function (newValue, oldValue) {
			if (oldValue != newValue) {
				if (newValue) {
					$scope.findPetById(newValue);
				} else {
					$scope.loadPets();
				}
			}
		});

		$scope.logout = function () {
			$location.path('/login');
		};

		$scope.hasPermission = function (permission) {
			if (!authService.currentUser()) {
				return false;
			}
			return authService.currentUser().authorities.indexOf(permission) > -1;
		}

		$scope.signedIn = function () {
			return authService.currentUser();
		}
		
	}]);
	
})();