
/**
 * 提交数据
 */
function submitData() {
	var opts = $('#mask_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定词汇名称 */
	queryParameter.word = $("#word").val();
	$('#mask_m_table').datagrid("reload");
	//$('#mask_m_table').datagrid('loadData', queryParameter);
}

function deleteOne(id){
	
	$.messager.confirm('删除确认', "确定删除当前记录吗？",function(ok){
		if(ok){
			$.httpPost("/mask/"+id+"/remove",null,true,function(resp){
				$('#mask_m_table').datagrid("reload");
			});
		}
	})
}


function deleteMore(){
	var list=[];
	var checkedItems = $('#mask_m_table').datagrid('getChecked');
	
	if(checkedItems == null || "" == checkedItems){
		$.messager.alert('警示信息', "请选择要删除的行！",'warning');
		return;
	}
	var a = JSON.stringify(checkedItems)
	
	$.messager.confirm('删除确认', "确定删除所选记录吗？",function(ok){
		if(ok){
			$.httpPost("/mask/remove/multiple",checkedItems,true,function(resp){
				$('#mask_m_table').datagrid("reload");
			});
		}
	})
}


//$('#mask_m_table').datagrid({
//    url: "/mask/query",
//    toolbar : "#tb",
//    rownumbers:false,
//    columns:[[
//        {field:'ck',checkbox:true},
//        {field:'word',title:'词汇',width:"20%",sortable:true},
//        {field:'createAt',title:'创建时间',width:"20%",sortable:true,formatter:getDateFormatter},
//        {field:'createByName',title:'创建者',width:"14%",sortable:true},
//        {field:'updateAt',title:'更新时间',width:"20%",sortable:true,formatter:getDateFormatter},
//        {field:'updateByName',title:'更新者',width:"14%",sortable:true},
//        {field:'opt',title:'操作', width:"10%",align:'center',
//            formatter:function(value,rec){
//                var btn =
//                    '<a  href="/mask/'+rec.id+'" class="editcls">编辑</a>'+'  '+
//                    '<a class="removecls" onclick="deleteOne('+rec.id+')">删除</a>';
//                return btn;
//            }
//        }
//    ]],
//    onLoadSuccess:function(data){
////        $('.editcls').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
////        $('.removecls').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
//    }
//});


$(function() {
	$('#mask_m_table').dg({
	    url: "/mask/query",
	    toolbar : "#tb",
	    remoteSort:true,
	    columns:[[
	        {field:'ck',checkbox:true},
	        {field:'word',title:'词汇',width:"40%"},
	        {field:'createAt',title:'创建时间',width:"15%",sortable:true,formatter:getDateFormatter},
	        {field:'createByName',title:'创建者',width:"8%"},
	        {field:'updateAt',title:'更新时间',width:"15%",sortable:true,formatter:getDateFormatter},
	        {field:'updateByName',title:'更新者',width:"8%"},
	        {field:'opt',title:'操作', width:"12%",align:'center',
	            formatter:function(value,rec){
	                var btn =
	                    '<a  href="/mask/'+rec.id+'" class="editcls">编辑</a>'+'  '+
	                    '<a class="removecls" onclick="deleteOne('+rec.id+')">删除</a>';
	                return btn;
	            }
	        }
	    ]],
	    onLoadSuccess:function(data){
	        $('.editcls').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
	        $('.removecls').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
	    }
	});
});

/**
 * 重置
 */
function onClear(){
	$('#maskform').form('clear');  
}