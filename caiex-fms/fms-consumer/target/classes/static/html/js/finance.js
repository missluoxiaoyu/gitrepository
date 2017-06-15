var app = angular.module('finance',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var nbox = [
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
	"texa":"Daily Single"
},
{
	"urlb":"channel.html",
	"texa":"渠道统计报表"
},
{
	"urlb":"finance.html",
	"texa":"Summary",
	"active":true
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
app.controller("financtrl",function(){
		this.nbox = nbox;
});
var dobox = [
{"worda":"",
"wordb":"场次数量",
"wordc":"原始投注额",
"wordd":"交易额",
"worde":"中奖金额",
"wordf":"盈利率"},
{"worda":"",
"wordb":"NO. Match",
"wordc":"TotalInvestment",
"wordd":"Investment",
"worde":"Payout",
"wordf":"Payout Ratio"},
{"worda":"",
"wordb":"",
"wordc":"人民币 RMB",
"wordd":"人民币 RMB",
"worde":"人民币 RMB",
"wordf":"%"}];

app.controller("fincectrl",function($scope,$http){
	this.dobox = dobox;
		
	var woDate = new Date();
	this.date = woDate.toLocaleDateString(); 
		
	var month = woDate.getMonth()+1;
	this.month = (month<10 ? "0"+month:month);
		
	var weekofyear =  (((new Date())-(new Date("2017-01-01")))/(24*60*60*7*1000)|0) +1;
	this.week = weekofyear;
		
	var _get = function(year,month,day){
		$http.get(urlip +'account/dailyFinancialStatements/queryAll?year='+year+'&month='+month+'&day='+day)
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
	console.log(year,month,day,"0");
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
		console.log(year,month,day,"4");
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
			window.open(urlip +'account/dailyFinancialStatements/summaryExcel?year='+year+"&month="+month+"&day="+day);
		} 
	}
});










