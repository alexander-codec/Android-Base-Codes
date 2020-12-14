	// create the module and name it scotchApp
	var scotchApp = angular.module('kinemarts', ['ngRoute']);

	// configure our routes
	scotchApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/', {
				templateUrl : 'cdn/index/angular/view/index.html',
				controller  : 'mainController'
			})
	});

	// create the controller and inject Angular's $scope
	scotchApp.controller('mainController', function($scope) {
		// create a message to display in our view
		$scope.message = 'Espetacular, um novo futuro espera você!';
	});