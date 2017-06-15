var app = angular.module('user',[]);

//var url = "http://192.168.1.210:8080/";

var usebox = [
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
},{
	"urlb":"third.html",
	"texa":"Third"
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
	var _get = function(tid,startime,endtime,page,select){
//		alert(select);
//			console.log(url)
			$http.get("./")
//		$http.get("./duo.txt")
		.success(function(data){
//			console.log(data);
			if(data){
//				$scope.bbox = data.resultList;
//				$scope.ticketResult = data.ticketResult;
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

	time.setTime(time.getTime()-24*60*60*1000);
	var s1 = time.getFullYear()+"-" + (time.getMonth()+1) + "-" + time.getDate();
	var star =$("#startime").val(s1);
	var startime=star.val();
	var end = $("#endtime").val(year+"-"+month+"-"+day);
	var endtime =end.val();
	//初始化第一页 
	_get(startime,endtime); 
	$scope.cha = function(){
		var startime = $("#startime").val();
		var endtime =$("#endtime").val();
		
		if(startime=="" && endtime==""){
			layer.msg("请选择时间",{time:920});
		}else{
			if(startime =="" || endtime=="" ){
				layer.msg("请选择要查询的时间",{time:920});
			}else{
				_get(startime,endtime); 
			}
		}
	}	
	
//点击未回收
	$scope.clicking=function(){		
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
	function lay(){
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
	$scope.details=function(){	
		var html = "<table class='pad'><tbody><tr><th>序号</th><td>1 (Alive)</td></tr>";
		html += "<tr><th>玩法</th><td>FCA</td></tr>";
		html += "<tr><th style='height:100px;'>投注内容</th><td>HAD 1001 * [哈德斯 vs 雷丁] X@2.979 (3.5) [0-0] / HHAD 1002 * [索尔纳 vs 马尔默] X[1]@3.799 (3.5) [0-0]</td></tr>";
		html += "<tr><th>过关方式</th><td>2</td></tr>";
		html += "<tr><th>倍数</th><td>20</td></tr>";
		html += "<tr><th>中奖金额</th><td>490￥</td></tr><tr>";
		html += "<tr><tr><th>交易金额</th><td>43.27￥</td></tr></tbody></table>";
		layer.confirm(html, {
			btn: ['取消', '确定'],
		});
	}
});








