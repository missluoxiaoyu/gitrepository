var app = angular.module('channel',['angular-loading-bar']);

var urlip = "http://192.168.1.5:8083/account/";

	app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	    cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
	    cfpLoadingBarProvider.spinnerTemplate = '<div id="loadgif" style="width:66px;height:66px;position:absolute;top:30%;left:50%;"><img  alt="加载中..." src="img/loading.gif"/></div>';
	   	cfpLoadingBarProvider.latencyThreshold = 20;
	}]);

var chbox = [
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
	"texa":"渠道统计报表",
	"active":true
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
}];
//修改更新
app.factory('update', ['$http', '$q', '$location', function($http, $q, $location) {
	return {
		query: function(updatee,agN,agI,urlid,paW,pre,prea) {
				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
				$http({
					method: "GET",
	                url: urlip +'/channelStatistics/update?updateflag='+updatee+'&agentName='+agN+'&agentId='+agI+'&url='+urlid+'&password='+paW+'&prestore='+pre+'&prestoreAlarm='+prea
				}).
				success(function(data) {
					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
					console.log(updatee,agN,agI,urlid,paW,pre,prea);
				}).
				error(function(data) {
					deferred.reject(data); // 声明执行失败，即服务器返回错误  
				});
				return deferred.promise;
			} // end query  
	};
}]);
//添加渠道
app.factory('add', ['$http', '$q', '$location', function($http, $q, $location) {
	return {
		query: function(aE,aC,aN,ul,pW,pT,pA) {
				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
				$http({
					method: "GET",
	                url: urlip +'/channelStatistics/add?agentName='+aE+'&agentCode='+aC+'&agentNum='+aN+'&url='+ul+'&password='+pW+'&prestore='+pT+'&prestoreAlarm='+pA	
				}).
				success(function(data) {
					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
					console.log(aE,aC,aN,ul,pW,pT,pA)
				}).
				error(function(data) {
					deferred.reject(data); // 声明执行失败，即服务器返回错误  
					alert(data);
				});
				return deferred.promise;
			} // end query  
	};
}]);
//开停售
app.factory('sell', ['$http', '$q', '$location', function($http, $q, $location) {
	return {
		query: function(agentId,agentSell) {
				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
				$http({
					method: "GET",
	                url: urlip +'channelStatistics/updateSell?agentId='+agentId+'&agentSell='+agentSell
				}).
				success(function(data) {
					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
//					alert("成功")
				}).
				error(function(data) {
					deferred.reject(data); // 声明执行失败，即服务器返回错误  
				});
				return deferred.promise;
			} // end query  
	};
}]);
//全部开停售
app.factory('allsell', ['$http', '$q', '$location', function($http, $q, $location) {
	return {
		query: function(agentSell) {
				var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
				$http({
					method: "GET",
	                url: urlip +'channelStatistics/updateSellAll?agentSell='+agentSell
				}).
				success(function(data) {
					deferred.resolve(data); // 声明执行成功，即http请求数据成功，可以返回数据了  
				}).
				error(function(data) {
					deferred.reject(data); // 声明执行失败，即服务器返回错误  
				});
				return deferred.promise;
			} // end query  
	};
}]);

