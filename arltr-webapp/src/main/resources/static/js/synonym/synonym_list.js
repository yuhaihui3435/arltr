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
	var opts = $('#synonym_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定词汇名称 */
	queryParameter.word = $("#word").val();
	$('#synonym_m_table').datagrid("reload");
}

$(function() {
	$('#synonym_m_table').dg({
		remoteSort:true,
		toolbar:'#tb',
		url: "/synonym/list/data",
	    columns:[[
	        {field:'ck',checkbox:true},
	        {field:'word',title:'词汇',width:"15%",sortable:false},
	        {field:'synonymWord',title:'同义词',width:"30%",sortable:false},
	        {field:'createAt',title:'创建时间',width:"12%",sortable:true, formatter:getDateFormatter},
	        {field:'createByName',title:'创建者',width:"9%",sortable:false},
	        {field:'updateAt',title:'更新时间',width:"12%",sortable:true,formatter:getDateFormatter},
	        {field:'updateByName',title:'更新者',width:"9%",sortable:false},
	        {field:'opt',title:'操作', width:"12%",align:'center',
	            formatter:function(value,rec){
	                var btn =
		                '<a class="editcls" href="/synonym/detail/'+rec.id+'">编辑</a>'+
	                    '<a class="removecls" onclick="doDeleteOne(\'' + rec.id + '\')">删除</a>';
	                
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
 * 单条删除信息
 */
function doDeleteOne(id){
	$.messager.confirm('删除确认', "确定删除当前记录吗？",function(ok){
				if(ok){
					$.httpPost("/synonym/remove/"+id, null, true, function(resp) {
						$('#synonym_m_table').datagrid('reload');
					});
				}
			})
}

/**
 * 批量删除信息
 */
function doDelete(){
	   var paramlist=[];
		var rows = $('#synonym_m_table').datagrid('getSelections');
		$.each(rows, function(index, item){
			var lexicon = {
					id : item.id,
					word : item.word
			};
			paramlist.push(lexicon);
		});
		if (rows.length>0){
			$.messager.confirm('删除确认', "确定删除所选记录吗？",function(ok){
				if(ok){
					$.httpPost("/synonym/multiple",paramlist, true, function(resp) {
						$('#synonym_m_table').datagrid('reload');
					});
				}
			})
		}else{
			$.messager.alert('警示信息', '请选择要删除的行！','warning');
		}
	
}

/**
 * 重置
 */
function onClear(){
	$('#synonymform').form('clear');  
}