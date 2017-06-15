var app = angular.module('summary',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var mbox = [
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
	"texa":"Daily Summary",
	"active":true
},
{
	"urlb":"user.html",
	"texa":"用户管理"
},{
	"urlb":"third.html",
	"texa":"Third"
}];
app.controller("summactrl",function(){
		this.mbox = mbox;
});

app.controller("sumaryctrl",function($scope,$http){
		var _get = function(year,month){
		$http.get(urlip +'account/dailyCollectStatements/queryAll?year='+year+'&month='+month)
//		$http.get("./summary.txt")
		.success(function(data){			
			console.log(data);		
			if(data){
				var obj=Object.keys(data).length-1
				$scope.monthTotal = data.monthTotal;
				var html ='';
				tmpnewchar = ""	
       			$("tbody").remove(".loop");
				for (var i = 1; i <=obj; i++) {
//					console.log(data[i].Weektotal.week);	
					switch (i) {
				        case 0: tmpnewchar = "零"; break;
				        case 1: tmpnewchar = "一"; break;
				        case 2: tmpnewchar = "二"; break;
				        case 3: tmpnewchar = "三"; break;
				        case 4: tmpnewchar = "四"; break;
				        case 5: tmpnewchar = "五"; break;
				        case 6: tmpnewchar = "六"; break;
				        case 7: tmpnewchar = "七"; break;
				        case 8: tmpnewchar = "八"; break;
				        case 9: tmpnewchar = "九"; break;
				       }	
				html +='<tbody id="loop" class="loop">';
				html +='<tr>'+
                                    '<td>第'+tmpnewchar+'周Week'+i+'</td>'+
                                    '<td class="gree">场次数量</td>'+
                                    '<td class="gree">原始投注额</td>'+
                                    '<td class="gree">交易额</td>'+
                                    '<td class="gree">中奖金额</td>'+
                                    '<td class="gree">盈利率</td>'+
                                    '<td class="red">场次数量</td>'+
                                    '<td class="red">原始投注额</td>'+
                                    '<td class="red">交易额</td>'+
                                    '<td class="red">中奖金额</td>'+
                                    '<td class="red">盈利率</td>'+
                                    '<td class="blue">场次数量</td>'+
                                    '<td class="blue">原始投注额</td>'+
                                    '<td class="blue">交易额</td>'+
                                    '<td class="blue">中奖金额</td>'+
                                    '<td class="blue">盈利率</td>'+
                                '</tr>';
                 html +='<tr>'+
                                    '<td></td>'+
                                    '<td class="gree">NO. Match</td>'+
                                    '<td class="gree">TotalInvestment</td>'+
                                    '<td class="gree">Investment</td>'+
                                    '<td class="gree">Payout</td>'+
                                    '<td class="gree">Payout Ratio</td>'+
                                    '<td class="red">NO. Match</td>'+
                                    '<td class="red">TotalInvestment</td>'+
                                    '<td class="red">Investment</td>'+
                                    '<td class="red">Payout</td>'+
                                    '<td class="red">Payout Ratio</td>'+
                                    '<td class="blue">NO. Match</td>'+
                                    '<td class="blue">TotalInvestment</td>'+
                                    '<td class="blue">Investment</td>'+
                                    '<td class="blue">Payout</td>'+
                                    '<td class="blue">Payout Ratio</td>'+
                                '</tr>' ;
                 html +='<tr>'+
                                    '<td></td>'+
                                    '<td class="gree"></td>'+
                                    '<td class="gree">人民币 RMB</td>'+
                                    '<td class="gree">人民币 RMB</td>'+
                                    '<td class="gree">人民币 RMB</td>'+
                                    '<td class="gree">%</td>'+
                                    '<td class="red"></td>'+
                                    '<td class="red">人民币 RMB</td>'+
                                    '<td class="red">人民币 RMB</td>'+
                                    '<td class="red">人民币 RMB</td>'+
                                    '<td class="red">%</td>'+
                                    '<td class="blue"></td>'+
                                    '<td class="blue">人民币 RMB</td>'+
                                    '<td class="blue">人民币 RMB</td>'+
                                    '<td class="blue">人民币 RMB</td>'+
                                    '<td class="blue">%</td>'+
                                '</tr>';
                                
                              for (var j = 0; j <data[i].weekList.length; j++) {
                              		
                                	html +='<tr>'+
                                    '<td>'+data[i].weekList[j].week+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalNum+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalInvestment+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].invest+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalPrice+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].payoutRate+'%</td>'+
                                    '<td class="red">'+data[i].weekList[j].fbNum+'</td>'+
                                    '<td class="red">'+data[i].weekList[j].totalInvestmentFb+'</td>'+
                                    '<td class="red">'+data[i].weekList[j].investFb+'</td>'+
                                    '<td class="red">'+data[i].weekList[j].totalPriceFb+'</td>'+
                                    '<td class="red">'+data[i].weekList[j].payoutRateFb+'%</td>'+
                                    '<td class="blue">'+data[i].weekList[j].bkNum+'</td>'+
                                    '<td class="blue">'+data[i].weekList[j].totalInvestmentBk+'</td>'+
                                    '<td class="blue">'+data[i].weekList[j].investBk+'</td>'+
                                    '<td class="blue">'+data[i].weekList[j].totalPriceBk+'</td>'+
                                    '<td class="blue">'+data[i].weekList[j].payoutRateBk+'%</td>'+
                                '</tr>';  
                                }
                                  html +='<tr>'+
                                    '<td>'+data[i].Weektotal.week+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalNum+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalInvestment+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.invest+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalPrice+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.payoutRate+'%</td>'+
                                    '<td class="red">'+data[i].Weektotal.fbNum+'</td>'+
                                    '<td class="red">'+data[i].Weektotal.totalInvestmentFb+'</td>'+
                                    '<td class="red">'+data[i].Weektotal.investFb+'</td>'+
                                    '<td class="red">'+data[i].Weektotal.totalPriceFb+'</td>'+
                                    '<td class="red">'+data[i].Weektotal.payoutRateFb+'%</td>'+
                                    '<td class="blue">'+data[i].Weektotal.bkNum+'</td>'+
                                    '<td class="blue">'+data[i].Weektotal.totalInvestmentBk+'</td>'+
                                    '<td class="blue">'+data[i].Weektotal.investBk+'</td>'+
                                    '<td class="blue">'+data[i].Weektotal.totalPriceBk+'</td>'+
                                    '<td class="blue">'+data[i].Weektotal.payoutRateBk+'%</td>'+
                                '</tr>';  
 						html +='</tbody>';  
 						
					}
					//$("#ta").append(html);
					document.getElementById('ta').insertAdjacentHTML('afterend', html);   

//				$scope.mybox = data.1.weekList;
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
	//var day = time.getDate();
	$("#d12").val(year+"-"+month);
	//初始化第一页 
	_get(year,month); 
	$scope.data = function(){
		//$('#loop').remove();	
		var	year = "";
		var	month = "";
		var data = $("#d12").val();
		var sort = data.split("-");
		if(sort.length ==2){
			year=sort[0];
			month=sort[1];
			_get(year,month); 
		}
	}
		//_get();     //初始化
	
//导出功能
	$scope.export=function(){
		var	year = "";
		var	month = "";
		var time = $("#d12").val();
		if(time =="" || time ==null){
			layer.msg("请先选择要导出的日期");
		}else{
			var sort = time.split("-");
			if(sort.length ==2){
				year=sort[0];
				month=sort[1];
			}
			window.open(urlip +'account/dailyCollectStatements/dailySummaryExcel?year='+year+"&month="+month);
		} 
	}
});










