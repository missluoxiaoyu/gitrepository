var app = angular.module('Allup',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var upbox = [
{
	"urlb":"errorTicket.html",
	"texa":"错误票信息"
},
{
	"urlb":"allup.html",
	"texa":"Daily Allup",
	"active":true
},
{
	"urlb":"breakdown.html",
	"texa":"Daily Breakdown"
},
{
	"urlb":"single.html",
	"texa":"Daily Single"
},
{
	"urlb":"channel.html",
	"texa":"渠道统计报表"
},
{
	"urlb":"finance.html",
	"texa":"Summary"
},
{
	"urlb":"summary.html",
	"texa":"Daily Summary"
},
{
	"urlb":"user.html",
	"texa":"用户管理"
},{
	"urlb":"third.html",
	"texa":"Third"
}];
app.controller("upctrl",function(){
		this.upbox = upbox;
});

//app.factory('allupService', ['$http', '$q', '$location', function($http, $q, $location) {
//	return {
//		queryallup: function() {
//				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
//				$http({
//					method: 'GET',
//					url: 'http://192.168.1.209:8080/account/dailyAllup/queryAll.do?date='
//				}).success(function(data) {
//					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
//				}).error(function(data) {
//					deferred.reject(data); // 声明执行失败，即服务器返回错误  
//				});
//				return deferred.promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API  
//			}
//	};
//}]);
app.controller("alupctrl",function($scope,$http){
	var _get = function(data){  
        $http.get(urlip +'account/dailyAllup/queryAll.do?date='+data)
        .success(function(data){
        	if(data){

//	var list = allupService.queryallup();
//	list.then(function(data){
//		console.log(data.model);
		$scope.model = data.model;
		
//		$scope.hbox = data.listHAD;  //原固定数据
		hbox =data.listHAD;
		$scope.hbox1 = hbox[7];
		$scope.hbox1.local_m = "胜平负 过关HAD";
		$scope.hbox = hbox;
		var str = hbox.splice(7,1);
//		hbox.unshift(str[0]);

		abox =data.listHHAD;
		$scope.abox1 = abox[7];
		$scope.abox1.local_m = "让球胜平负 过关HHAD";
		$scope.abox = abox;
		var str = abox.splice(7,1);
//		abox.unshift(str[0]);

		bbox =data.listHAFU;
		$scope.bbox1 = bbox[7];
		$scope.bbox1.local_m = "HAFU";
		$scope.bbox = bbox;
		var str = bbox.splice(7,1);
//		bbox.unshift(str[0]);

		tbox =data.listTTG;
		$scope.tbox1 = tbox[7];
		$scope.tbox1.local_m = "TTG";
		$scope.tbox = tbox;
		var str = tbox.splice(7,1);
//		tbox.unshift(str[0]);

		cbox =data.listCRS;
		$scope.cbox1 = cbox[7];
		$scope.cbox1.local_m = "CRS";
		$scope.cbox = cbox;
		var str = cbox.splice(7,1);
//		cbox.unshift(str[0]);

		fbox =data.listFCA;
		$scope.fbox1 = fbox[7];
		$scope.fbox1.local_m = "FCA";
		$scope.fbox = fbox;
		var str = fbox.splice(7,1);
//		fbox.unshift(str[0]);
		 
		  }else{  
                alert("加载失败");  
            }  
       })
    }
	var time =  new Date();
	
	var year = time.getFullYear();
	
	var month = time.getMonth()+1;
	
	var day = time.getDate();
	mydate = year+"-"+month+"-"+day;
	$("#d12").val(year+"-"+month+"-"+day);
	//初始化第一页 
	_get(mydate); 
	$scope.data = function(){
		var data = $("#d12").val();
		_get(data); 	
	}
//	},function(data){
//	  	console.log(123)
//	});
});










