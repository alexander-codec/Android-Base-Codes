	// create the module and name it scotchApp
	var scotchApp = angular.module('cloud', ['ngRoute']);

	// configure our routes
	scotchApp.config(function($routeProvider) {
		$routeProvider

			// route for the home page
			.when('/', {
				templateUrl : 'angular/pags/inicio.html',
				controller  : 'mainController'
			})
			// route for the home page
			.when('/wordpress', {
				templateUrl : 'angular/pags/wordpress.html'
			})
			// route for the home page
			.when('/website', {
				templateUrl : 'angular/pags/website.html'
			})
			// route for the home page
			.when('/videos', {
				templateUrl : 'angular/pags/videos.html'
			})
			// route for the home page
			.when('/imagens', {
				templateUrl : 'angular/pags/imagens.html'
			})
			// route for the home page
			.when('/primeiroacesso', {
				templateUrl : 'angular/pags/primeiroacesso.html'
			})
	});

	scotchApp.controller('mainController', function($scope) {
	});