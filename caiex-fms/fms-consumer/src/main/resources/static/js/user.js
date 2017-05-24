var app = angular.module('user',[]);

//var url = "http://192.168.1.210:8080/";

var usebox = [
{
	"urlb":"error.html",
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
	"texa":"Summary"
},
{
	"urlb":"summary.html",
	"texa":"Daily Summary"
},
{
	"urlb":"user.html",
	"texa":"用户管理",
	"active":true
}];
app.controller("userorctrl",function(){
		this.usebox = usebox;
});
var abox = [
{"texb":"合计Total"},
{"texb":"1"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"},
{"texb":"340"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"},
{"texb":"490.28"},
{"texb":"-"},
{"texb":"-35.751%"},
{"texb":"315"},
{"texb":"0"}];
var bbox = [
{"ordera":"订单ID",
"orderb":"用户ID",
"orderc":"渠道",
"orderd":"彩种",
"ordere":"玩法",
"orderf":"订单类型",
"orderg":"金额",
"orderh":"最大中奖额",
"orderi":"加奖额",
"orderj":"交易时间",
"orderk":"交易金额",
"orderl":"交易状态",
"orderm":"订单状态",
"ordern":"中奖金额",
"ordero":"第三方状态"},
{"ordera":"RU32201",
"orderm":"120",
"orderb":"Swaggie",
"orderc":"竞彩足球",
"orderd":"HAD",
"ordere":"死球",
"orderf":"80",
"orderg":"1592.5",
"orderh":"-",
"orderi":"2017-04-17 16:25:44",
"orderj":"99.3",
"orderk":"交易成功",
"orderl":"未中奖",
"ordern":"0",
"ordero":"未回收"},
{"ordera":"RU32201",
"orderm":"120",
"orderb":"Swaggie",
"orderc":"竞彩足球",
"orderd":"HAD",
"ordere":"死球",
"orderf":"80",
"orderg":"1592.5",
"orderh":"-",
"orderi":"2017-04-17 16:25:44",
"orderj":"99.3",
"orderk":"交易成功",
"orderl":"未中奖",
"ordern":"0",
"ordero":"未回收"},
{"ordera":"RU32201",
"orderm":"120",
"orderb":"Swaggie",
"orderc":"竞彩足球",
"orderd":"HAD",
"ordere":"死球",
"orderf":"80",
"orderg":"1592.5",
"orderh":"-",
"orderi":"2017-04-17 16:25:44",
"orderj":"99.3",
"orderk":"交易成功",
"orderl":"未中奖",
"ordern":"0",
"ordero":"未回收"}];

app.controller("userctrl",function($scope,$http,$location){
	this.abox = abox;
	this.bbox = bbox;
	
//点击未回收
	$scope.clicking=function(num,state){		
		if(num=="0" && state=="0"){
		lay("收票");
		}else if(num=="2" && state=="0"){
			lay("拒票");
		}else if(num=="1" && state=="0" || num=="6" && state=="0"){
			lay("取消票");
		}else if(num=="5" && state=="0"){
			lay("回收票")
		}
	}
	//公共弹窗
	function lay(piao,agService){
		layer.msg(piao, {
			shade: [0.8, '#fff'],	
		  	time: 0, //不自动关闭
		  	btn: ['确定', '取消'],
		  	yes: function(index){
//		  		var per = agService.query();
//		  		console.log(per);
				layer.msg("操作成功");
				
		  	},btn2: function(index, layero){
			  alert("取消");
			}
		});
	}

});








