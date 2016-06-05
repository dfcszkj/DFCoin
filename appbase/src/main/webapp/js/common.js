//重新加载验证码
function reloadVerifyImg(){
	var $img = $("#verify-img");
	var verifyImgUrl = $img.attr("src");
	$img.attr("src",verifyImgUrl + "?rnd=" + Math.random());
}

function showErr(msg){
	layer.msg(msg);
	//$('#_msg > b').addClass('red').text(msg);
}
function clearErr() {
	$('#_msg > b').removeClass('red').text("");
}
// 全选
function selectAll(ischeck,checkboxs){
	for(var index in checkboxs){
		checkboxs[index].checked = ischeck;
	}
} 

