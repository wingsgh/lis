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
                         {"save"  : {method:"POST"},
                          "get" : {method:"GET"},
                          "update" : {method:"PUT"},
                          "remove" : {method:"DELETE"}});
    Decl.prototype.update = function(cb) {
        return Decl.update({id: this.id}, angular.extend({}, this, {id:undefined}), cb);
    };
    
    Decl.prototype.destroy = function(cb) {
        return Decl.remove({id: this._id}, cb);
    };

    return Decl;
});


decl.controller('ListCtrl', function($scope, Decl){
     $scope.decls = Decl.query();
});
decl.controller('CreateCtrl',function($scope,$location,Decl,$http){

    $http.get('http://localhost/tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get('http://localhost/regions') .success(function(res) {
	$scope.regions = res;
    });
    
    //set default values
    var decl = {};
    var today = new Date();
    decl.occurDate = decl.declDate = today.toLocaleDateString();
    $scope.decl = decl;

    $scope.save = function(){
	Decl.save($scope.decl,function(){
	    $location.path('/');
	});
    };
});


decl.controller('EditCtrl',function($scope,$location,Decl,$routeParams,$http){
    
    $http.get('http://localhost/tax-objects').success(function(res) {
	$scope.taxObjects = res;
    });
    $http.get('http://localhost/regions') .success(function(res) {
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

decl.controller('PrintCtrl', function($scope,$routeParams, $location, Decl){
    var self = this;
    Decl.get({id: $routeParams.id}, function(decls) {
        var decl = decls[0];        
        var taxes_detail = JSON.parse(decl.taxes_detail); 
        var total_tax = 0;    
        for (var i in taxes_detail){
            if(taxes_detail[i].tax_class == "资源税"){
                taxes_detail[i].tax_basis += "吨";
                taxes_detail[i].rate = "1元/吨";
            }
            else {
                taxes_detail[i].tax_basis += "元";
                taxes_detail[i].rate = 100 * taxes_detail[i].rate + "%";
            }
            
            total_tax += taxes_detail[i].taxes;
        }
        total_tax = total_tax.toFixed(2);
        

        decl.taxes_detail = taxes_detail;
        self.original = decl;
        $scope.decl = new Decl(self.original);
        $scope.total_tax  = total_tax;
        
        var occur_date = new Date(decl.occur_date);
        $scope.taxes_belong_date = misc.getFirstDay(occur_date) + "至" + misc.getLastDay(occur_date) ;
        
        var decl_date = new Date(decl.decl_date);
        $scope.decl_date = decl_date.toLocaleDateString();

        $('#print').printThis({loadCSS: "tax/decl/print.css"});   
        $location.path('/');
});

decl.config(function($routeProvider,$locationProvider){
    var declPath = "tax/decl/";
    $routeProvider. 
        when('/',    {controller: 'ListCtrl',  templateUrl: declPath + "list.html"}) .
        when('/new', {controller: 'CreateCtrl', templateUrl: declPath +"detail.html"}).
        when('/print/:id', {controller:'PrintCtrl',templateUrl: declPath + "print.html"}).
	when('/edit/:id', {controller: 'EditCtrl',  templateUrl: declPath + "detail.html"}) ;
    $routeProvider.otherwise( { redirectTo: '/'});
});
