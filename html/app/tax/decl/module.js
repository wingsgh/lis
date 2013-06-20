// angular.module('decl',['ngResource','DeclController'],function(
//     $routeProvider,$locationProvider) {
//     var declPath = "/";
//     $routeProvider. 
//         when('/',    {controller: 'ListCtrl',  templateUrl: declPath + "list.html"})// .
//         // when('/new', {controller: CreateCtrl, templateUrl: declPath +"detail.html"}).
//         // when('/print/:id', {controller:PrintCtrl,templateUrl: declPath + "print.html"}).
//         // when('/edit/:id', {controller: EditCtrl,  templateUrl: declPath + "detail.html"})
//     ;
//     $routeProvider.otherwise( { redirectTo: '/'});
// });
var decl = angular.module('decl',['ngResource']);

decl.factory('Decl', function($resource){
    var Decl = $resource('http://localhost/decls/:id',{},
                         {"save"  : {method:"POST",isArray: true},
                          "get" : {method:"GET",isArray: true},
                          "update" : {method:"PUT"},
                          "remove" : {method:"DELETE"}});
    Decl.prototype.update = function(cb) {
        return Decl.update({id: this.id}, angular.extend({}, this, {id:undefined}), cb);
    };
    
    Decl.prototype.destroy = function(cb) {
        return Decl.remove({id: this.id}, cb);
    };

    return Decl;
});

decl.controller('ListCtrl', function($scope, Decl){
     $scope.decls = Decl.query();
});
decl.controller('CreateCtrl',function($scope,$location,Decl,$http){

    $http.get('http://localhost/regions')
	.success(function(res) {
	    $scope.regions = res;
	});
    
    $scope.save = function(){
	Decl.save($scope.decl,function(){
	    $location.path('/');
	});
    };
});


decl.config(function($routeProvider,$locationProvider){
    var declPath = "tax/decl/";
    $routeProvider. 
        when('/',    {controller: 'ListCtrl',  templateUrl: declPath + "list.html"}) .
        when('/new', {controller: 'CreateCtrl', templateUrl: declPath +"detail.html"})
        // when('/print/:id', {controller:PrintCtrl,templateUrl: declPath + "print.html"}).
        // when('/edit/:id', {controller: EditCtrl,  templateUrl: declPath + "detail.html"})
    ;
    $routeProvider.otherwise( { redirectTo: '/'});
});
