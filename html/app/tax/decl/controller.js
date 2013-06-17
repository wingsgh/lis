var DeclController = angular.module('DeclController',['DeclService']);

DeclController.controller('ListCtrl', function($scope, Decl){
     $scope.decls = Decl.query();
});





// var regions =   ["扎西镇"
//                  ,"林凤镇"
//                  ,"旧城镇"                     
//                  ,"罗布镇"
//                  ,"长安镇"
//                  ,"水田镇"
//                  ,"庙沟镇"
//                  ,"三桃乡"
//                  ,"双河乡"
//                  ,"高田乡"];

// var tax_object_classes =  ["建筑业－建筑"
//                            ,"建筑业－安装"
//                            ,"建筑业－修缮"
//                            ,"建筑业－装饰"
//                            ,"建筑业－建筑－校安工程"
//                            ,"建筑业－其他工程作业"
//                            ,"服务业－其他"
//                            ,"服务业－工程监理"];

// var ListCtrl = function($scope,Decl){
//     $scope.decls = Decl.query();
// };

// var CreateCtrl =  function CreateCtrl($scope, $location, Decl,$http) {
    
//     var today = misc.getToday();

//     $scope.decl = {occur_date:today, decl_date:today};

//     $scope.occur_regions = regions;
//     $scope.tax_object_classes = tax_object_classes;

//     $scope.names = function() {
//         $http.get('http://localhost/bld/').success(function(res){
//             var names = [];
//             for (var i in res){
//                 names.push(res[i].name);
//             }
//             return names;
//         });
//     };

//     $scope.save = function() {
//         Decl.save($scope.decl, function(decls) {
//             var decl = decls[0];
//             $http.defaults.useXDomain = false;
//             $http.post('http://localhost/bldDecl/',
//                        {"decl":decl.id,"bld":$scope.bld}).success(function(){});
//             $location.path('/');
//         });
//     };
// };

// var EditCtrl = function EditCtrl($scope, $location, $routeParams, Decl) {
    
//     $scope.occur_regions = regions;
//     $scope.tax_object_classes = tax_object_classes;
    
//     var self = this;   
//     Decl.get({id: $routeParams.id}, function(decls) {
//         self.original = decls[0];
//         $scope.decl = new Decl(self.original);
//     });

//     $scope.isClean = function() {
//         return angular.equals(self.original, $scope.decl);
//     };

//     $scope.destroy = function() {
//         self.original.destroy(function() {
//             $location.path('/list');
//         });
//     };

//     $scope.save = function() {
//         $scope.decl.update(function() {
//             $location.path('/');
//         });
//     };
// };

// var PrintCtrl =  function PrintCtrl($scope,$routeParams, $location, Decl) {
//     var self = this;   
//     Decl.get({id: $routeParams.id}, function(decls) {
//         var decl = decls[0];        
//         var taxes_detail = JSON.parse(decl.taxes_detail); 
//         var total_tax = 0;    
//         for (var i in taxes_detail){
//             if(taxes_detail[i].tax_class == "资源税"){
//                 taxes_detail[i].tax_basis += "吨";
//                 taxes_detail[i].rate = "1元/吨";
//             }
//             else {
//                 taxes_detail[i].tax_basis += "元";
//                 taxes_detail[i].rate = 100 * taxes_detail[i].rate + "%";
//             }
            
//             total_tax += taxes_detail[i].taxes;
//         }
//         total_tax = total_tax.toFixed(2);
        

//         decl.taxes_detail = taxes_detail;
//         self.original = decl;
//         $scope.decl = new Decl(self.original);
//         $scope.total_tax  = total_tax;
        
//         var occur_date = new Date(decl.occur_date);
//         $scope.taxes_belong_date = misc.getFirstDay(occur_date) + "至" + misc.getLastDay(occur_date) ;
        
//         var decl_date = new Date(decl.decl_date);
//         $scope.decl_date = decl_date.toLocaleDateString();

//         $('#print').printThis({loadCSS: "tax/decl/print.css"});   
//         $location.path('/');
//     });
// };
