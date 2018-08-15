
/**
 * 新建或修改词汇信息提交方法
 */
function submitLexicon() {
	if($('#lexicon_detail_form').form("validate")){
		var data = $('#lexicon_detail_form').getFormData();
		data.createAt = new Date(data.createAt.replace(/\-/g,'/'));
		data.updateAt = new Date(data.updateAt.replace(/\-/g,'/'));
/*		alert(JSON.stringify(data));*/
		$.httpPost("/lexicon/save",data, true, function(resp) {
			$('#id').val(resp.id);
		});
	}
	else{
		$.messager.alert('提示信息', "表单验证失败！",'error');
	}
}