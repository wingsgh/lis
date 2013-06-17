define(['angular','ngResource'],function(angular){
    return angular.module('BldService', ['ngResource']).factory('Bld', function($resource){
        var Bld = $resource('http://localhost/bld/:id',{},
                             {"save"  : {method:"POST",isArray:true},
                              "get" : {method:"GET",isArray: true},
                              "update" : {method:"PUT",isArray: true}});
        Bld.prototype.update = function(cb) {
            return Bld.update({id: this.id}, angular.extend({}, this, {id:undefined}), cb);
        };
        return Bld;
    });
});

