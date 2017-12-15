  angular.module('channel11',['angular-loading-bar']);

  angular.module('ui.bootstrap.demo', ['ui.bootstrap']);

  var app =  angular.module('channel', [ 'channel11','ui.bootstrap.demo']);
  
  
  
var urlip = "http://"+document.location.host+"/";

var ca = document.getElementById("luu");
var cb = document.getElementById("hongg");
	app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	
	    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
	    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="http://fms.caiex.com:8083/account/img/loading.gif"/></div>';
	   	cfpLoadingBarProvider.latencyThreshold = 20;
	}]);

var chbox = [
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
	"active":true
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
}];


var agentidd=0;
app.controller("chanctrl",function(){
		this.chbox = chbox;
});
app.controller("chanelctrl",function($scope,$http,cfpLoadingBar,$uibModal,){
	var _get = function(year,month,day){
		$http.get(urlip +'account//channelStatistics/queryAll?year='+year+'&month='+month+'&day='+day)
		.success(function(data){
			if(data){
				$scope.lebox = data.agentInfoModels;
			
				for(var i=0; i<$scope.lebox.length; i++){	
				
					if($scope.lebox[i].agentSell =="-1"){
						$scope.lebox[i].agentSellMess = "停售";
						
					}else if($scope.lebox[i].agentId == $scope.lebox[i].agentSell){
						$scope.lebox[i].agentSellMess = "开售";
					}
					
					
					if($scope.lebox[i].recyclePrice ==null){
						$scope.lebox[i].recyclePriceName= "-"; 
					}else{
						$scope.lebox[i].recyclePriceName = $scope.lebox[i].recyclePrice ;
					}
					
					
				}
				
				$scope.model = data.model
				
			}else{
				alert("加载失败")
			};
		})
	}
	var time =  new Date();
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	
	
	$("#d12").val(year+"-"+month+"-"+day);
	//初始化第一页 
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
		
		
		_get(year,month,day);
		
	}
	

	$scope.saveOrdrawDetail=function(agentId){
		var detail = function(){  
	        $http.get(urlip +'account/channelStatistics/queryPreStoreDetail?agentCode='+agentId)
	        .success(function(data){
//	        	console.log(data);
				var agentInfoDetails = data.agentInfoDetails;
	        	console.log(agentInfoDetails);
				var html = " <p class='line'> "
				for(var i = 0; i<agentInfoDetails.length; i++){
					html +="<span class='liner'>"+agentInfoDetails[i].operateTime+"</span><span>"+agentInfoDetails[i].preStoreDetail+"</span></br>" ;
				}
					html +="</p >";
				layer.confirm(html, {
					btn: ['取消', '确定']
				}, function() {
					layer.msg('取消了', {
						icon: 5
					});
				});
			})
		}
		//初始化
		detail();
	}
	
	
	
	


	
	
	
	
	function RGBToHex(rgb){ 
	    var regexp = /[0-9]{0,3}/g;  
	    var re = rgb.match(regexp);//利用正则表达式去掉多余的部分，将rgb中的数字提取
	    var hexColor = "#"; 
	    var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'];  
	    for (var i = 0; i < re.length; i++) {
	        var r = null, c = re[i], l = c; 
	        var hexAr = [];
	        while (c > 16){  
	            r = c % 16;  
	            c = (c / 16) >> 0; 
	            hexAr.push(hex[r]);  
	        } hexAr.push(hex[c]);
	        if(l < 16&&l != ""){        
	            hexAr.push(0)
	        }
	        hexColor += hexAr.reverse().join(''); 
	    }  
	   //alert(hexColor)  
	    return hexColor;  
	}
	
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
			window.open(urlip +'account/channelStatistics/channelStatisticsExcel?year='+year+"&month="+month+"&day="+day);

		} 
}
	
	
	
	    $scope.items = ['item1', 'item2', 'item3'];
	    
	  
	    $scope.open = function (agentId) {
	    	agentidd = agentId;
	        var modalInstance = $uibModal.open({
	            templateUrl: 'myModalContent.html',
	            controller: 'ModalInstanceCtrl', /*依赖控制器注入*/
	            backdrop: "static",
	             resolve: { /*传递到模态窗口中的对象*/
	                items1: function () {
	                
	                	return $scope.items;  
	                }
	      
	            } 
	        });
	        //模态实例---ModalDemoCtrl中的$scope
	        //接收点击ok参数
	        modalInstance.result.then(function (selectedItem) {
	           // $scope.selected = selectedItem;
	       
	        }, function () {
	            $log.info('Modal dismissed at: ' + new Date());
	        });
	    };

	    $scope.toggleAnimation = function () {
	        $scope.animationsEnabled = !$scope.animationsEnabled;
	    };
	
	
});


//$uibModalInstance是模态窗口的实例  
app.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items1) {
  /*  $scope.items = items1;
    $scope.selected = {
        item: $scope.items[0]
    };*/
    $scope.ok = function () {
    	//关闭模态后，将ModalInstanceCtrl控制器
    	var str = $("#recyS").val();
    	var end = $("#recyD").val();
    	if(str==null || str =="" || end==null || end ==""){
    		alert("请选择时间");
    	}else{
    		window.open(urlip +'account/channelStatistics/detailExcel?startDate='+str+"&endDate="+end+"&agentId="+agentidd);
    		$uibModalInstance.dismiss('cancel');
    	}
    	
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});




