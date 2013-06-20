define( ['angular' ,'./BldService.js'], function(angular){
    var root = "/bld";
    var regions =   ["扎西镇"
                     ,"林凤镇"
                     ,"旧城镇"
                     ,"罗布镇"
                     ,"长安镇"
                     ,"水田镇"
                     ,"庙沟镇"
                     ,"三桃乡"
                     ,"双河乡"
                     ,"高田乡"];
    
    var ListCtrl = function($scope,Bld){
        $scope.blds = Bld.query();
    };
    
    var CreateCtrl =  function CreateCtrl($scope, $location, Bld) {
        $scope.regions  = regions;
        
        $scope.save = function() {
            Bld.save($scope.bld, function(bld) {
                $location.path(root + '/');
            });
        };
    };

    var EditCtrl = function EditCtrl($scope, $location, $routeParams,
                                     Bld,$http) {
        $scope.regions  = regions;
        
        var self = this;   
        Bld.get({id: $routeParams.id}, function(blds) {
            self.original = blds[0];
            $scope.bld = new Bld(self.original);

            
            $http.get('http://localhost:3000/bldDecl/'+ $routeParams.id)
                .success(function(res){
                    $scope.bldDecls = res;
                });
        });

        $scope.isClean = function() {
            return angular.equals(self.original, $scope.bld);
        };

        $scope.save = function() {
            $scope.bld.update(function() {
                $location.path(root);
            });
        };
    };


    
    var templatePath = "../tax/bld/";
    return angular.module('bld',[,'ngResource','BldService'],function($routeProvider,$locationProvider) {
        $routeProvider. 
            when(root + '/',    {controller: ListCtrl,  templateUrl: templatePath + "list.html"}).
            when(root + '/new', {controller: CreateCtrl, templateUrl: templatePath +"detail.html"}).
            when(root + '/edit/:id', {controller: EditCtrl,  templateUrl: templatePath + "detail.html"})
        ;
        //$routeProvider.otherwise( { redirectTo: root +  '/'});
    });
});
