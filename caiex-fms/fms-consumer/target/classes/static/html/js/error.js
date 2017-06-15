var app = angular.module('error',[]);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";

var urle = "account//errorTicket/refuseTicket";   //拒票
var urlf = "account//errorTicket/acceptTicket";   //收票
var urlc = "account//errorTicket/cancelTicket";   //取消票
var urld = "account//errorTicket/recycleTicket";  //回收票

var errbox = [
{
	"urlb":"errorTicket.html",
	"texa":"错误票信息",
	"active":true
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
	"texa":"用户管理"
},{
	"urlb":"third.html",
	"texa":"Third"
}];
app.controller("rorctrl",function(){
		this.errbox = errbox;
});
var erorbox = [
{"texb":"ID"},
{"texb":"tkId"},
{"texb":"tradeState"},
{"texb":"agentId"},
{"texb":"tradeTime"},
{"texb":"tradePrice"},
{"texb":"recyclePrice"},
{"texb":"winMoney"},
{"texb":"state"},
{"texb":"queryTime"}];
app.factory('agService', ['$http', '$q', '$location', function($http, $q, $location) {
	return {
		query: function(url,id) {
				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
				$http({
					method: 'GET',
					url: urlip +url+'?id='+id
				}).success(function(data) {
					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
//				alert(id);
				}).error(function(data) {
					deferred.reject(data); // 声明执行失败，即服务器返回错误  
//				console.log(id);
				});
				return deferred.promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API  
			} // end query  
	};
}]);
app.controller("erorctrl",function($scope,$http,$location,agService){
	this.erorbox = erorbox;
	//配置  
    $scope.count = 0;  
    $scope.p_pernum = 10;  
    //变量  
    $scope.p_current = 1;  
    $scope.p_all_page =0;  
    $scope.pages = [];  
	var _get = function(page,size,startime,endtime,callback){  
		console.log(page,size,startime,endtime,"112")
		
        $http.get(urlip +"account/errorTicket/getErrorTickets?page="+page +"&size="+size+"&startDate="+startime+"&endDate="+endtime)
//		$http.get(urlip +"account/errorTicket/getErrorTickets?size=10&startDate=2017-5-22&endDate=2017-5-24&page=1")
        .success(function(res){
            if(res){
            	var count = ajax(startime,endtime);
				for(var i = 0; i < res.ticketResult.length; i++){
					switch(res.ticketResult[i].tradeState){
//					case 0:
//					res.ticketResult[i].ts = "数据库无此票第三方有此票";
//					res.ticketResult[i].tst = "收票";
//					break;
					case 1:
					res.ticketResult[i].ts = "数据库有此票第三方无此票";
//					res.ticketResult[i].tst = "取消票";
					break;
					case 2:
					res.ticketResult[i].ts = "对方还在交易中";
//					res.ticketResult[i].tst = "拒票";
					break;
					case 3:
					res.ticketResult[i].ts = "对方显示转让完成已中奖，我们显示未中奖或alive";
					break;
					case 5:
					res.ticketResult[i].ts = "对方显示中奖已回收，我们显示未回收";
//					res.ticketResult[i].tst = "回收票";
					break;
					case 6:
					res.ticketResult[i].ts = "对方方案未交易，我们有票";
//					res.ticketResult[i].tst = "取消票";
					break;
					}
					if(res.ticketResult[i].state==0){
						res.ticketResult[i].state = "未处理";
					}else{
						res.ticketResult[i].state = "已处理";
					}
				} 

                $scope.count=count;  
                $scope.ticketResult = res.ticketResult;
                //this.ticketResult=res.ticketResult;  
                $scope.p_current = page;  
                $scope.p_all_page =Math.ceil($scope.count/$scope.p_pernum);  
                reloadPno();  
                callback();  
            }else{  
                alert("加载失败");  
            }  
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
//	_get(startime,endtime); 
	$scope.cha = function(){
		var startime = $("#startime").val();
		var endtime =$("#endtime").val();
		
		if(startime=="" && endtime==""){
			layer.msg("请选择时间",{time:920});
		}else{
			if(startime =="" || endtime=="" ){
				layer.msg("请选择要查询的时间",{time:920});
			}else{
				_get($scope.p_current,$scope.p_pernum,startime,endtime,function(){  
        				//alert("123");  
   				}); 
			}
		}
	}
	//初始化第一页
	_get($scope.p_current,$scope.p_pernum,startime,endtime,function(){  
        //alert("我是第一次加载");  
   });  
    //单选按钮选中  
   /*  $scope.select= function(id){  
        alert(id);  
    }   */
    //首页  
    $scope.p_index = function(){  
        $scope.load_page(1);  
    }  
    //尾页  
    $scope.p_last = function(){  
        $scope.load_page($scope.p_all_page);  
    }  
    //加载某一页  
    $scope.load_page = function(page){  
        _get(page,$scope.p_pernum,function(){ });  
    };  
    //初始化页码  
    var reloadPno = function(){  
        $scope.pages=calculateIndexes($scope.p_current,$scope.p_all_page,8); 
	};   
	    var calculateIndexes = function (current, length, displayLength) {  
		var indexes = [];  
		var start = Math.round(current - displayLength / 2);  
		var end = Math.round(current + displayLength / 2);  
	    if (start <= 1) {  
	        start = 1;  
	        end = start + displayLength - 1;  
	        if (end >= length - 1) {  
	            end = length - 1;  
	        }  
	    }
	    if (end >= length - 1) {  
	        end = length;  
	        start = end - displayLength + 1;  
	        if (start <= 1) {  
	            start = 1;  
	        }  
	    }  
	    for (var i = start; i <= end; i++) {  
	        indexes.push(i);  
	    }  
	    return indexes;  
	  };  
	$scope.que=function(tradeState){		
		if(tradeState =="1"){
		}
		$scope.visible = !$scope.visible;		
	}
//点击未处理
	$scope.clicking=function(id,tradeState,state,$index){
//		if(tradeState=="0" && state=="未处理"){
//		lay("收票",urlf,id,$index);
//		}else 
		if(tradeState=="2" && state=="未处理"){
			lay("拒票",urle,id,$index);
		}else if(tradeState=="1" && state=="未处理" || tradeState=="6" && state=="未处理"){
			lay("取消票",urlc,id,$index);
		}else if(tradeState=="5" && state=="未处理"){
			lay("回收票",urld,id,$index)
		}
	}
	//公共弹窗
	function lay(piao,url,id,ind){
//		alert(ind);
		layer.msg(piao, {
			shade: [0.8, '#fff'],	
		  	time: 0, //不自动关闭
		  	btn: ['确定', '取消'],
		  	yes: function(index){
		  		var per = agService.query(url,id);
		  		per.then(function(data){
		  			if(data.status =="2001" || data.status=="2002"){   //拒票
		  				if(data.status =="2001"){
							layer.msg(data.message,{icon:'6'});
							$scope.ticketResult[ind].state ="已处理";
						}else{
							layer.msg(data.message,{icon:'5'});
						}
		  			}else if(data.status =="1001" || data.status=="1002"){   //收票
//		  				layer.msg(data.message);
						if(data.status =="1001"){
							layer.msg(data.message,{icon:'6'});
							$scope.ticketResult[ind].state ="已处理";
						}else{
							layer.msg(data.message,{icon:'5'});
						}
		  			}else if(data.status =="6331" || data.status=="6332"){   //取消票
//		  				console.log(data.message);
		  				if(data.status =="6332"){
							layer.msg(data.message,{icon:'6'});
							$scope.ticketResult[ind].state ="已处理";
						}else{
							layer.msg(data.message,{icon:'5'});
						}
		  			}
//		  			else if(data.status =="6229" || data.status=="6230"){   //回收票
//		  				if(data.status =="6230"){
//							layer.msg(data.message,{icon:'6'});
//							$scope.ticketResult[ind].state ="已处理";
//						}else{
//							layer.msg(data.message,{icon:'5'});
//						}
//		  			}
		  			else{
//		  				alert(data.status);
		  				alert(data.message);
		  			}
		  		},function(data){
		  			//返回error
//		  			console.log(123)
		  		});
//				layer.msg("操作成功");
		  	},btn2: function(index, layero){
//			  alert("取消");
			}
		});
	}
});
function ajax(startime,endtime){
	var counter = '';
	$.ajax({
		async:false,
		dataType:"json",
		url:urlip +"account//errorTicket/amount?startDate="+startime+"&endDate="+endtime,
//		url:urlip +"account//errorTicket/amount?startDate=2017-5-22&endDate=2017-5-24",
		type:"get",
		success:function(data){
			counter = data.amount;
//			console.log(startime,endtime)
		},
		error:function(data){
			alert(data);
		},
	})
	return counter;
	
//	var setCookie = function(name, value) {
//      var Days = 30;
//      var exp = new Date();
//      exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
//      document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
//          //$cookies[name] = value;
//      };
//	});
//	var getCookie = function(name) {
//  var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
//  if (arr = document.cookie.match(reg))
//      return unescape(arr[2]);
//  else
//      return null;
//  }
}







