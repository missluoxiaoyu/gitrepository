var app = angular.module('third',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";
var thibox = [
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
},{
	"urlb":"third.html",
	"texa":"Third",
	"active":true
}];

app.controller("thirctrl",function(){
		this.thibox = thibox;
});
function ajax(){
	var counter = '';
	$.ajax({
		async:false,
		dataType:"json",
		url:urlip +"account/orderTicket/queryAllAgent",
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
			var url ="";
			if(tid !="" && tid !=null){
				url = urlip+"account/orderTicket/query?tkId="+tid+"&agentid="+select;
//				url = urlip+"account/orderTicket/query?tkId=79841949&agentid=111";
//				url = "./dan.txt";
			}else{
				url = urlip+"account/orderTicket/queryAll?agentid="+select+"&startDate="+startime+"&endDate="+endtime+"&page="+page;	
//				url = urlip+"account/orderTicket/queryAll?agentid=111&startDate=2017-6-7&endDate=2017-6-8&page=1";	
//				url = "./duo.txt";
			}
			console.log(url)
			$http.get(url)
//		$http.get("./duo.txt")
		.success(function(data){
//			console.log(data);
			if(data){
		  		if(select =="110"){
					layer.msg(data.resultList ,{icon:'5'});
				}else{
					$scope.bbox = data.resultList;
				}
				
				if(data.resultList.length<100){
					layer.msg("已是最后一页" ,{icon:'5'});
				}else{
					$scope.bbox = data.resultList;
				}
				$scope.ticketResult = data.ticketResult;
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
		
		if(select =="110"){
			layer.msg(data.ticketResult ,{icon:'5'});
		}else{
			_get(tid,startime,endtime,page,select)
		}
						
		if(tid =="" && startime=="" && endtime==""){
			layer.msg("请选择时间或者订单",{time:920});
		}else{
			if(tid !="" && tid !=null){
				//alert(tid);
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
});






