var app = angular.module('Breakdown',['angular-loading-bar']);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";

app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	
    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
   	cfpLoadingBarProvider.latencyThreshold = 20;
}]);


var brbox = [
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
	"texa":"Daily Breakdown",
	"active":true
},
{
	"urlb":urlip+"account/detailSGL/list",
	"texa":"Daily Single"
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
}];
app.controller("breakctrl",function(){
		this.brbox = brbox;
});
var dobox = [
{"worda":"",
"wordb":"人民币元RMB",
"wordc":"原始投注额TotalInvestment",
"wordd":"交易额Investment",
"wordn":"caiex加奖额",
"worde":"中奖金额Payout",
"wordf":"盈利率Payout Ratio",
"wordg":"中奖交易额Amt.Winning Unit",
"wordh":"平均奖金Average Winning Odds"},
];

app.controller("brdownctrl",function($scope,$http){
	this.dobox = dobox;
	var _get = function(year,month,day){
		$http.get(urlip +'account/dailyFinancialDetail/queryAll?year='+year+'&month='+month+'&day='+day)
		.success(function(data){
			if(data){
				$scope.dnbox = data.modelList;
				$scope.dnboxBk=data.basketBallModelList
				$scope.FBtotal = data.FBtotal;
				$scope.BKtotal = data.BKtotal;
				$scope.total = data.total;
			}else{
				alert("加载失败")
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
			console.log(time);
			if(sort.length ==3){
				year=sort[0];
				month=sort[1];
				day=sort[2];
				console.log(year,month,day);
			}else if(sort.length ==2){
				year=sort[0];
				month=sort[1];
			}else{
				year=sort;
			}
			console.log(year,month,day);
			window.open(urlip +'account/dailyFinancialDetail//dailyFinancialDetailExcel?year='+year+"&month="+month+"&day="+day);
		} 
	}
});










