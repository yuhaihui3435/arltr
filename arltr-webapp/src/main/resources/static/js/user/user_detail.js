/**
 * 保存用户
 */
function submitUser() {
		var user={};
		user.id=$("#id").val();
		user.securityClass=$("#securityClass").val();
		//var data = $('#user_detail_form').getFormData();
		//alert(JSON.stringify(data));
		$.httpPost("/user/save",user, true, function(resp) {
		});
	
}

$("#securityClass").combobox({
//	 valueField:'value',
//	 textField:'text',
//	 onLoadSuccess : function() {
//		 if($("#securityClassValue").val()==0){
//		 $("#securityClass").combobox("setValue", ''); 
//	 }
//	 else{
//		 $("#securityClass").combobox("setValue", $("#securityClassValue").val()); 
//	 }
//	    },
   panelHeight: 'auto'
});