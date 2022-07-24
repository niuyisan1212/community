$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	debugger
	$("#publishModal").modal("hide");
	//发送AJAX请求之前，将csrf令牌放置到请求的消息头中
	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header, token)
	// });
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content  = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function(data){
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);

			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code == 200){
					window.location.reload();
				}
			}, 2000);
		}
	);
}