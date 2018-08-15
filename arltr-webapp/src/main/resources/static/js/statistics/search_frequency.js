
/**
 * 查询
 */
function queryData() {
	var opts = $('#view_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定词汇名称 */
	queryParameter.queryKeyWord = $("#queryKeyWord").val();
	$('#view_m_table').datagrid("reload");
}

$(function() {
	$('#view_m_table').dg({
		remoteSort:true,
//		toolbar:'#tb',
		url: "/statistics/frequencydata",
	    columns:[[
//	        {field:'ck',checkbox:true},  //,sortable:true
	        {field:'queryKeyWord',title:'搜索关键词',width:"30%"},
	        {field:'docTitle',title:'查询文档名称',width:"50%"},
	        {field:'querycount',title:'查阅次数',width:"20%"}
	    ]],
	   // method:"post",
	    onLoadSuccess:function(data){
	      //  $('.editcls').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
	    },
	onClickCell: function(data){},
	onClickRow:function(data){}
    
	});
});


/**
 * 重置
 */
function onClear(){
	$('#searchmform').form('clear');  
}