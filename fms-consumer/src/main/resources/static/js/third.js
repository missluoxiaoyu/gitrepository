var app = angular.module('third',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var thibox = [
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
	"urlb":urlip+"account/dailyFinancialStatements/list",
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
	"texa":"Third",
	"active":true
}];

app.controller("thirctrl",function(){
		this.thibox = thibox;
});
//查询渠道
function ajax(){
	var counter = '';
	$.ajax({
		async:false,
		dataType:"json",
		url:urlip +"account/orderManage/queryAllAgent",
//		url:"./qudao.txt",
		type:"get",
		success:function(data){
			//alert(data);
			counter = data.agentList;
		},
		error:function(data){
			alert("error")
		},
	});
	return counter;
}

app.controller("thirdctrl",function($scope,$http,$location){
	$scope.tbox = ajax();
	var _get = function(tid,startime,endtime,page,select){
//		alert(select);
//select=110;
			var url ="";
			if(tid !="" && tid !=null){
				url = urlip+"account/orderTicket/query?tkId="+tid+"&agentid="+select;
//				
			}else{
				url = urlip+"account/orderTicket/queryAll?agentid="+select+"&startDate="+startime+"&endDate="+endtime+"&page="+page;	
//				
			}
			console.log(url)
			$http.get(url)

		.success(function(data){

			if(data.resultlist !="" || data.ticket !=""){
				if(tid !="" && tid !=null){
		  			if(select =="110"){
						layer.msg(data.ticketResult ,{time:920});
					}else{
						$scope.ticketResult = data.ticketResult;
					}
				}else{
					if(select =="110"||data.resultList.length<100){
						if(select =="110"){
							layer.msg(data.resultList ,{time:920});
						}else{
							layer.msg("已是最后一页" ,{time:920});
							$scope.bbox = data.resultList;
						}
					}else{
						$scope.bbox = data.resultList;
					}
				}
//				$scope.bbox = data.resultList;
//				$scope.ticketResult = data.ticketResult;
			}else{
				alert("加载失败")
			};
		}).error(function(data){
			alert("error")
		})
	}
	//alert(ajax());
	var time =  new Date();
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();

	time.setTime(time.getTime()-24*60*60*1000);
	var s1 = time.getFullYear()+"-" + (time.getMonth()+1) + "-" + time.getDate();
	var star =$("#startime").val(s1);
	var startime=star.val();
	var end = $("#endtime").val(year+"-"+month+"-"+day);
	//alert(star);
	var endtime =end.val();
	var tid = $("#tid").val();
	var page = $("#page").val();
	
	var select = $scope.tbox[0].agentNum;
	//初始化第一页 
//	alert(select);
//	alert(tid);
	_get(tid,startime,endtime,page,select); 
	$scope.cha = function(){
		var tid = $("#tid").val();
		var startime = $("#startime").val();
		var endtime =$("#endtime").val();
		var page = $("#page").val();
		var select = $('#testSelect option:selected').val();
		
		if(tid =="" && startime=="" && endtime==""){
			layer.msg("请选择时间或者订单",{time:920});
		}else{
			if(tid !="" && tid !=null){
			
				_get(tid,startime,endtime,page,select);
			}else{
				if(startime =="" || endtime=="" ){
					layer.msg("请选择要查询的时间",{time:920});
				}else{
					_get(tid,startime,endtime,page,select)
				}
			}
		}
	}
	
	
	$scope.export1 =function(){
		var tid = $("#tid").val();
		var startime = $("#startime").val();
		var endtime =$("#endtime").val();
		var page = $("#page").val();
		var select = $('#testSelect option:selected').val();
		
		if(startime=="" || endtime==""){
			layer.msg("请先选择要导出的日期");
		}else{
			window.open(urlip +"account/orderTicket//orderExcel?startDate="+startime +"&endDate="+endtime+"&agentid="+select+"&page="+page);
		} 
	}
	
	
	
	
	
});






