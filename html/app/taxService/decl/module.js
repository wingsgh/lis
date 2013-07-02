var decl = angular.module('decl',['ngResource','CommonModule']);

decl.factory('Decl', function($resource){
    var Decl = $resource(lis.path.service + 'decl/:id',{},
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




decl.controller('DeclListCtrl', function($scope, Decl){

    $scope.decls = Decl.query();
    
    //paging
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.numberOfPages = function(){
        return Math.ceil($scope.decls.length/$scope.pageSize);                
    };
});
decl.controller('DeclCreateCtrl',function($scope,$location,Decl,$http,$filter){

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


decl.controller('DeclEditCtrl',function($scope,$location,Decl,$routeParams,$http){
    
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
            $location.path('/decl/list');
        });
    };

    $scope.save = function() {
        $scope.decl.update(function() {
            $location.path('/decl/list');
        });
    };
});

decl.controller('DeclPrintCtrl', function($scope,$routeParams,$location,$filter,Decl){
    //     $http.get(lis.path.service + 'taxes-detail/' + $routeParams.id).success(function(taxesDetail){

    // 	$scope.taxesDetail = taxesDetail;
	
    // 	$http.get(lis.path.service + 'decls/' + $routeParams.id).success(function(decl){
  

    // 	    $scope.decl = decl;
	    

    // 	});
    // });
    var self = this;   
    Decl.get({id: $routeParams.id}, function(decl) {
        self.original = decl;
        $scope.decl = new Decl(self.original);
	
	var taxesDetail = $scope.decl.taxesDetail;
	
	for (var i in taxesDetail){
    	    if(taxesDetail[i].taxClass == "资源税"){
                taxesDetail[i].taxBasis += "吨";
                taxesDetail[i].rate = "1元/吨";
    	    }
    	    else {
                taxesDetail[i].taxBasis += "元";
                taxesDetail[i].rate = 100 * taxesDetail[i].rate + "%";
    	    }
        }

	$scope.taxesBelongDate = lis.misc.getTaxesBelongDate($scope.decl.occurDate);
    	$scope.decl.declDate = $filter('date')(new Date(decl.declDate),'yyyy' + '年MM月dd日');
	
	$('#print').printThis({loadCSS: "decl/print.css"});   
    	$location.path('/decl/list');
    });
});



decl.config(function($routeProvider,$locationProvider){
    var declPath = "decl/";
    var root = "/decl";
    $routeProvider. 
        when(root + '/list',    {controller: 'DeclListCtrl',  templateUrl: declPath + "list.html"}) .
        when(root + '/new', {controller: 'DeclCreateCtrl', templateUrl: declPath +"detail.html"}).
        when(root + '/print/:id', {controller:'DeclPrintCtrl',templateUrl: declPath + "print.html"}).
	when(root + '/edit/:id', {controller: 'DeclEditCtrl', templateUrl: declPath + "detail.html"}) ;
});
