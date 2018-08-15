
/**
 * 提交查询条件数据，重新刷新datagrid
 */
function queryData() {
	var node = $('#user').combotree('tree').tree('getSelected');		// get selected node
	var opts = $('#view_user_table').datagrid('options');
	var queryParameter = opts.queryParams;
	if(node){
		if(node.text!='' &&  node.attributes == undefined){
			/** 绑定用户id */
//			alert("org"+node.text);
			queryParameter.userName = $("#user").val();
			queryParameter.userId =0;
		}
		else{
			/** 绑定组织机构id */
//			alert("user"+node.text);
			queryParameter.userName='-1';
			queryParameter.userId = $("#user").val();
		}
	}
	/** 绑定角色id */
	queryParameter.id = $("#role").val();
	$('#view_user_table').datagrid("reload");
}
/**
 *用户搜索详情datagrid初始化
 */
$(function() {
	$('#view_user_table').dg({
		url: "/statistics/usersearch",
		singleSelect : true,
	    columns:[[
	        {field:'userName',title:'用户姓名',width:"25%",align:'center'},
	        {field:'keyWord',title:'搜索关键词',width:"40%",align:'center'},
	        {field:'sarchNum',title:'搜索次数',width:"15%",align:'center'},
	        {field:'opt',title:'操作', width:"20%",align:'center',
	            formatter:function(value,rec){
	                var btn = '<a class="editcls"  onclick="showdialog(\'' + rec.userId + '\',\'' + rec.userName + '\',\'' + rec.keyWord + '\')" >查看明细</a>';
	                return btn;
	            }
	        }
	    ]],
	   // method:"post",
	    onLoadSuccess:function(data){
	        $('.editcls').linkbutton({text:'查看详细',plain:true,iconCls:'icon-edit'});
	    }
	});
});


/**
 * 重置
 */
function onClear(){
	$('#synonymform').form('clear');  
	var opts = $('#view_user_table').datagrid('options');
	var queryParameter = opts.queryParams;
	queryParameter.userName ='-1';
	queryParameter.userId =0;
	$('#user').combotree('clear');
	$('#user').combotree('reload',"/user/role/orgusertree?roleId=0");  
	$("#user").combotree('tree').tree("collapseAll");
}
/**
 * 角色下拉列表
 */
$("#role").combobox({
   url:'/statistics/usersearch/role',
   method:'post',
   valueField: 'id',
   textField: 'name',
   width:'60%',
  // panelHeight: 'auto',
   onSelect:function(role){  
	   $('#user').combotree('clear');
//     $('#user').combobox('reload',"/statistics/usersearch/user?id="+role.id); 
	   $('#user').combotree('reload',"/user/role/orgusertree?roleId="+role.id);  
	   $("#user").combotree('tree').tree("collapseAll");
   } 
}); 
/**
 * 展示用户组织机构树下拉框
 */
$("#user").combotree({
	url:'/user/role/orgusertree?roleId=0',
	method:'post',
	width:'60%'
}); 

/**
 * 用户搜索文档详情dialog
 */
$("#dialog").dialog({
	title: "查阅文档详情",
	width:'800px',
	height:'400px',
	modal: true,
	closed: true,
});

/**
 * 弹出用户文档搜索情况的详情页面
 * @returns
 */
function showdialog(userId,userName,keyWord){
	$('#dialog').dialog('open');
	$("#userId").val(userId);
	$("#userName").html(userName);
	$("#queryKeyWord").html(keyWord);
	var opts = $('#view_userdetail_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定用户id */
	queryParameter.userId = userId;
	/** 绑定搜索词 */
	queryParameter.queryKeyWord = keyWord;
	$('#view_userdetail_table').datagrid({url:'/statistics/userDocumentsearch'});
	$('#view_userdetail_table').datagrid("reload");
}
/**
 * 弹出用户文档搜索情况的详情列表初始化页面
 * @returns
 */
$(function() {
	$('#view_userdetail_table').dg({
		height:310,
		columns:[[
	        {field:'docTitle',title:'查阅文档名称',width:"27%",align:'center'},
	        {field:'docLocation',title:'文档路径',width:"55%",align:'center'},
	        {field:'querycount',title:'查阅频次',width:"20%",align:'center'}
	    ]],
	   // method:"post",
	    onLoadSuccess:function(data){
	    }
	});
})