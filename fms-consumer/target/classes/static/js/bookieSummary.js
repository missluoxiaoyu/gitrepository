var app = angular.module('finance',['angular-loading-bar']);
var urlip = "http://"+document.location.host+"/";


app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	//var server = "http://"+document.location.host+"/";
    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
   	cfpLoadingBarProvider.latencyThreshold = 20;
}]);


var nbox = [
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
	"texa":"Daily Single"
},
{
	"urlb":urlip+"account/channelStatistics/list",
	"texa":"渠道统计报表",
},

{
	"urlb":urlip+"account/summary/list",
	"texa":"Summary",
	"active":true
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
app.controller("financtrl",function(){
		this.nbox = nbox;
});
var dobox = [
{"worda":"",
"wordb":"场次数量NO. Match",
"wordd":"交易额 Investment 人民币 RMB",
"worde":"中奖金额 Payout 人民币 RMB",
"wordf":"盈利率 Payout Ratio %"}
];

app.controller("fincectrl",function($scope,$http){
	this.dobox = dobox;
		
	var woDate = new Date();
	this.date = woDate.toLocaleDateString(); 
		
	var month = woDate.getMonth()+1;

	
	this.month = (month<10 ? "0"+month:month);
	
	var time,week,checkDate = new Date(new Date());
    checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7));
    time = checkDate.getTime();
    checkDate.setMonth(0);
    checkDate.setDate(1);
	week=Math.floor(Math.round((time - checkDate) / 86400000) / 7) + 1;
			
	this.weeking =week;
	
	
	
	var _get = function(year,month,day){
		$http.get(urlip +'account/bookieSummary/queryAll?year='+year+'&month='+month+'&day='+day)
		.success(function(data){
			if(data){
				$scope.modelToday = data.modelToday;
				$scope.modelWeek = data.modelWeek;
				$scope.modelMonth = data.modelMonth;
				$scope.modelYear = data.modelYear;
				$scope.financebox = data.modelList;
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
		console.log(year,month,day,"4");
		
		week =getYearWeek(year, month, day);
		//var weekofyear =  (((new Date())-(new Date("2017-12-27")))/(24*60*60*7*1000)|0) +1
		//alert(weekofyear);
		//this.weekk = weekofyear;
		//this.month = month;
		document.getElementById("week").innerText = week;	
		document.getElementById("vor").innerText = month;
		
		
		
		
		
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
			window.open(urlip +'account/bookieSummary/summaryExcel?year='+year+"&month="+month+"&day="+day);
		} 
	}
});


var getYearWeek = function (a, b, c) { 
    var d1 = new Date(a, b-1, c), d2 = new Date(a, 0, 1), 
    d = Math.round((d1 - d2) / 86400000); 
    return Math.ceil((d + ((d2.getDay() + 1) - 1)) / 7); 
};








