
/**
 * 提交数据
 */

function doSave(){
	if ($('#mask').form("validate")) {
		var data = $('#mask').getFormData();
		data.createAt = new Date(data.createAt.replace(/\-/g,'/'));
		data.updateAt = new Date(data.updateAt.replace(/\-/g,'/'));
		$.httpPost("/mask/save",data,true,function(resp){
		$("#id").val(resp.id);	
		});
	}else{
		$.messager.alert('提示信息','表单验证失败',"error");
	}
}
