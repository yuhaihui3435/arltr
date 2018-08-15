var isRoleListChanged = false;
$('#role_table').datagrid({
			url : "/user/menu/authorization/role",
			method : 'POST',
			singleSelect : true,
			width:cmTreeWidth,
			rownumbers : false,
			remoteSort : false,
			pagination : false,
			columns : [ [   {field : 'name',title : '角色列表',width : '100%'}, 
							{field : 'id',title : 'id',hidden : true} 
						] ],
			onLoadSuccess: function(data){
				
			},
			onBeforeSelect : function(node) {
				
				if (isRoleListChanged) {
					if (!confirm('当前页面角色信息未保存，确定不保存吗？若希望保存请点击取消按钮后保存。')) {
						
						return false;
					}
				}
			},
			onSelect : function() {
				var row = $('#role_table').datagrid('getSelected');
				getRoleMenus(row.id);
			}
		});

/**
 * 页面初始化
 */
$(function(){
	$('.submit').hide();
});

/**
 * 加载菜单树
 */
function getRoleMenus(id)
{
    $('#menutree').tree({
        url:addTimestamp('/user/menu/authorization/'+id+'/menus/tree'),
        method : 'GET',
        checkbox:true,
        onCheck: function(node, checked) {
			isRoleListChanged = true;
		},
        onLoadSuccess:function(node,data){
        	//alert(JSON.stringify(data))
        	var node = $('#menutree').tree('getChecked',['checked','indeterminate']);
        	$.each(node, function(index, value) {
        		$('#menutree').tree('expand', value.target);
        	})
        },
        onBeforeExpand:function(node){

        }
    });
    isRoleListChanged = false;
    $('.submit').show();
}

/**
 * 节点定位并展开父节点
 * @returns
 */
function expandParentTree(selectnodeid){
	var tempnode;
	var rootNode = $("#menutree").tree("find", selectnodeid); //获取当前节点的父节点
	tempnode=$("#menutree").tree("getParent", rootNode.target);
	while(tempnode!=null){
		$('#menutree').tree('expand', tempnode.target);
		tempnode=$("#menutree").tree("getParent", tempnode.target);
	}
	$('#menutree').tree('select', rootNode.target);
}


/**
 * 保存菜单角色列表
 * 
 */
function saveMenuRoles() {
	var menuChecked = $('#menutree').tree('getChecked',['checked','indeterminate']);	
	
	var menus = [];
	
	var role = $('#role_table').datagrid('getSelected');
	
	$.each(menuChecked, function(index, item){
		var menu = {
				id : item.id
		};
		
		menus.push(menu);
	}); 
	
	$.httpPost('/user/menu/authorization/' + role.id + '/roles/save',menus, true, function(resp) {
	});
	
	isRoleListChanged = false;
}
