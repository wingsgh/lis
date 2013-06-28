//module.js
var levy = angular.module('levy',['ngResource']);

levy.controller('MainCtrl',function($scope){
    $scope.change = function(){
	$scope.decl =$scope.sn;
    };
});

levy.config(function($routeProvider,$locationProvider){
    var levyPath = "levy/";
    var root ="/levy";
    $routeProvider. 
        when(root + '/main',    {controller: 'MainCtrl',  templateUrl: levyPath + "main.html"})
        // when('/new', {controller: 'CreateCtrl', templateUrl: declPath +"detail.html"}).
        // when('/print/:id', {controller:'PrintCtrl',templateUrl: declPath + "print.html"}).
	// when('/edit/:id', {controller: 'EditCtrl', templateUrl: declPath + "detail.html"})
    ;
});
