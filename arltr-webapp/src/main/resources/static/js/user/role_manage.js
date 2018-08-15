var isNodeEditNotCommited = false;
var isMenuClicking = false;
/**
 * 页面初始化
 */
$(function(){
    initTreeData();
    $('#saveB').linkbutton('disable');

});

/**
 * 组织机构树
 */
$("#orgusertree").tree({
	url:'/user/role/orgusertree',
	method:'post',
	checkbox:true
});

/**
 * 加载角色树
 */
function initTreeData(){
	$("#roletree").tree({
		url:'/user/role/tree' ,
		onLoadSuccess : function(data) {
		},
        onContextMenu: function(e, node){
        	if(node.id=="-1"){
        		 e.preventDefault();
                 $('#roletree').tree('select', node.target);
                 $('#role_r_toggle').menu('show', {
                     left: e.pageX,
                     top: e.pageY
                 });
        	}
        	else{
        		isMenuClicking = true;
        		e.preventDefault();
        		$('#roletree').tree('select', node.target);
                $('#role_r_toggleU').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
        	}
        },
        onBeforeSelect : function(node) {
        	if (isNodeEditNotCommited) {
        		return false;
        	}
        },
        onSelect:function(node){
        	
        	if(node.id=="-1"||node.id=="0"){
        		$(this).tree('expand',node.target);
        	}
        	else{
//        	    alert("selectroleid="+roleid);
        		 insertRoleRelationUser(node.id);
        		 $('#saveB').linkbutton('enable');
        	}
        },
        onAfterEdit : function(node){

        	if (isMenuClicking) {
        		isNodeEditNotCommited = true;
        		return;
        	}
        	
        	isNodeEditNotCommited = true;
            var _tree = $(this);
            
            if(node.id == 0){
                // 新增节点
            	$.httpPost("/user/role/save",{name:node.text}, true, function(bodyvalue) {
            		$("#roletree").tree("update",{target : node.target,id : bodyvalue.id});
            		insertRoleRelationUser(bodyvalue.id);
           		    $('#saveB').linkbutton('enable');
           		 isNodeEditNotCommited = false;
        		}, function() {
        			    var node = $("#roletree").tree("getSelected");
            			$("#roletree").tree('beginEdit',node.target);
        		});
            }else{
            	//修改
            	$.httpPost("/user/role/save",{id:node.id,name:node.text}, true, function(resp) {
            		isNodeEditNotCommited = false;
            	}, function() {
            		var node = $("#roletree").tree("getSelected");
        			$("#roletree").tree('beginEdit',node.target);
        		});
            }
        }
	});
}

/**
 * 新增角色
 */
function append(){
	insertRoleRelationUser(0);
	 $('#saveB').linkbutton('disable');
	var node = $('#roletree').tree('getSelected');
	if (node){
		var nodes = [{
			"id" :0,
			"text":"请输入角色名称"
		}];
		$('#roletree').tree('append', {
			parent:node.target,
			data:nodes
		});
		var _node = $("#roletree").tree('find',0);
		$("#roletree").tree("select",_node.target).tree('beginEdit',_node.target);
	}	
}
/**
 * 删除角色
 */
function remove(){
	
	var node = $("#roletree").tree("getSelected");
	$.messager.confirm('删除确认', "确定删除当前角色吗？",function(ok){
		if(ok){
			if(node.id==0){
				$("#roletree").tree("remove",node.target);
				isNodeEditNotCommited = false;
				isMenuClicking = false;
			}else{
			$.httpPost("/user/role/remove/"+node.id, null, true, function(resp) {
				$("#roletree").tree("remove",node.target);
				isNodeEditNotCommited = false;
				isMenuClicking = false;
			});
		}
		}
	})
}


/**
 * 修改角色
 */
function update(){
	var node = $("#roletree").tree("getSelected");
	$("#roletree").tree('beginEdit',node.target);
	isMenuClicking = false;
}

/**
 * 前台显示 有角色的人
 * @param id
 * @returns
 */
function insertRoleRelationUser(id){
	var checkedItems = $('#orgusertree').tree('getChecked');
	for(var j=0;j<checkedItems.length;j++){
			var node=$('#orgusertree').tree('find', checkedItems[j].id);			
			$('#orgusertree').tree('uncheck',node.target);
		var pnode=$('#orgusertree').tree('find', checkedItems[j].pid);
		if(pnode!=null){
		 $('#orgusertree').tree('collapseAll', pnode.target);
		}
	}
	var node='';
	var data;
	$.httpPost("/user/role/rolerelauser?id="+id,null, false, function(result) {
      data=result;
  	for(var i=0;i<data.length;i++){			
		node=$('#orgusertree').tree('find', data[i].employeeOrg);
		orgdata=$('#orgusertree').tree('getChildren',node.target);
		for(var j=0;j<orgdata.length;j++){
			if(data[i].id==orgdata[j].id){
				node=$('#orgusertree').tree('find', orgdata[j].id);
				$('#orgusertree').tree('check',node.target);
			    $('#orgusertree').tree('expandTo', node.target);	
			}
			
		}
	}
	});
           
}
/**
 * 角色配置人员保存方法
 * @returns
 */
function saveRoleUser(){
	 var node =$('#roletree').tree('getSelected');
	 var roleid=node.id;
	var checkedItems = $('#orgusertree').tree('getChecked');
	var useridlist=[]
	for(var i=0;i<checkedItems.length;i++){
		if(checkedItems[i].attributes!=null){
		useridlist.push(checkedItems[i].id)
	}
	}
	$.httpPost("/user/role/roleusersave?useridlist="+useridlist+"&id="+roleid,null, true, function(result) {})
}

function menuHandler(item){
    var tree = $("#roletree");
    var node = tree.tree("getSelected");
    if(item.name === "add"){
        tree.tree('append', {
            parent: (node?node.target:null),
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
        var _node = tree.tree('find',0);
        tree.tree("select",_node.target).tree('beginEdit',_node.target);
    }else if(item.name === "rename"){
        tree.tree('beginEdit',node.target);
    }else if(item.name === "delete"){
        $.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
            if(r){
                $.post("/content/category/delete/",{parentId:node.parentId,id:node.id},function(){
                    tree.tree("remove",node.target);
                }); 
            }
        });
    }
}