var CommonModule = angular.module('CommonModule',[]);

CommonModule.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});


//autocomplete use jquery-ui
CommonModule.directive('ac',function($timeout){
    return {
        restrict: 'A',
        link: function(scope, elem, attr, ctrl) {
            elem.autocomplete({
                source: scope[attr.uiItems],
		select: function(event, ui) {
		    var selectedObj = ui.item;
		    alert(selectedObj.value);
		    return false;
		}
            });
        }
    };
});
