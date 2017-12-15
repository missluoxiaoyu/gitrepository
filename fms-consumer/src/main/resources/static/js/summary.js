var app = angular.module('summary',['angular-loading-bar']);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";


app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {

    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:20%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
   	cfpLoadingBarProvider.latencyThreshold = 20;
}]);

var mbox = [
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
	"texa":"Summary"
},
{
	"urlb":urlip+"account/dailyCollectStatements/list",
	"texa":"Daily Summary",
	"active":true
},
{
	"urlb":urlip+"account/orderManage/list",
	"texa":"订单管理"
},{
	"urlb":urlip+"account/orderTicket/list",
	"texa":"Third"
}];

app.controller("summactrl",function($scope,$http){
		this.mbox = mbox;

});

app.controller("sumaryctrl",function($scope,$http){
		
		var _get = function(year,month){
		$http.get(urlip +'account/dailyCollectStatements/queryAll?year='+year+'&month='+month)

		.success(function(data){			
			console.log(data);		
			if(data){
				var obj=Object.keys(data).length-1
				$scope.monthTotal = data.monthTotal;
				var html ='';
				tmpnewchar = ""	
       			$("tbody").remove(".loop");
				for (var i = 1; i <=obj; i++) {	
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
				html +='<tr class="gree1" >'+
                                    '<td>第'+tmpnewchar+'周Week'+i+'</td>'+
                                    '<td >场次数量NO.Match</td>'+
                                    '<td >原始投注额TotalInvestment</td>'+
                                    '<td >交易额Investment</td>'+
                                    '<td >caiex加奖额</td>'+
                                    '<td >中奖金额Payout</td>'+
                                    '<td >盈利率Payout Ratio</td>'+
                                    '<td >场次数量场次数量NO.Match</td>'+
                                    '<td >原始投注额TotalInvestment</td>'+
                                    '<td >交易额Investment</td>'+
                                    '<td >caiex加奖额</td>'+
                                    '<td >中奖金额Payout</td>'+
                                    '<td >盈利率Payout Ratio</td>'+
                                    '<td >场次数量场次数量NO.Match</td>'+
                                    '<td >原始投注额TotalInvestment</td>'+
                                    '<td >交易额Investment</td>'+
                                    '<td >caiex加奖额</td>'+
                                    '<td >中奖金额Payout</td>'+
                                    '<td >盈利率Payout Ratio</td>'+
                                '</tr>';
                /* html +='<tr>'+
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
                                '</tr>';*/
                                
                              for (var j = 0; j <data[i].weekList.length; j++) {
                                	html +='<tr>'+
                                    '<td class="gree2" >'+data[i].weekList[j].week+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalNum+'</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalInvestment+'￥</td>'+
                                    '<td class="gree">'+data[i].weekList[j].invest+'￥</td>'+
                                    '<td class="gree">'+data[i].weekList[j].cxBonus+'￥</td>'+
                                    '<td class="gree">'+data[i].weekList[j].totalPrice+'￥</td>'+
                                    '<td class="gree">'+data[i].weekList[j].payoutRate+'%</td>'+
                                    '<td class="red">'+data[i].weekList[j].fbNum+'</td>'+
                                    '<td class="red">'+data[i].weekList[j].totalInvestmentFb+'￥</td>'+
                                    '<td class="red">'+data[i].weekList[j].investFb+'￥</td>'+
                                    '<td class="red">'+data[i].weekList[j].cxBonusFb+'￥</td>'+
                                    '<td class="red">'+data[i].weekList[j].totalPriceFb+'￥</td>'+
                                    '<td class="red">'+data[i].weekList[j].payoutRateFb+'%</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].bkNum+'</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].totalInvestmentBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].investBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].cxBonusBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].totalPriceBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].weekList[j].payoutRateBk+'%</td>'+
                                '</tr>';  
                                }
                                  html +='<tr>'+
                                    '<td class="gree2" >'+data[i].Weektotal.week+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalNum+'</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalInvestment+'￥</td>'+
                                    '<td class="gree">'+data[i].Weektotal.invest+'￥</td>'+
                                    '<td class="gree">'+data[i].Weektotal.cxBonus+'￥</td>'+
                                    '<td class="gree">'+data[i].Weektotal.totalPrice+'￥</td>'+
                                    '<td class="gree">'+data[i].Weektotal.payoutRate+'%</td>'+
                                    '<td class="red">'+data[i].Weektotal.fbNum+'</td>'+
                                    '<td class="red">'+data[i].Weektotal.totalInvestmentFb+'￥</td>'+
                                    '<td class="red">'+data[i].Weektotal.investFb+'￥</td>'+
                                    '<td class="red">'+data[i].Weektotal.cxBonusFb+'￥</td>'+
                                    '<td class="red">'+data[i].Weektotal.totalPriceFb+'￥</td>'+
                                    '<td class="red">'+data[i].Weektotal.payoutRateFb+'%</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.bkNum+'</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.totalInvestmentBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.investBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.cxBonusBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.totalPriceBk+'￥</td>'+
                                    '<td class="yellow">'+data[i].Weektotal.payoutRateBk+'%</td>'+
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










