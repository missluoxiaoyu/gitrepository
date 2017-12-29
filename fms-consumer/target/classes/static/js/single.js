var app = angular.module('single',['angular-loading-bar']);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";

app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	var server = "http://"+document.location.host+"/";
    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
   	cfpLoadingBarProvider.latencyThreshold = 20;
}]);



var gbox = [
{
	"urlb":urlip+"account/errorTicket/list",
	"texa":"错误票信息"
},
{
	"urlb":urlip+"account/dailyAllup/list",
	"texa":"Daily Allup"
},
{
	"urlb":urlip+"account/dailyFinancialDetail/list",
	"texa":"Daily Breakdown"
},

{
	"urlb":urlip+"account/detailSGL/list",
	"texa":"Daily Single",
	"active":true
},


{
	"urlb":urlip+"account/channelStatistics/list",
	"texa":"渠道统计报表",
},

{
	"urlb":urlip+"account/summary/list",
	"texa":"Summary"
},
{
	"urlb":urlip+"account/dailyCollectStatements/list",
	"texa":"Daily Summary"
},
{
	"urlb":urlip+"account/orderManage/list",
	"texa":"订单管理"
},{
	"urlb":urlip+"account/orderTicket/list",
	"texa":"Third"
}
,
{
	"urlb":urlip+"account/bookieSummary/list",
	"texa":"虚拟Summary"
},
{
	"urlb":urlip+"account/bookieAgent/list",
	"texa":"虚拟渠道"
},
{
	"urlb":urlip+"account/bookieOrder/list",
	"texa":"虚拟订单"
},{
	"urlb":urlip+"account/bookieSingle/list",
	"texa":"虚拟single"
}


];
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
	
	var sel=document.getElementById("state");
	var pid = sel.options[sel.selectedIndex].value;
	
	$("#d12").val(year+"-"+month+"-"+day);
	//初始化第一页 
	//_get(year,month,day); 
	$scope.data = function(){
		var	year = "";
		var	month = "";
		var	day = "";
		
		var sel=document.getElementById("state");
		var pid = sel.options[sel.selectedIndex].value;
		
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
		
		
		//_get(year,month,day,pid); 	
		
		if(pid==2){
			window.location.href = urlip +'account/basketBallDetailSGL/list';
			
		}else{
			_get(year,month,day,pid); 	
		}
		
		
		
	}
//		_get();     //初始化
//导出功能
	$scope.export=function(){
		var	year = "";
		var	month = "";
		var	day = "";
		
		//var sel=document.getElementById("state");
		//var pid = sel.options[sel.selectedIndex].value;
		
		
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










