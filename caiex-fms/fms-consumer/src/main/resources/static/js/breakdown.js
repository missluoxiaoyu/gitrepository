var app = angular.module('Breakdown',[]);

var brbox = [
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
	"texa":"Daily Breakdown",
	"active":true
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
}];
app.controller("breakctrl",function(){
		this.brbox = brbox;
});
var dobox = [
{"worda":"",
"wordb":"人民币元RMB",
"wordc":"原始投注额",
"wordd":"交易额",
"worde":"中奖金额",
"wordf":"盈利率",
"wordg":"中奖交易额",
"wordh":"平均奖金"},
{"worda":"",
"wordb":"",
"wordc":"TotalInvestment",
"wordd":"Investment",
"worde":"Payout",
"wordf":"Payout Ratio",
"wordg":"Amt.Winning Unit",
"wordh":"Average Winning Odds"}];

var dnbox = [
{"texta":"HAD",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"},
{"texta":"HHAD",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"},
{"texta":"HAFU",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"},
{"texta":"TTG",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"},
{"texta":"CRS",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"},
{"texta":"FCA",
"numa":"0",
"numb":"0",
"numc":"0",
"numd":"0",
"nume":"0",
"numf":"0",
"numg":"0",
"numh":"0",
"numi":"0",
"numj":"0",
"numk":"0",
"numl":"0",
"numm":"0",
"numn":"0",
"numo":"0",
"nump":"0",
"numq":"0",
"numr":"0"}];
app.controller("brdownctrl",function(){
		this.dnbox = dnbox;
		this.dobox = dobox;
});










