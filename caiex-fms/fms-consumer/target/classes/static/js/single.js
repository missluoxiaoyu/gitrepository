var app = angular.module('single',[]);

var gbox = [
{
	"urlb":"error.html",
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
	"texa":"Daily Single",
	"active":true
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
}];
app.controller("singctrl",function(){
		this.gbox = gbox;
});
var dobox = [
{"worda":"1003",
"wordb":"2017-04-17 20:30:00",
"wordc":"DF2",
"wordd":"多德勒支 vs 特尔斯达",
"worde":"1:2/1:2",
"wordu":"20",
"wordv":"21.53",
"wordw":"70",
"wordx":"21.53",
"wordy":"3.251",
"wordz":"225.128%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%"},
{"worda":"1003",
"wordb":"2017-04-17 20:30:00",
"wordc":"DF2",
"wordd":"多德勒支 vs 特尔斯达",
"worde":"1:2/1:2",
"wordu":"20",
"wordv":"21.53",
"wordw":"70",
"wordx":"21.53",
"wordy":"3.251",
"wordz":"225.128%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%",
"wordu":"0",
"wordv":"0",
"wordw":"0",
"wordx":"0",
"wordy":"0",
"wordz":"0%"}];

app.controller("glectrl",function(){
		this.dobox = dobox;
});










