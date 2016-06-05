/**小说采集规则**/
jQuery(function($){
	$("#save_button").click(function(){
		var param = $("#ruleForm").serialize() + getParam();
		$.ajax({
			dataType: "text",
			url: ctx  + "/rule/novel/doCreate",
			data: param,
			type:"POST",
			async: false,
			success: function(_msg){
				if(_msg == "success") {
					window.location.href=ctx+"/rule/novel/list";
				} else {
					alert("保存文件失败");
				}
			}
		});
	});
	
});

function showDialog() {
	$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
		_title: function(title) {
			var $title = this.options.title || '&nbsp;';
			if( ("title_html" in this.options) && this.options.title_html == true )
				title.html($title);
			else title.text($title);
		}
	}));
	$("#console").removeClass('hide').dialog({
		modal: true,
		title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-check'></i> 测试信息</h4></div>",
		title_html: true,
		width:500,
		position: ['center', 'top'],
		close:function(){
			Console.clear();
		},
		buttons: [ 
			{
				text: "关闭",
				"class" : "btn btn-primary btn-xs",
				click: function() {
					$( this ).dialog( "close" ); 
				}
			}
		]
	});
}
var Console = {};
Console.log = (function(message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = message;
    console.appendChild(p);
});
Console.clear = (function() {
    var console = document.getElementById('console');
    console.innerHTML = "";
});
// 测试规则
function _test() {
	var param = $("#ruleForm").serialize() + getParam();
	$.ajax({
		dataType: "text",
		url: ctx  + "/rule/novel/test",
		data: param,
		type:"POST",
		async: false,
		success: function(_msg){
			showDialog();
			Console.log(_msg);
		}
	});
}
function getParam() {
	var p = "&";
	$("#ruleForm input[type=checkbox]").each(function(){
    	p = p + $(this).attr("name").substring(1) + "=" + $(this).is(':checked') + "&";
    });
	return p;
}
function escapeSymbol(s) {
	return s.replace(/\&/g,'%26');
}