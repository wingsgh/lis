angular.module('DeclService', ['ngResource']).factory('Decl', function($resource){
    var Decl = $resource('http://localhost:8000/decl/:id',{},
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

