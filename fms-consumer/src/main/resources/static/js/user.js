
    angular.module('user', ['angular-loading-bar']);

    angular.module('page', ['tm.pagination']);

    var app = angular.module('myApp', ['user', 'page']);

    var urlip = "http://"+document.location.host+"/";
 
    app.controller("usero",function($scope){
    	$scope.usebox = usebox;
    	
    }); 
    
    var usebox = [
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
                  	"texa":"Daily Summary"
                  },

                  {
                  	"urlb":urlip+"account/orderManage/list",
                  	"texa":"订单管理",
                  	"active":true
                  },
         
                  {
                  	"urlb":urlip+"account/orderTicket/list",
                  	"texa":"Third"
                  },
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
    
    
    
    
    
    
    var page ="";
    var size = "";
    var model ="";
    var totalSize = 0;
    
    /* 默认时间 */
    var time =  new Date();
	var year = time.getFullYear();
	var month = time.getMonth()+1;
	var day = time.getDate();
	var hour = time.getHours();
	var minutes = time.getMinutes();  
	var second = time.getSeconds(); 
	

	function GetDateStr(AddDayCount) { 
		var dd = new Date(); 
		dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
		var y = dd.getFullYear(); 
		var m = dd.getMonth()+1;//获取当前月份的日期 
		var d = dd.getDate(); 
		var hour = time.getHours();
		var minutes = time.getMinutes();  
		var second = time.getSeconds(); 
		return y+"-"+m+"-"+d; 
	} 
		
		if(hour>=14){
			var s1 = GetDateStr(0)+" 14:00:00";
			var end = GetDateStr(0)+" 23:59:59";
		}else{
			var s1 = GetDateStr(-1)+" 14:00:00";
			var end =GetDateStr(0)+" 23:59:59";
		}
		
		var star =$("#startime").val(s1);
		var startime=star.val();
		var end =$("#endtime").val(end);
		var endtime =end.val();
		
		
		app.controller('promptInfo', function($scope) {
			 $scope.myVar = true;
	        $scope.click = function() {
	       	 $scope.myVar = !$scope.myVar;
	        };
		});	
	
		
	/* app.controller("myController", function($scope, $timeout,$http){
		var url='http://localhost:8085/account/testOrder';
		$http.get(url, function(data) {
    		 alert("收到请求并返回");
   		}) 
		
	})	 */
		

	
    app.controller('testController', function($scope, $timeout,$http){
    	$scope.tbox = ajax();
    	
   		//应该在请求发回后再去给这个赋值
        $scope.paginationConf = {
            currentPage: 1,
            itemsPerPage: 15,
            pagesLength: 15,
            perPageOptions: [10, 20, 30, 40, 50],
        
            onChange: function(){
            	
            	size = $scope.paginationConf.itemsPerPage;
            	page = $scope.paginationConf.currentPage;
            	
            	$scope.queryAll();
            	/*  $http.get(url, function(data) {
            		 alert("收到请求并返回");
               		// $scope.paginationConf.totalItems = data.totalItems;
           		}) */ 
            	
            }
        };
      

     $scope.details = function (data){
    	var ticketInfo_id = data.ticketInfo_id;
    	var  ballType = data.ballType;
    	var tkId = data.tkId;
    	
    	$http({
    		url:urlip+'account/orderManage/queryDetail?ticketInfo_id='+ticketInfo_id+'&ballType='+ballType,
    		method:'GET'
    		}).success(function(data,header,config,status){
    		//响应成功
    			 //
				 var html= "<table class='pad'><thead>";
				 html+="<tr><th >订单ID</th><th  colspan='7'  style='text-align:left;'>"+tkId+"</th></tr>";
				 
				  html += "<tr><th>序号</th><th>玩法</th><th style='width:52%'>投注内容</th>";
	              html += "<th>过关方式</th><th>倍数</th><th>中奖金额</th><th >原交易金额</th><th >caiex加奖额</th></tr></thead>";
					for (var i = 0; i < data.detailList.length; i++) {
						var l_code= data.detailList[i].l_code;// level match code: 一场比赛的赛事编码 +,+ 降关情况
						var l_codes=l_code.split(",");
						var betContents= data.detailList[i].betContent.split("/");
						var canceled = data.detailList[i].canceled;
						var cancel = canceled.split(",");
						
						var contents=""; 
						for (var j = 0; j < l_codes.length; j++) {
							if(contents==""){
								if(l_codes[j]=="0"){
									  if(cancel[j] == "VOID" ){
									  	contents="<font style='color:BLUE'>"+betContents[j]+"</font>";
									  }else{
									  	contents="<font style='color:#fe3c3c'>"+betContents[j]+"</font>"; 
									  }
								 }else{
									  contents=betContents[j]; 
								}
							}else{
								if(l_codes[j]=="0"){
									 if(cancel[j] == "VOID" ){
									   	contents=contents+"/"+"<font style='color:BLUE'>"+betContents[j]+"</font>"; 
									}else{ 
										contents=contents+"/"+"<font style='color:#fe3c3c'>"+betContents[j]+"</font>";
									 } 
								}else{
									   contents=contents+"/"+betContents[j];
								}
							} 
						}
						html += "<tbody><tr><td>"+(i+1)+data.detailList[i].stateMessage+"</td>";
						html += "<td>"+data.detailList[i].product+"</td><td>"+contents+"</td><td>"+data.detailList[i].local_m+"</td><td>"+data.detailList[i].num+"</td>";
						html += "<td>"+data.detailList[i].total_price+"￥</td><td>"+data.detailList[i].price_allup+"￥</td><td>"+data.detailList[i].cxBonus+"￥</td></tr></tbody>";
					}
				html +='</table>';  
               layer.confirm(html, {
					area: ['80%', 'auto'],
                   btn: ['取消', '确定'],
               });
    			

    		}).error(function(data,header,config,status){
    		//处理响应失败
    		});
     }   
        
        
        
        
        
        
   		//点击查询
   		$scope.queryAll = function(){	
			 query();
			 $scope.total = model.total; 
			 totalSize = model.total.size;
		   	
			 $scope.paginationConf.totalItems =totalSize ;

			 $scope.bbox = model.modelList;
		
			
			 for (var i = 0; i < $scope.bbox.length; i++) {
					if($scope.bbox[i].trade_type=="0"){
						$scope.bbox[i].tradeName ="未交易";
					}else if($scope.bbox[i].trade_type=="1"){
						$scope.bbox[i].tradeName ="交易成功";
					}else if($scope.bbox[i].trade_type=="2"){
						$scope.bbox[i].tradeName ="等待";
					}else if($scope.bbox[i].trade_type=="3"){
						$scope.bbox[i].tradeName ="交易失败";
					}else if($scope.bbox[i].trade_type=="4"){
						$scope.bbox[i].tradeName ="取消申请";
					}else if($scope.bbox[i].trade_type=="5"){
						$scope.bbox[i].tradeName ="通过";
					}else if($scope.bbox[i].trade_type=="6"){
						$scope.bbox[i].tradeName ="拒绝";
					}else if($scope.bbox[i].trade_type=="7"){
						$scope.bbox[i].tradeName ="取消";
					}else if($scope.bbox[i].trade_type=="44"){
						$scope.bbox[i].tradeName ="取消申请";
					}else{
						$scope.bbox[i].tradeName ="-";
					}
						if($scope.bbox[i].trade_type=="4"){
							$scope.bbox[i].iscancel=true;
						}else{
							$scope.bbox[i].iscancel=false;
						}
					if($scope.bbox[i].state=="1"){
						$scope.bbox[i].stateName ="中奖";
					}else if($scope.bbox[i].state =="2"){
						$scope.bbox[i].stateName ="未中奖";
					}else if($scope.bbox[i].state =="3"){
						$scope.bbox[i].stateName ="Alive";
					}else{
						$scope.bbox[i].stateName ="-";
					}
					if ($scope.bbox[i].inplay=="0") {
						$scope.bbox[i].inplayName ="死球";
					}else{
						$scope.bbox[i].inplayName ="即场";
					}
					if($scope.bbox[i].ballType =="1"){
						$scope.bbox[i].ballTypeName ="竞彩足球";
					}else{
						$scope.bbox[i].ballTypeName ="篮球";
					}
					if($scope.bbox[i].addAwardAmount =="0"){
						$scope.bbox[i].addAwardAmount ="-";
					}else{
						$scope.bbox[i].addAwardAmount = $scope.bbox[i].addAwardAmount;
					}
					
					if($scope.bbox[i].rakeRate =="0" || $scope.bbox[i].rakeRate ==null ){
						$scope.bbox[i].rakeRate ="-";
					}else{
						$scope.bbox[i].rakeRate = $scope.bbox[i].rakeRate;
					}
					if($scope.bbox[i].recyclePrice =="-1" || $scope.bbox[i].recyclePrice ==="" || $scope.bbox[i].recyclePrice==null){
						$scope.bbox[i].recycleP ="未回收";
						if($scope.bbox[i].recyclePrice =="-1"){
							$scope.bbox[i].isNorecycle=false;
						}else{
							$scope.bbox[i].isNorecycle=true;
						}
					}else{
						$scope.bbox[i].recycleP = $scope.bbox[i].recyclePrice;
					}
				
				}
			  
			
		}
   		
   		
   		$scope.li_click=function(i){
   			$scope.navdown=i;
   		};
   		
   		
   		$scope.export=function(){
   			var startime = $("#startime").val();
   			var endtime =$("#endtime").val();
   			var agNum =$('#agentNum option:selected').val();
   			var trType =$('#tradeType option:selected').val();
   			var state =$('#state option:selected').val();
   			var reState =$('#recycleState option:selected').val();
   			var inplay =$('#inplay option:selected').val();
   			var tkId = $("#tk").val();
   			var uid = $("#uid").val();
   			var ballType = $("#ballState").val();	
   		
   				window.open(urlip+"account/orderManage/userManagementExcel?startDate="+startime +"&endDate="+endtime+"&agent_id="+agNum+"&trade_type="+trType+"&state="+state+"&recycleState="+reState+"&inplay="+inplay+"&tkId="+tkId+"&uid="+uid+"&ballType="+ballType);
   			
   		}
   		
   		
   		
   		
   		

    })
    

    
  function query(){
    	var startime = $("#startime").val();
		var endtime =$("#endtime").val();
		var agNum =$('#agentNum option:selected').val();
		var trType =$('#tradeType option:selected').val();
		var state =$('#state option:selected').val();
		var reState =$('#recycleState option:selected').val();
		var inplay =$('#inplay option:selected').val();
		var tkId = $("#tk").val();
		var uid = $("#uid").val();
		var ballType = $("#ballState").val();	
    	
    	
	
	var data = {};
	data.startDate = startime;
	data.endDate = endtime;
	data.agent_id =	agNum;
	data.page = page;
	data.size = size;
	data.trade_type =trType;
	data.state =state;
	data.recycleState = reState;
	data.inplay =inplay;
	data.tkId =tkId;
	data.uid =uid;
	data.ballType = ballType;
	
		$.ajax({
			async:false,
			dataType:"json",
			url:urlip+"account/orderManage/queryAll",
			data:data,
			type:"post",
			success:function(data){
				model=data;
				return  model;
			},
			error:function(data){
				
			}
		});		
		
   }  
     
    
    
    
    
    
    
    //查询渠道
function ajax(){
	var counter = '';
	$.ajax({
		async:false,
		dataType:"json",
		url:urlip+"account/orderManage/queryAllAgent",
		type:"get",
		success:function(data){
			counter = data.agentList;
		},
		error:function(data){
			alert("查询渠道信息报错")
		}
	});
	return counter;
}
    
