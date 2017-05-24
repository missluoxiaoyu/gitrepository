var app = angular.module('summary',[]);

var mbox = [
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
	"texa":"Summary"
},
{
	"urlb":"summary.html",
	"texa":"Daily Summary",
	"active":true
},
{
	"urlb":"user.html",
	"texa":"用户管理"
}];
app.controller("summactrl",function(){
		this.mbox = mbox;
});
var ybox = [
{"texb":"2017-04 合计Total"},
{"texb":"11"},
{"texb":"0"},
{"texb":"0.00"},
{"texb":"0.00"},
{"texb":"0.00%"},
{"texb":"11"},
{"texb":"0"},
{"texb":"0.00"},
{"texb":"0.00"},
{"texb":"0.00%"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"},
{"texb":"-"}];
var arbox = [
{"worda":"第一周Week1",
"wordc":"场次数量",
"wordd":"原始投注额",
"worde":"交易额",
"wordf":"中奖金额",
"wordg":"盈利率"},
{"worda":"",
"wordc":"NO. Match",
"wordd":"TotalInvestment",
"worde":"Investment",
"wordf":"Payout",
"wordg":"Payout Ratio"},
{"worda":"",
"wordc":"",
"wordd":"人民币 RMB",
"worde":"人民币 RMB",
"wordf":"人民币 RMB",
"wordg":"%"}];

var mybox = [
{"texta":"-",
"numb":"-",
"numo":"-",
"nump":"-",
"numq":"-",
"numr":"-",
"numb":"-",
"nume":"-",
"numf":"-",
"numg":"-",
"numh":"-",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"-",
"numb":"-",
"numo":"-",
"nump":"-",
"numq":"-",
"numr":"-",
"numb":"-",
"nume":"-",
"numf":"-",
"numg":"-",
"numh":"-",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"2017-04-01 周三",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"2017-04-01 周四",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"2017-04-01 周五",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"2017-04-01 周六",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"2017-04-01 周日",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"},
{"texta":"合计",
"numb":"0",
"numo":"0",
"nump":"0.00",
"numq":"0.00",
"numr":"0.00%",
"numb":"0",
"nume":"0",
"numf":"0.00",
"numg":"0.00",
"numh":"0.00%",
"numb":"-",
"numi":"-",
"numj":"-",
"numk":"-",
"numl":"-"}];
app.controller("sumaryctrl",function(){
		this.ybox = ybox;
		this.arbox = arbox;
		this.mybox = mybox;
});










