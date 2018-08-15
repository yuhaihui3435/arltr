/**
 * 获取数据
 */
function getData() {
	$.httpGet("/sample/get?cmd=4", function(resp) {
		alert(resp);
	});
	return false;
}

/**
 * 提交数据
 */
function submitData() {
	
	var d = $("#a").getFormData();
	alert(JSON.stringify(d));
	
	return false;
	var param = {
			name : "开始",
			code : "123456"
	};
	
	$.httpPost("/sample/post?cmd=5", param, true, function(resp) {
		$.messager.alert("提示消息", resp.name,"info");
	});
	
//	$.httpPost("/sample/post?cmd=5", $("#mainForm").getFormData(), true, function(resp) {
//		$.messager.alert("提示消息", JSON.stringify(resp),"info");
//	});
	
	return false;
}

$(function() {
	$('#user_m_table').dg({
	    url: "/sample/list/data",
	    columns:[[
	        {field:'r1',title:'列1',width:"10%",sortable:true},
	        {field:'r2',title:'列2',width:"10%",sortable:true},
	        {field:'r3',title:'列3',width:"10%",sortable:true},
	        {field:'opt',title:'操作', width:"10%",align:'center',
	            formatter:function(value,rec){
	                var btn =
	                    '<a class="editcls">编辑</a>'+
	                    '<a class="removecls">删除</a>';
	                return btn;
	            }
	        }
	    ]],
	    onLoadSuccess:function(data){
	        $('.editcls').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
	    }
	});
});

