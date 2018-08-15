var orgdata;
var sysauthdata;
var sysauthid='';
/**
 * 页面初始化
 */
$(function(){
	//if(sysauthid==''){
		 $('#saveB').linkbutton('disable');		 		 
	//}
//		 $.httpPost("/user/sysauth/sysauthall?id="+1,null, false, function(result) {
//			 alert(result);
//		 })	 
//		 $('#sysauth').dg({
//				url : "/user/sysauth/sysauthlist",
//				singleSelect : true,
//				rownumbers : false,
//				pagination : false,
//				columns : [ [   
//				{field : 'name',title : '访问权限',width : '20%',sortable : true	},
//				{field : 'id',title : 'id',hidden : true} 
//						] ],
//
//				onSelect : function(node) {
//					sysauthid=sysauthdata[node].id;
//					insertarea(sysauthdata[node].id);
//					$('#saveB').linkbutton('enable');
//				},
//				 onLoadSuccess : function(data) {
//					 sysauthdata=data.rows
//				    }
//			});	 
		 
});

/**
 * 加载菜单树
 */
//function initTreeData(){
//	$("#sysauthtree").tree({
//		url:'/user/sysauth/sysauthlist',
//		method:'get',
//		onLoadSuccess:function(node,data){
//			//alert(JSON.stringify(node));
//        },
//        onSelect:function(node){
//        	// $(this).tree('expand',node.target);
//        	 alert(node.id);
//        	 insertarea(node.id);
//        	
//        }
//	});
//}

$('#sysauth').datagrid({
	url : "/user/sysauth/sysauthlist",
	method : 'get',
	singleSelect : true,
	width:cmTreeWidth,
	rownumbers : false,
	pagination : false,
	columns : [ [   
	{field : 'name',title : '访问权限',width :'100%'},
	{field : 'id',title : 'id',hidden : true} 
			] ],

	onSelect : function(node) {
		sysauthid=sysauthdata[node].id;
		insertarea(sysauthdata[node].id);
		$('#saveB').linkbutton('enable');
	},
	 onLoadSuccess : function(data) {
		 sysauthdata=data.rows
	    }
});
/**
 * 加载机构树
 */
$("#orgtree").tree({
	url:'/user/role/orgusertree',
	method:'post',
	checkbox:true
//	onLoadSuccess:function(node,data){
//	orgdata=data;
//    },
});
/**
 * 前台显示 有权限的人
 * @param id
 * @returns
 */
function insertarea(id){
	var checkedItems = $('#orgtree').tree('getChecked');
	for(var j=0;j<checkedItems.length;j++){
			var node=$('#orgtree').tree('find', checkedItems[j].id);			
			$('#orgtree').tree('uncheck',node.target);
		var pnode=$('#orgtree').tree('find', checkedItems[j].pid);
		if(pnode!=null){
		 $('#orgtree').tree('collapseAll', pnode.target);
		}
	}
	
	
	 var node='';
	var data;
	$.httpPost("/user/sysauth/usersysauth?id="+id,null, false, function(result) {
      data=result;
  	for(var i=0;i<data.length;i++){		
		//node=$('#orgtree').tree('find', data[i].id);		
		node=$('#orgtree').tree('find', data[i].employeeOrg);
		orgdata=$('#orgtree').tree('getChildren',node.target);
		for(var j=0;j<orgdata.length;j++){
			if(data[i].id==orgdata[j].id){
				node=$('#orgtree').tree('find', orgdata[j].id);
				$('#orgtree').tree('check',node.target);
			    $('#orgtree').tree('expandTo', node.target);	
			}
			
		}
	}
	});
		      
	//var data=orgdata;
	//checkedItems[2].attributes.userInfo.roles
//	for(var i=0;i<data.length;i++){
//		var user=data[i].attributes.userInfo.sysAuths;
//		for(var j=0;j<user.length;j++){
//			if(id==user[i].id){
//				node=$('#orgtree').tree('find', id);
//				$('#orgtree').tree('select',node.target);
//			    $('#orgtree').tree('expandTo', node.target);
//			    data[i].attributes.userInfo
//			}
//		}
//	}
           
}
/**
 * 访问权限保存方法
 * @returns
 */
function save(){
	var checkedItems = $('#orgtree').tree('getChecked');
	var useridlist=[]
	for(var i=0;i<checkedItems.length;i++){
		if(checkedItems[i].attributes!=null){
		useridlist.push(checkedItems[i].id)
	}
	}
	$.httpPost("/user/sysauth/save?useridlist="+useridlist+"&id="+sysauthid,null, true, function(result) {})
}

