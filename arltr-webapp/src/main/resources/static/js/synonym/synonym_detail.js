/**
 * 系统参数列表
 */
var dataArr;
function init(){
	var swstr = $('#synonymWord').val();

	if(swstr=='' || swstr==null){
		dataArr = {
				"total": 0,
				"rows" : []
		}
	}else{
		var swarr = swstr.split(",");

		var jsonstr;jsonstr="[";
		for(var i =0;i<swarr.length;i++){	
//			rowsdata[i] = {'synonymOne':swarr[i]};
			jsonstr += "{synonymOne : \"" + swarr[i] + "\" },";
		}
		
		jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(','));
		jsonstr += "]";
		var rowsdata = eval(jsonstr);
		
		
		dataArr = {
				"total": swarr.length,
				"rows":rowsdata
		}
		
	}
	
}
	


$(function(){
	init();
$('#synonym_m_table').dg({
    pagination:false,
    height:cmTableH-20,
    data : dataArr,
	toolbar: '#tb',
	fitColumns:true,
	iconCls: 'icon-edit',
	singleSelect: true,
    columns:[[
        {field:'synonymOne',title:'同义词',width:'100%',align:'center',editor:'textbox'}
    ]],
    onLoadSuccess:function(data){
        $('.editcls').linkbutton({text:'abc',plain:true,iconCls:'icon-edit'});
    },
    onClickCell: onClickRow,

	onEndEdit: function onEndEdit(index, row) {
		var ed = $(this).datagrid('getEditor', {
			index: index,
			field: 'synonymOne'
		})
	}
	});

});	


/**
 * 标示当前编辑行，提高效率，避免使用each遍历所有行来关闭正在编辑的行。
 */
var editIndex = undefined;
//编辑行内内容
function endEditing(){
  if (editIndex == undefined){
	  return true
  }
  if ($('#synonym_m_table').datagrid('validateRow', editIndex)){
	  	$('#synonym_m_table').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
   } else {
	   return false;
   }

}

//双击触发编辑事件，开始更改此行内容
function onClickRow(index,value){
	if (editIndex != index){  
    	if (endEditing()){
            $('#synonym_m_table').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#synonym_m_table').datagrid('selectRow', editIndex);
        }
    }
}


/**
 * 增加一行
 * @returns
 */     
function append(){
	if (endEditing()){
		$('#synonym_m_table').datagrid('appendRow',{status:'P'});
		editIndex = $('#synonym_m_table').datagrid('getRows').length-1;
		$('#synonym_m_table').datagrid('selectRow', editIndex)
							 .datagrid('beginEdit', editIndex);
	}
}

/**
 * 删除一行
 * @returns
 */
function removeit(){
	if (editIndex == undefined){
		return
	}
    $('#synonym_m_table').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
	editIndex = undefined; 

}

/**
 * 判断字符串类型的值是否为空
 * @param array
 * @returns
 */
function isNull(array){
	return (array == null || array == ""||trimStr(array)=="");
}
/**
 * 去字符串空格
 * @param str
 * @returns
 */
function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 提交方法
 * @returns
 */
function accept(){
	var word = $('#word').val();
	if(isNull(word)){		
		$.messager.alert('警示信息', '词条不能为空','warning');
		return false;
	}
	if (endEditing()){
		var rows = $('#synonym_m_table').datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('警示信息', '同义词不能为空','warning');
			return false;
		}		
		var arr=[];
		for ( var i = 0; i < rows.length; i++) {
			arr[i] = rows[i]['synonymOne'];
//			var flag_isNull = isBlank(rows[i].synonymOne)
			var flag_isNull = isNull(rows[i].synonymOne)
			if(flag_isNull){
				$.messager.alert('警示信息', '同义词不能为空','warning');
				return false;
			}
		}

		
		var flag = isRepeat(arr);
		if(flag){
			$.messager.alert('警示信息', '同义词有重复','error');
			return false;
		}
		if ($('#synonym_m_table').datagrid('getRows').length) {
//			var rows = $('#synonym_m_table').datagrid('getRows');
			var synonymWord = '';
			for(var i = 0 ;i<rows.length;i++){
				if(rows[i].synonymOne==null ||rows[i].synonymOne==''){
					$.messager.alert('警示信息', '同义词不能为空','warning');
					return false;
				}
				synonymWord +=rows[i].synonymOne+',';
			}
			//把同义词连成以,连接的串
			synonymWord = synonymWord==''? synonymWord :synonymWord.substr(0,synonymWord.length-1);
			$('#synonymWord').val(synonymWord);
        }
		if(synonymWord.replace(/[^x00-xff]/g, "01").length>=1024){
			$.messager.alert('警示信息', '同义词信息已经超上限','warning');
		}

		saveInfor();
	}
}
function saveInfor() {
	if($('#synonym_detail_form').form("validate")){
		var data = $('#synonym_detail_form').getFormData();
		data.createAt = new Date(data.createAt.replace(/\-/g,'/'));
		data.updateAt = new Date(data.updateAt.replace(/\-/g,'/'));
/*		alert(JSON.stringify(data));*/
//		console.log(data);
		$.httpPost("/synonym/detail/insert",data, true, function(resp) {
			$('#id').val(resp.id);
//			console.log(resp);
		});
	}
	else{
		$.messager.alert('提示信息', "表单验证失败！",'error');
	}
}

/**
 * 取消操作
 * @returns
 */
function reject(){
		$('#synonym_m_table').datagrid('rejectChanges');
		editIndex = undefined;
}
/**
 * 获取表格修改的内容
 * @returns
 */
function getChanges(){
		var rows = $('#synonym_m_table').datagrid('getChanges');
		alert(rows.length+' rows are changed!');
}
/**
 * 结束编辑
 * @returns
 */
function endEdit(){
    var rows = $('#synonym_m_table').datagrid('getRows');
    for ( var i = 0; i < rows.length; i++) {
    	$('#synonym_m_table').datagrid('endEdit', i);
    }
}


/**
 * 判断数组中是否存在重复数据
 * 有的话返回true,反之返回false
 * @param arr 要判断的数组
 * @returns true/false 
 */
function isRepeat(arr){ 
	var hash = {}; 
	for(var i in arr) { 
		if(hash[arr[i]]){
			return true; 
		}
		hash[arr[i]] = true; 
	} 
	return false; 
}