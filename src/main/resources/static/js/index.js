$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	debugger
	$("#publishModal").modal("hide");

	//获取标题和内容
	var title = $("#recipient-name").val();
	var content  = $("#message-text").val();
	$.post(
		"/community/discuss/add",
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