var app = angular.module('single',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var gbox = [
{
	"urlb":"errorTicket.html",
	"texa":"错误票信息"
},
{
	"urlb":"allup.html",
	"texa":"Daily Allup"
},
{
	"urlb":"breakdown.html",
	"texa":"Daily Breakdown"
},
{
	"urlb":"single.html",
	"texa":"Daily Single",
	"active":true
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
app.controller("singctrl",function(){
		this.gbox = gbox;
});

app.controller("glectrl",function($scope,$http){
	var _get = function(year,month,day){
		$http.get(urlip +'account/detailSGL/queryAll?year='+year+'&month='+month+'&day='+day)
		.success(function(data){
			if(data){
				$scope.singlebox = data.modelList;
				$scope.total = data.total;
			}else{
				alert("error")
			};
		}).error(function(data){
			alert("error")
		})
	}
	var time =  new Date();
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	$("#d12").val(year+"-"+month+"-"+day);
	//初始化第一页 
	_get(year,month,day); 
	$scope.data = function(){
		var	year = "";
		var	month = "";
		var	day = "";
		var data = $("#d12").val();
		var sort = data.split("-");
		if(sort.length ==3){
			year=sort[0];
			month=sort[1];
			day=sort[2];
		}else if(sort.length ==2){
			year=sort[0];
			month=sort[1];
		}else{
			year=sort;
		}
		_get(year,month,day); 	
	}
//		_get();     //初始化
//导出功能
	$scope.export=function(){
		var	year = "";
		var	month = "";
		var	day = "";
		var time = $("#d12").val();
		if(time =="" || time ==null){
			layer.msg("请先选择要导出的日期");
		}else{
			var sort = time.split("-");
			if(sort.length ==3){
				year=sort[0];
				month=sort[1];
				day=sort[2];
			}else if(sort.length ==2){
				year=sort[0];
				month=sort[1];
			}else{
				year=sort;
			}
			window.open(urlip +'account/detailSGL/detailSGLExcel?year='+year+"&month="+month+"&day="+day);
		} 
	}
});