app.controller("chanctrl",function(){
		this.chbox = chbox;
});
app.controller("chanelctrl",function($scope,$http,cfpLoadingBar,update,add,sell,allsell){
	var _get = function(year,month,day){
		$http.get(urlip +'/channelStatistics/queryAll?year='+year+'&month='+month+'&day='+day)
		.success(function(data){
			if(data){
				$scope.lebox = data.agentInfoModels;
				$scope.model = data.model;
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
//	_get();     //初始化
	
	$scope.addAgent=function(){		
		var html = "<label class='youqi'>渠道名称：<input class='confirm_input' id='agentNameadd' placeholder='100新浪'></label>";
		html += "<label class='youqi'>渠道编码：<input class='confirm_input' id='agentCodeadd' placeholder='100'></label>";
		html += "<label class='youqi'>渠道编号：<input class='confirm_input' id='agentNumadd' placeholder='100'></label>";
		html += "<label class='youqi'>渠道url：<input class='confirm_input' id='urladd' placeholder='http://123.65.217.102:12112/oltpNotify/sendTrade'></label>";
		html += "<label class='youqi'>渠道密码：<input class='confirm_input' id='passwordadd' placeholder='123456'></label>";		
		html += "<label class='youqi'>预存额：<input class='confirm_input' id='prestoreadd' placeholder='20'></label>";
		html += "<label class='youqi'>存额报警线：<input class='confirm_input' id='prestoreAlarmadd' placeholder='18'></label>";
		layer.confirm(html, {
			btn: ['取消', '添加'],
			yes: function(){
				layer.msg('取消了', {
					icon: 5
				});
			},
			btn2: function(){
//				agentSell=0;
				var agentName = $('#agentNameadd').val();
				var agentCode = $('#agentCodeadd').val();
				var agentNum = $('#agentNumadd').val();
				var url = $('#urladd').val();
				var password = $('#passwordadd').val();
				var prestore = $('#prestoreadd').val();
				var prestoreAlarm = $('#prestoreAlarmadd').val();
				var upda = add.query(agentName,agentCode,agentNum,url,password,prestore,prestoreAlarm);
					upda.then(function(data){
						console.log(agentName,agentCode,agentNum,url,password,prestore,prestoreAlarm);
						if(data.result.resultCode =="1"){
							layer.msg(data.result.resultMsg ,{icon:'6'});
						}else{
							layer.msg(data.result.resultMsg ,{icon:'5'});
						}
					},function(data){
					}
				)
			}
		});
	}
	$scope.updateAgentName=function(agentName,agentCode,agentId,url,password,prestore,prestoreAlarm,upnew){	
		var html = "<label class='youqi'>渠道名称：<input class='confirm_input' id='agentNameid' value="+ agentName +"></label>";
		html += "<label class='youqi'>渠道编码：<input class='confirm_input noline' id='agentCodeid' value="+ agentCode +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道编号：<input class='confirm_input noline' id='agentIdid' value="+ agentId +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道url：<input class='confirm_input' id='urlid' value="+ url +"></label>";
		html += "<label class='youqi'>渠道密码：<input class='confirm_input' id='passwordid' value="+ password +"></label>";		
		html += "<label class='youqi'>预存额：<input class='confirm_input noline' id='prestoreid' value="+ prestore +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>存额报警线：<input class='confirm_input noline' id='prestoreAlarmid' value="+ prestoreAlarm +" readonly onfocus='this.blur()'></label>";
		layer.confirm(html, {
			btn: ['取消', '修改'],
			yes: function(){
				layer.msg('取消了', {
					icon: 5
				});
			},
			btn2: function(){
				console.log(agentId);
				var agentName = $('#agentNameid').val();
				var url = $('#urlid').val();
				var password = $('#passwordid').val();
				var upda = update.query(upnew,agentName,agentId,url,password,prestore,prestoreAlarm);
					upda.then(function(data){
			  			if(data.result.resultCode =="1"){
							layer.msg(data.result.resultMsg ,{icon:'6'});
						}else{
							layer.msg(data.result.resultMsg ,{icon:'5'});
						}
					},function(data){
				})
			}
		});
	}
	$scope.prestore=function(agentName,agentCode,agentId,url,password,prestore,prestoreAlarm,upnew){	
		var html = "<label class='youqi'>渠道名称：<input class='confirm_input' id='agentNameyu' value="+ agentName +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道编码：<input class='confirm_input noline' id='agentCodeyu' value="+ agentCode +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道编号：<input class='confirm_input noline' id='agentIdyu' value="+ agentId +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>原预存额：<input class='confirm_input' id='prestoreold' value="+ prestore +" readonly onfocus='this.blur()'></label>";		
		html += "<label class='youqi'>预存额报警线：<input class='confirm_input noline' id='prestoreAlarmyu' value="+ prestoreAlarm +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>预存额：<input class='confirm_input noline' id='prestoreyu' placeholder="+ prestore +" value="+ prestore +"></label>";
		layer.confirm(html, {
			btn: ['取消', '修改']
		}, function() {
			layer.msg('取消了', {
				icon: 5
			});
		},function(){
			var prestore = $('#prestoreyu').val();
			upnew=1
			var upda = update.query(upnew,agentName,agentId,url,password,prestore,prestoreAlarm);
			upda.then(function(data){
					console.log(prestore);
				if(data.result.resultCode =="1"){
					layer.msg(data.result.resultMsg ,{icon:'6'});
				}else{
					layer.msg(data.result.resultMsg ,{icon:'5'});
				}
			},function(data){
//				console.log(upnew,prestore);
			})
		}
		);
	}
	$scope.saveOrdrawDetail=function(agentId){
		var detail = function(){  
	        $http.get(urlip +'channelStatistics/queryPreStoreDetail?agentId='+agentId)
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
	$scope.prestoreAlarm=function(agentName,agentCode,agentId,url,password,prestore,prestoreAlarm,upnew){	
		var html = "<label class='youqi'>渠道名称：<input class='confirm_input' id='agentNamecop' value="+ agentName +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道编码：<input class='confirm_input noline' id='agentCodecop' value="+ agentCode +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>渠道编号：<input class='confirm_input noline' id='agentIdcop' value="+ agentId +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>预存额：<input class='confirm_input' id='prestorecop' value="+ prestore +" readonly onfocus='this.blur()'></label>";		
		html += "<label class='youqi'>原预存额报警线：<input class='confirm_input noline' id='prestoreAlarmold' value="+ prestoreAlarm +" readonly onfocus='this.blur()'></label>";
		html += "<label class='youqi'>预存额报警线：<input class='confirm_input noline' id='prestoreAlarmcop' placeholder="+ prestoreAlarm +" value="+ prestoreAlarm +"></label>";
		layer.confirm(html, {
			btn: ['取消', '修改']
		}, function() {
			layer.msg('取消了', {
				icon: 5
			});
		},function(){
			var prestoreAlarm = $('#prestoreAlarmcop').val();
			upnew=2;
			var upda = update.query(upnew,agentName,agentId,url,password,prestore,prestoreAlarm);
			upda.then(function(data){
					console.log(upnew,prestoreAlarm);
				if(data.result.resultCode =="1"){
					layer.msg(data.result.resultMsg ,{icon:'6'});
				}else{
					layer.msg(data.result.resultMsg ,{icon:'5'});
				}
			},function(data){
				console.log(agentName);
			})
		});
	}
	$scope.start=function(agentId,agentSell,$index){
		if(RGBToHex(document.getElementById("lu"+$index).style.background).substring("0",'7') !="#d0dde9"){
			//alert();
			layer.confirm('start sell ?', {
				btn: ['取消', '确定']
			}, function() {
				layer.msg('取消了', {
					icon: 5
				});
			},function(){
				agentSell=1;
				var agentsel = sell.query(agentId,agentSell);
				agentsel.then(function(data){
					if(data.result.resultCode =="1"){
						layer.msg(data.result.resultMsg ,{icon:'6'});
						var tmp =document.getElementById("lu"+$index);
						tmp.style.background="#d0dde9";	
					}else{
						layer.msg(data.result.resultMsg ,{icon:'5'});
					}
				},function(){
					alert("更新失败");
				})
			});
		}
	}		
	$scope.stop=function(agentId,agentSell,$index){		
		if(RGBToHex(document.getElementById("hong"+$index).style.background).substring("0",'7') !="#f4bcbc"){
			layer.confirm('stop sell ?', {
				btn: ['取消', '确定']
			}, function() {
				layer.msg('取消了', {
					icon: 5
				});
			},function(){
				agentSell=0;
				var agentsel = sell.query(agentId,agentSell);
				agentsel.then(function(data){
					if(data.result.resultCode =="1"){
						layer.msg(data.result.resultMsg ,{icon:'6'});
						var tmp =document.getElementById("hong"+$index);
						tmp.style.background="#f4bcbc";
					}else{
						layer.msg(data.result.resultMsg ,{icon:'5'});
					}
				},function(){
					alert("更新失败");
				})
			});
		}
	}
	$scope.startall=function(agentSell){
		if(RGBToHex(document.getElementById("luu").style.background).substring("0",'7') !="#d0dde9"){
			layer.confirm('all on sale?', {
				btn: ['取消', '确定']
			}, function() {
				layer.msg('取消了', {
					icon: 5
				});
			},function(){
				agentSell=1;
				var agentsel = allsell.query(agentSell);
				agentsel.then(function(data){
					if(data.result.resultCode =="1"){
						layer.msg(data.result.resultMsg ,{icon:'6'});
						var tmp =document.getElementsByClassName("lu");
						for(var i =0;i<tmp.length;i++){
							tmp[i].style.background="#d0dde9";	
						}
					}else{
						layer.msg(data.result.resultMsg ,{icon:'5'});
					}
				},function(data){
					//alert("更新失败");
				})
			});
		}
	}
	$scope.stopall=function(agentSell){		
		if(RGBToHex(document.getElementById("hongg").style.background).substring("0",'7') !="#f4bcbc"){
			layer.confirm('stop selling all?', {
				btn: ['取消', '确定']
			}, function() {
				layer.msg('取消了', {
					icon: 5
				});
			},function(){
				agentSell=0;
				var agentsel = allsell.query(agentSell);
				agentsel.then(function(data){
					if(data.result.resultCode =="1"){
						layer.msg(data.result.resultMsg ,{icon:'6'});var tmp =document.getElementsByClassName("hong");
					for(var i =0;i<tmp.length;i++){
						tmp[i].style.background="#f4bcbc";	
					}
					}else{
						layer.msg(data.result.resultMsg ,{icon:'5'});
					}
				
				},function(data){
					//alert("更新失败");
				})
			});
		}
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
//			var execl = excel.queryallup(year,month,day);
			window.open(urlip +'channelStatistics/channelStatisticsExcel?year='+year+"&month="+month+"&day"+day);

//			console.log(year,month,day)
//			execl.then(function(data){
//				window.open('http://192.168.1.5:8083/account/channelStatistics/channelStatisticsExcel?year='+year+"&month="+month+"&day"+day);
//				console.log(data)
//			},function(data){
//			})
		} 
}
});










