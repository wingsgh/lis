var building = angular.module('building',['ngResource']);

building.factory('Building', function($resource){
    var Building = $resource(lis.path.service + 'tax-source/building/:id',{},
                         {"save"  : {method:"POST"},
                          "get" : {method:"GET"},
                          "update" : {method:"PUT"},
                          "remove" : {method:"DELETE"}});
    Building.prototype.update = function(cb) {
        return Building.update({id: this._id}, angular.extend({}, this, {id:undefined}), cb);
    };
    
    Building.prototype.destroy = function(cb) {
        return Building.remove({id: this._id}, cb);
    };
    return Building;
});

building.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});

building.controller('BuildingListCtrl', function($scope, Building){

    $scope.buildings = Building.query();

    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.numberOfPages = function(){
        return Math.ceil($scope.building.length/$scope.pageSize);                
    };
});

building.controller('BuildingCreateCtrl',function($scope, Building,$http,$location){
    $http.get(lis.path.service + 'tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get(lis.path.service + 'regions').success(function(res) {
	$scope.regions = res;
    });

    $scope.save = function(){
	Building.save($scope.building,function(){
	    $location.path('/building/list');
	});
    };
});

building.controller('BuildingEditCtrl',function($scope,$location,Building,$routeParams,$http){
    
    $http.get(lis.path.service + 'tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get(lis.path.service + 'regions').success(function(res) {
	$scope.regions = res;
    });

    var self = this;   
    Building.get({id: $routeParams.id}, function(building) {
        self.original = building;
        $scope.building = new Building(self.original);
    });

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.building);
    };

    $scope.destroy = function() {
        self.original.destroy(function() {
            $location.path('/building/list');
        });
    };

    $scope.save = function() {
        $scope.building.update(function() {
            $location.path('/building/list');
        });
    };
});

building.config(function($routeProvider,$locationProvider){
    var path = "building/";
    var root = "/building";
    $routeProvider. 
        when(root + '/list',    {controller: 'BuildingListCtrl',  templateUrl: path + "list.html"}). 
        when(root + '/new', {controller: 'BuildingCreateCtrl', templateUrl: path +"detail.html"}).
        // when(root + '/print/:id', {controller:'DeclPrintCtrl',templateUrl: path + "print.html"}).
	when(root + '/edit/:id', {controller: 'BuildingEditCtrl', templateUrl: path + "detail.html"})
    ;
});
