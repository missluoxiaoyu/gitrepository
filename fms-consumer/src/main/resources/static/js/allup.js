var app = angular.module('Allup',['angular-loading-bar']);
var urlip = "http://"+document.location.host+"/";
//var urlip = "http://192.168.1.5:8083/";

app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {

    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
   	cfpLoadingBarProvider.latencyThreshold = 20;
}]);




var upbox = [
{
	"urlb":urlip+"account/errorTicket/list",
	"texa":"错误票信息"
	
},
{
	"urlb":urlip+"account/dailyAllup/list",
	"texa":"Daily Allup",
	
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


app.controller("upctrl",function(){
		this.upbox = upbox;
});

app.controller("alupctrl",function($scope,$http){
	var _get = function(year,month,day){  

        $http.get(urlip +'account/dailyAllup/queryAll?year='+year+"&month="+month+"&day="+day)
        .success(function(data){
        	if(data){


        		$scope.total = data.total;
//        		$scope.hbox = data.listHAD;  //原固定数据
        		
        		hbox =data.HAD;
        		$scope.hbox1 = hbox[7];
        		$scope.hbox1.local_m = "HAD";
        		$scope.hbox = hbox;
        		var str = hbox.splice(7,1);
//        		hbox.unshift(str[0]);
        		abox =data.HHAD;
        		$scope.abox1 = abox[7];
        		$scope.abox1.local_m = "HHAD";
        		$scope.abox = abox;
        		var str = abox.splice(7,1);
//        		abox.unshift(str[0]);
        		bbox =data.HAFU;
        		$scope.bbox1 = bbox[7];
        		$scope.bbox1.local_m = "HAFU";
        		$scope.bbox = bbox;
        		var str = bbox.splice(7,1);
//        		bbox.unshift(str[0]);
        		tbox =data.TTG;
        		$scope.tbox1 = tbox[7];
        		$scope.tbox1.local_m = "TTG";
        		$scope.tbox = tbox;
        		var str = tbox.splice(7,1);
        		
        		fbox =data.FCA;
        		$scope.fbox1 = fbox[7];
        		$scope.fbox1.local_m = "FCA";
        		$scope.fbox = fbox;
        		var str = fbox.splice(7,1);
        		
        		
        		
//        		tbox.unshift(str[0]);
        		cbox =data.CRS;
        		$scope.cbox1 = cbox[7];
        		$scope.cbox1.local_m = "CRS";
        		$scope.cbox = cbox;
        		var str = cbox.splice(7,1);
//        		cbox.unshift(str[0]);
        	
       	
     
//		fbox.unshift(str[0]);
		  }else{  
                alert("加载失败");  
            }  
       })
    }
	
	
	
	
	
	
//	var time =  new Date();
//	var year = time.getFullYear();
//	var month = time.getMonth()+1;
//	var day = time.getDate();
//	mydate = year+"-"+month+"-"+day;
//	$("#d12").val(year+"-"+month+"-"+day);
//	//初始化第一页 
//	_get(mydate); 
//	$scope.data = function(){
//		var data = $("#d12").val();
//		_get(data); 	
//	}
	
//	},function(data){
//	  	console.log(123)
//	});

var time =  new Date();
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	
	var sel=document.getElementById("state");
	var pid = sel.options[sel.selectedIndex].value;
	
	$("#d12").val(year+"-"+month+"-"+day);
	//初始化第一页 
	_get(year,month,day); 
	$scope.data = function(){
		var	year = "";
		var	month = "";
		var	day = "";
		
		var sel=document.getElementById("state");
		var pid = sel.options[sel.selectedIndex].value;
		
		
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
		
		
		//_get(year,month,day); 
		
		
		if(pid==2){
			window.location.href = urlip +'account/basketballDailyAllup/list';
		}else{
			_get(year,month,day); 
			
		}
		
		
	}
//		_get();     //初始化
	
//导出功能
	$scope.export=function(){
		var	year = "";
		var	month = "";
		var	day = "";
		
		
		/*var sel=document.getElementById("state");
		var pid = sel.options[sel.selectedIndex].value;*/
		
		
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
			
			window.open(urlip +'account/dailyAllup//dailyAllupExcel?year='+year+"&month="+month+"&day="+day);
		} 
	}
});










