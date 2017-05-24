var app = angular.module('finance',[]);

var nbox = [
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
	"texa":"Daily Single"
},
{
	"urlb":"channel.html",
	"texa":"渠道统计报表"
},
{
	"urlb":"finance.html",
	"texa":"Summary",
	"active":true
},
{
	"urlb":"summary.html",
	"texa":"Daily Summary"
},
{
	"urlb":"user.html",
	"texa":"用户管理"
}];
app.controller("financtrl",function(){
		this.nbox = nbox;
});
var dobox = [
{"worda":"",
"wordb":"场次数量",
"wordc":"原始投注额",
"wordd":"交易额",
"worde":"中奖金额",
"wordf":"盈利率"},
{"worda":"",
"wordb":"NO. Match",
"wordc":"TotalInvestment",
"wordd":"Investment",
"worde":"Payout",
"wordf":"Payout Ratio"},
{"worda":"",
"wordb":"",
"wordc":"人民币 RMB",
"wordd":"人民币 RMB",
"worde":"人民币 RMB",
"wordf":"%"}];

var dnbox = [
{"texta":"2017-04-18",
"textb":"",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"本周截至当日 WTD",
"textb":"0",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"本月截至当日 MTD",
"textb":"11",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"本年截至当日 YTD",
"textb":"20",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"月份明细 Month",
"textb":"",
"numa":"",
"numb":"",
"numc":"",
"numd":"",
"nume":"",
"numf":"",
"numg":"",
"numh":"",
"numi":"",
"numj":"",
"numk":"",
"numl":"",
"numo":"",
"nump":"",
"numq":"",
"numr":""},
{"texta":"一月 Jan",
"textb":"0",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"二月 Feb",
"textb":"6",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"},
{"texta":"三月 Mar",
"textb":"11",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0%",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0%",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0%",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0%"}];
app.controller("fincectrl",function(){
		this.dnbox = dnbox;
		this.dobox = dobox;
		this.month = "04";
		this.week = "16";
});










