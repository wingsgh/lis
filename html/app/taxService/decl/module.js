var decl = angular.module('decl',['ngResource']);

decl.factory('Decl', function($resource){
    var Decl = $resource(lis.path.service + 'decls/:id',{},
                         {"save"  : {method:"POST"},
                          "get" : {method:"GET"},
                          "update" : {method:"PUT"},
                          "remove" : {method:"DELETE"}});
    Decl.prototype.update = function(cb) {
        return Decl.update({id: this._id}, angular.extend({}, this, {id:undefined}), cb);
    };
    
    Decl.prototype.destroy = function(cb) {
        return Decl.remove({id: this._id}, cb);
    };

    return Decl;
});


decl.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});

decl.controller('ListCtrl', function($scope, Decl){

    $scope.decls = Decl.query();
    
    //paging
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.numberOfPages = function(){
        return Math.ceil($scope.decls.length/$scope.pageSize);                
    };
});
decl.controller('CreateCtrl',function($scope,$location,Decl,$http,$filter){

    $http.get(lis.path.service + 'tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get(lis.path.service + 'regions') .success(function(res) {
	$scope.regions = res;
    });
    
    //set default values
    var decl = {};
    var today = new Date();

    decl.occurDate = decl.declDate = $filter('date')(today,'yyyy-MM-dd');
    $scope.decl = decl;

    $scope.save = function(){
	Decl.save($scope.decl,function(){
	    $location.path('/decl/list');
	});
    };
});


decl.controller('EditCtrl',function($scope,$location,Decl,$routeParams,$http){
    
    $http.get(lis.path.service + 'tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get(lis.path.service + 'regions').success(function(res) {
	$scope.regions = res;
    });

    var self = this;   
    Decl.get({id: $routeParams.id}, function(decl) {
        self.original = decl;
        $scope.decl = new Decl(self.original);
    });

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.decl);
    };

    $scope.destroy = function() {
        self.original.destroy(function() {
            $location.path('/list');
        });
    };

    $scope.save = function() {
        $scope.decl.update(function() {
            $location.path('/');
        });
    };
});

decl.controller('PrintCtrl', function($scope,$routeParams,$location,$http,$filter){
    $http.get(lis.path.service + 'taxes-detail/' + $routeParams.id).success(function(taxesDetail){
	for (var i in taxesDetail){
	    if(taxesDetail[i].class == "资源税"){
                taxesDetail[i].taxBasis += "吨";
                taxesDetail[i].rate = "1元/吨";
	    }
	    else {
                taxesDetail[i].taxBasis += "元";
                taxesDetail[i].rate = 100 * taxesDetail[i].rate + "%";
	    }
        }
	$scope.taxesDetail = taxesDetail;
	
	$http.get(lis.path.service + 'decls/' + $routeParams.id).success(function(decl){
	    $scope.taxesBelongDate = lis.misc.getTaxesBelongDate(decl.occurDate);
	    decl.declDate = $filter('date')(new Date(decl.declDate),'yyyy' + '年MM月dd日');

	    $scope.decl = decl;
	    
	    $('#print').printThis({loadCSS: "decl/print.css"});   
	    $location.path('/decl/list');
	});
    });
});

// filter
decl.filter('startFrom', function() {
    return function(input, start) {
        start = +start; 
        return input.slice(start);
    }
});

decl.config(function($routeProvider,$locationProvider){
    var declPath = "decl/";
    var root = "/decl";
    $routeProvider. 
        when(root + '/list',    {controller: 'ListCtrl',  templateUrl: declPath + "list.html"}) .
        when(root + '/new', {controller: 'CreateCtrl', templateUrl: declPath +"detail.html"}).
        when(root + '/print/:id', {controller:'PrintCtrl',templateUrl: declPath + "print.html"}).
	when(root + '/edit/:id', {controller: 'EditCtrl', templateUrl: declPath + "detail.html"}) ;
});
