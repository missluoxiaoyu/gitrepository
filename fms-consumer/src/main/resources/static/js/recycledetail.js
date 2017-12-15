 

/*
  $(function () {
	   $('#daterange-btn').datepicker({
	        autoclose: true
	    });
    }); 
*/
  
  
    
    $('#daterange-btn').daterangepicker({
		ranges: {
			'今天': [moment(), moment().add('days',1)],
			'本周': [moment().startOf('week'), moment().endOf('week')],
			'本月': [moment().startOf('month'), moment().endOf('month')]
		
		},
		startDate: moment(),
		endDate: moment().endOf('month')
	},
	function(start, end) {
		$('#daterange-btn span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
		//alert(start.format('YYYYMMDD') + " " + end.format('YYYYMMDD'));
		startDate = start.format('YYYY-MM-DD');
		endDate = end.format('YYYY-MM-DD');
		
	}
);
    


var url ="http://"+window.location.host+"/account/channelStatistics/recycleDetail"

   
    function  query (){
   	
	
   		var datas={};
   		
   		
   		datas.startDate=startDate;
   		datas.endDate = endDate;
   		datas.agentId = agentId;
   		
   		$.ajax({
            type: "GET",
            data:datas,
            url: url,
            dataType: "json",
            success: function (data) {
            
             var total =  data.total;
             var list = data.list;
            
             
     		$("#tablesss").remove();
     		$("#recyde").append(' <table id="tablesss"  style="width:100%;" ></table>');
     		
            $("#tablesss").append(' <tr> <td align="center">合计</td><td align="center">-</td><td align="center">'+total.recyclePrice+'</td> <td align="center">-</td></tr>');
     		$("#tablesss").append(' <tr> <td align="center">渠道名称</td><td align="center">方案号</td> <td align="center">回收金额</td><td align="center">回收时间</td></tr>');
     						
     		   for(var i=0 ; i< list.length; i++){
     		      $("#tablesss").append(' <tr> <td align="center">'+list[i].agentName+'</td>  <td align="center">'+list[i].tkId+'</td>  <td align="center">'+list[i].recyclePrice+'</td>  <td align="center">'+list[i].recycleTime+'</td> </tr>');
     			 }	 
         
           	},
            error: function (XMLHttpRequest, txtStatus, errorThrown) {
                alert(XMLHttpRequest + txtStatus + errorThrown);
            }
        });

   };
    
   
   
   
      function   exportExcel  () {
	   
		  window.open("detailExcel?startDate="+startDate+"&endDate="+endDate+"&agentId="+agentId);						 
    }
   



 



