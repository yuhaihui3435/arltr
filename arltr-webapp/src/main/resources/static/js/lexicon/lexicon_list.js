/**
 * 提交数据
 */
function submitData() {
	var opts = $('#lexicon_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	/** 绑定词汇名称 */
	queryParameter.word = $("#word").val();
	$('#lexicon_m_table').datagrid("reload");
}
/**
 * 列表详情
 */
$(function(){
	$('#lexicon_m_table').dg({
		url: "/lexicon/list/data",
		toolbar:'#tb',
		remoteSort:true,
	    columns:[[
	        {field:'ck',checkbox:true},
	        {field:'word',title:'词汇',width:"40%"},
	        {field:'createAt',title:'创建时间',width:"15%",sortable:true,formatter:getDateFormatter },
	        {field:'createByName',title:'创建者',width:"8%"},
	        {field:'updateAt',title:'更新时间',width:"15%",sortable:true,formatter:getDateFormatter },
	        {field:'updateByName',title:'更新者',width:"8%"},
	        {field:'opt',title:'操作', width:"12%",align:'center',
	            formatter:function(value,rec){
	                var btn =
	                    '<a class="editcls" href="/lexicon/'+rec.id+'">编辑</a>'+
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
 * 单条删除航空词汇信息
 */
function doDeleteOne(id){
	$.messager.confirm('删除确认', "确定删除当前记录吗？",function(ok){
				if(ok){
					$.httpPost("/lexicon/remove/"+id, null, true, function(resp) {
						$('#lexicon_m_table').datagrid('reload');
					});
				}
			})
}

/**
 * 批量删除词库信息
 */
function doDelete(){
	   var lexiconlist=[];
		var rows = $('#lexicon_m_table').datagrid('getSelections');
		$.each(rows, function(index, item){
			var lexicon = {
					id : item.id,
					word : item.word
			};
			lexiconlist.push(lexicon);
		});
		if (rows.length>0){
			$.messager.confirm('删除确认', "确定删除所选记录吗？",function(ok){
				if(ok){
					$.httpPost("/lexicon/remove/multiple",lexiconlist, false, function(resp) {
						
						setTimeout("$('#lexicon_m_table').datagrid('reload');",5000);
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
	$('#lexiconform').form('clear');  
}

function doInsert(){
//	$("#fileupload").click();
	showdialog();
}
var intId;
$(function() {
	$('#fileupload').fileupload({
        dataType: 'json',
        autoUpload:false,
        add: function (e, data) {
        console.log(data);
//            data.context = $('<button/>').text('Upload')
//                .appendTo(document.body)
//                .click(function () {
//                    data.context = $('<p/>').text('Uploading...').replaceAll($(this));
//                    data.submit();
//                });
        
	        var reg=new RegExp(".owl$");     
	        if (reg.test(data.files[0].name)) {
	        	data.context = $("#updbtn").click(function () {
	                //data.context = $('<p/>').text('Uploading...').replaceAll($(this));
	                data.submit();
	                $('#uploadDiv').hide();
	                $('#pgDiv').show();
	                $('#msg').html('文件上传中...');
	            });
	        } else {
	        	alert('请选择OWL文件');
	        }
        },
        done: function (e, data) {
//        	var fileName = data.files[0].name;
            alert('文件上传成功！');
        },
        fail: function(e, data) {
        	
        	$('#msg').html('文件解析中...');
			$('#p').progressbar({
			    value: 0
			});
			$('#p').show();
			intId = setInterval("refreshPg()", 1000);
			
//			setTimeout("$('#p').hide();$('#lexicon_m_table').datagrid('reload');alert('文件上传成功！');",5000);
			
        	
        }
    });

});

function refreshPg() {
	
	console.log('执行进度条刷新');
	var value = $('#p').progressbar('getValue');
	
	if (value >= 100){
		window.clearInterval(intId);
		$('#msg').html('解析完成');
		
		setTimeout("$('#divFile').dialog('close');document.location.reload();",1000);
	}
	value+=10;
	$('#p').progressbar('setValue', value);
}

function save(){
	/**$("#fileupload").fileupload({
		url : '/management/fileupload/test',
		sequentialUploads : true
	}).bind('fileuploadprogress', function(e, data) {
//		var progress = parseInt(data.loaded / data.total * 100, 10);
//		$("#weixin_progress").css('width', progress + '%');
//		$("#weixin_progress").html(progress + '%');
	}).bind('fileuploaddone', function(e, data) {alert();
//		$("#weixin_show").attr("src", "__PUBLIC__/" + data.result);
//		$("#weixin_upload").css({
//			display : "none"
//		});
//		$("#weixin_cancle").css({
//			display : ""
//		});
	});**/
	var filesList = $('input[type="file"]').prop('files');
	var jqXHR = $("#fileupload").fileupload('send',{files: filesList});
}

$("#divFile").dialog({
	title: "文件导入",
	width:'300px',
	height:'100px',
	 modal: true,
	 closed: true,
	 onClose : function() {
		 
	 }

})

function showdialog(){
	$('#uploadDiv').show();
    $('#pgDiv').hide();
    $('#p').hide();
    $('#fileupload').attr("value", "");
//    document.selection.clear();
	 $('#divFile').dialog('open');
}