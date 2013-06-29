//module.js
var levy = angular.module('levy',['ngResource']);

levy.factory('Levy', function($resource){
    var Levy = $resource(lis.path.service + 'levy/:id',{},
                         {"save"  : {method:"POST"},
                          "get" : {method:"GET"},
                          "update" : {method:"PUT"},
                          "remove" : {method:"DELETE"}});
    Levy.prototype.update = function(cb) {
        return Levy.update({id: this._id}, angular.extend({}, this, {id:undefined}), cb);
    };
    
    Levy.prototype.destroy = function(cb) {
        return Levy.remove({id: this._id}, cb);
    };

    return Levy;
});


levy.controller('LevyListCtrl',function($scope,Levy){
    $scope.levyes = Levy.query();

    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.numberOfPages = function(){
        return Math.ceil($scope.levyes.length/$scope.pageSize);                
    };
});

levy.controller('LevyCreateCtrl',function($scope,$location,$filter,Levy,$http){
    
    var levy = {};

    levy.payDate  = $filter('date')(new Date(),'yyyy-MM-dd');
    $scope.levy = levy;
    
    $scope.change = function(){
	$http.get('http://localhost/service/levy/search/' + $scope.levy.sn)
	    .success(function(levy){
		$scope.levy = levy;
	    });
    };
    
    $scope.save = function(){
	Levy.save($scope.levy,function(){
	    $location.path('/levy/list');
	});
    };
});

levy.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});

levy.config(function($routeProvider,$locationProvider){
    var levyPath = "levy/";
    var root ="/levy";
    $routeProvider. 
        when(root + '/list', {controller: 'LevyListCtrl',  templateUrl: levyPath + "list.html"}).
        when(root + '/new', {controller: 'LevyCreateCtrl', templateUrl: levyPath +"detail.html"})
        // when('/print/:id', {controller:'PrintCtrl',templateUrl: declPath + "print.html"}).
	// when('/edit/:id', {controller: 'EditCtrl', templateUrl: declPath + "detail.html"})
    ;
});
