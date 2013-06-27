//
var lis = {};

lis.misc = {
    getTaxesBelongDate : function(date){
	var beginDate = new Date(date);
	
	var year = beginDate.getFullYear();
	var month = beginDate.getMonth() + 1;
	if (month<10){
            month = "0"+month;
	}
	var begin = year + "年" + month + "月" + 01 + "日";

	var endDate = new Date(year,month,0);
	var end = year + "年" + month + "月" + endDate.getDate() + "日";

	return begin + "至" + end;
    }
};

lis.path = {
    service : 'http://localhost/service/'
};
