$(function () {
	$(".handle").click(function(event){
		var e=window.event || event;
		if(e.stopPropagation){
			e.stopPropagation();
		}else{
			e.cancelBubble = true;
		}   
		$('#overlay').fadeIn('fast', function() {
			$('#box').animate({
				'top': '300px'
			}, 500);
		});
	});
	$("#box").click(function(event){
		var e=window.event || event;
		if(e.stopPropagation){
			e.stopPropagation();
		}else{
			e.cancelBubble = true;
		}
	});
	$('.qu').click(function() {
		$('#box').animate({
			'top': '-1000px'
		}, 500, function() {
			$('#overlay').fadeOut('fast');
		});
	});
	document.onclick = function(){
		$('#box').animate({
			'top': '-1000px'
		}, 500, function() {
			$('#overlay').fadeOut('fast');
		});
	};
});