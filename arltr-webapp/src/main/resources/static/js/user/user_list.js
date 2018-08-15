
/**
 * 提交数据
 */
function submitData() {
	var opts = $('#user_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定词汇名称 */
	queryParameter.userName = $("#userName").val();
	queryParameter.employeeName = $("#employeeName").val();
	queryParameter.employeeNo = $("#employeeNo").val();
	queryParameter.employeeOrg = $("#employeeOrg").val();
	queryParameter.employeeTitle = $("#employeeTitle").val();
	
	
	$('#user_m_table').datagrid("reload");
	//$('#mask_m_table').datagrid('loadData', queryParameter);
}


$(function() {
    $('#user_m_table').dg({
        url: "/user/query",
        height:cmTableH,
        /*singleSelect:true,*/
        remoteSort:true,
        columns:[[
            //{field:'ck',checkbox:true},
            {field:'employeeName',title:'职员姓名',width:"12%"},
            {field:'employeeNo',title:'职员编号',width:"17%"},
            {field:'userName',title:'登录账号',width:"17%"},
            {field:'employeeTitleName',title:'职务',width:"14%",hidden : true},
            {field:'employeeOrg',title:'组织机构',width:"17%",formatter:function(value,rec){
                    var btn
                    if(rec.orgnazation!=null){
                        btn = rec.orgnazation.name
                    }
                    return btn;
                }},
            {field:'securityClassName',title:'密级',width:"12%"},
            {field:'updateAt',title:'更新时间',width:"15%",sortable:true,formatter:getDateFormatter},
            {field:'opt',title:'操作', width:"11%",align:'center',
                formatter:function(value,rec){
                    var btn =
                        '<a  href="/user/'+rec.id+'" class="editcls">查看详细</a>';
                    return btn;
                }
            }
        ]],
        onLoadSuccess:function(data){
//	        $('.editcls').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
//	        $('.removecls').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
            $('.editcls').linkbutton({text:'查看详细',plain:true,iconCls:'icon-edit'});
        }
    });
});

/**
 * 重置
 */
function onClear(){
	$('#userform').form('clear');  
}
/**
 * 职务 下拉列表
 */
$("#employeeTitle").combobox({
	url:"/user/enumeration",
	method:'get',
	valueField: 'value',
	   textField: 'valueName',
	   panelHeight:'auto',
	   editable:false,
	   onLoadSuccess:function(data){
	   }
	})
/**
 * 组织机构树
 */
$('#employeeOrg').combotree({
    url:'/user/role/orgtree',
    method:'post'
    //panelHeight:'auto'
});