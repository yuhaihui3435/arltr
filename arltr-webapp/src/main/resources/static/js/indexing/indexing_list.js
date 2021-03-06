
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/**
 * 提交数据
 */
function submitData() {
	var opts = $('#indexing_m_table').datagrid('options');
	var queryParameter = opts.queryParams;
	
	/** 绑定查询字段 */
//	queryParameter.startTime = $("#startTime").val();
	if($("#startTime").val()==''){	
		queryParameter.startTime = new Date('1900-01-01'.replace(/\-/g,'/'));
	}else{
		queryParameter.startTime = new Date($("#startTime").val().replace(/\-/g,'/'));
	}	
	
	queryParameter.taskType = $("#taskType").val() == "" ? 0 : $("#taskType").val();
	queryParameter.importType = $("#importType").val() == "" ? 0 : $("#importType").val();
	queryParameter.taskState = $("#taskState").val() == "" ? 0 : $("#taskState").val();

	$('#indexing_m_table').datagrid("reload");
}

$(function() {
	$('#indexing_m_table').dg({
		remoteSort:true,
		toolbar:'#tb',
		fitColumns:true,
		url: "/indexing/list/data",
		singleSelect : true,
	    columns:[[
//	        {field:'ck',checkbox:true},
	        {field:'startTime',title:'开始时间',width:"15%",sortable:false,formatter:getDateFormatter},
	        {field:'endTime',title:'结束时间',width:"15%",sortable:false,formatter:getDateFormatterCustom},
	        {field:'taskTypeName',title:'任务类型',width:"20%",sortable:false},
	        {field:'importTypeName',title:'采集类型',width:"20%",sortable:false,formatter:function (value,rec) {
					if(rec.importType=='1'){
						var index=rec.taskInfo.indexOf("|");
						if(index>0)
							return value+'('+rec.taskInfo.substring(0,index)+")";
					}
					return value;
                }},
	        {field:'taskStateName',title:'任务状态',width:"20%",sortable:false},
	        {field:'opt',title:'操作', width:"10%",align:'center',
	            formatter:function(value,rec){
	                var btn =
		                '<a class="editcls" href="/indexing/'+rec.id+'">详情</a>';
	                return btn;
	            }
	        }
	    ]],
	   // method:"post",
	    onLoadSuccess:function(data){
	        $('.editcls').linkbutton({text:'查看详细',plain:true,iconCls:'icon-edit'});
	        $("#taskType").combobox("loadData", data.taskTypeList);
	        $("#importType").combobox("loadData", data.importTypeList);
	        $("#taskState").combobox("loadData", data.taskStateList);
	    }
	});
	var isFirst=true;
	$('#tab').tabs({
        onSelect:function(title,index){
			if(index==1&&isFirst){
				isFirst=false;
                $('#indexing_pfq_table').dg({
                    remoteSort:true,
                    fitColumns:true,
                    url: "/indexing/pdm/fail/query",
                    singleSelect : true,
                    columns:[[
                        {field:'docId',title:'文档ID',width:"20%",sortable:false},
                        {field:'docTitle',title:'文档标题',width:"20%",sortable:false},
                        {field:'failStateTxt',title:'失败原因',width:"10%",sortable:false},
                        {field:'cAt',title:'创建时间',width:"15%",sortable:false,formatter:getDateFormatter},
                        {field:'uAt',title:'最后更新时间',width:"15%",sortable:true,formatter:getDateFormatter},
                        {field:'opt',title:'操作', width:"20%",align:'center',
                            formatter:function(value,rec){
                        		var failReason=rec.failReason;
                        		failReason=failReason.replace(/\'/g,"");
                        		failReason=failReason.replace(/\"/g,"");
                        		failReason=failReason.replace(/\</g,"").replace(/\>/g,"");
                                failReason=failReason.replace(/\;/g,"").replace(/\>/g,"");

                                $('#failReason').text(failReason);
                                var btn =
                                    '<a class="editcls" href="javascript:void(0)" onclick="viewPdmFailDetail()">错误详情</a>';
                                return btn;
                            }
                        }
                    ]],
                    // method:"post",
                    onLoadSuccess:function(data){
                        $('.editcls').linkbutton({text:'查看详细',plain:true,iconCls:'icon-edit'});
                    }
                });
			}

        }

    })





});

/**
 * 转时间戳函数(如果时间为null，显示空字符串)
 */

function getDateFormatterCustom(rowIndex, rowData) {
    var date = new Date(rowIndex);
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    return rowIndex == null ? "" : Y + M + D + h + m + s;
}


/**
 * 重置
 */
function onClear(){
	$('#indexingform').form('clear');  
}

/**
 * 设置下拉框
 */
$("#taskType").combobox({
	valueField : 'value',
	textField: 'valueName',
	editable:false,
	panelHeight:'auto'
});
$("#importType").combobox({
	valueField : 'value',
	textField: 'valueName',
	editable:false,
	panelHeight:'auto'
});
$("#taskState").combobox({
	valueField : 'value',
	textField: 'valueName',
	panelHeight:'auto',
	editable:false
});

/******************设置定时器******************/

/**
 * 定时器弹出框
 */
$("#cron_task_dialog").dialog({
	title: "设置增量定时器",
	width:'800px',
	height:'300px',
	modal: true,
	closed: true
/*	onClose : function() {

    }*/
})

/**
 * 设置下拉框高度
 */
$("#frequency").combobox({
	panelHeight:'auto'
});

$("#importTypeTimer").combobox({
	panelHeight:'auto'
});

/**
 * 显示定时器页面
 */
function setTimer(){
	$('#cron_task_dialog').dialog('open');
	$.httpGet("/indexing/timer", function(resp) {
		$("#id").val(resp.id);
		//alert(JSON.stringify(resp));
		$("#startTime_real").val(resp.startTime);
		var startTime_show = "";
		if(resp.startTime != null && "" != resp.startTime){
			var startTime_real = new Date(resp.startTime);
			var minutes = startTime_real.getMinutes()+"";
			if(minutes.length == 1){
				minutes = "0"+minutes;
			}
			startTime_show = startTime_real.getHours()+":"+minutes;
		}
		//alert("startTime_show="+startTime_show);
    	$('#startTimeTimer').timespinner('setValue', startTime_show);
    	$("#createBy").val(resp.createBy);
    	$("#createByName").val(resp.createByName);
    	$("#createByShow").html(resp.createByName);
    	var createAt = resp.createAt;
    	$("#createAtShow").html(getDateFormatter(createAt,null));
    	$("#createAt").val(createAt);
    	$("#updateBy").val(resp.updateBy);
    	$("#updateByName").val(resp.updateByName);
    	$("#updateByShow").html(resp.updateByName);
    	var updateAt = resp.updateAt;
    	$("#updateAtShow").html(getDateFormatter(updateAt,null));
    	$("#updateAt").val(updateAt);
    	
    	var frequencyList = [];
    	frequencyList.push({text : "每天",value : "1"});
    	frequencyList.push({text : "每周",value : "2"});
    	frequencyList.push({text : "半个月",value : "3"});
    	$(frequencyList).each(function(i,frequency){
			if(frequency.value == resp.frequency){
				$("#frequency").textbox('setValue',frequency.value);
				$("#frequency").textbox('setText',frequency.text);
			}
		});
    	
    	var importTypeList = [];
    	importTypeList.push({text : "全量",value : "1"});
    	importTypeList.push({text : "增量",value : "2"});
    	$(importTypeList).each(function(i,importType){
			if(importType.value == resp.importType){
				$("#importTypeTimer").textbox('setValue',importType.value);
				$("#importTypeTimer").textbox('setText',importType.text);
			}
		});
    	
    	$("#importTypeValue").val(resp.importType);
    	$("#frequency").combobox("loadData", frequencyList);
    	//getImportTypeList();
    	$("#importTypeTimer").combobox("loadData", importTypeList);
	});
	
}

/**
 * 单独加载采集类型的下拉列表(如此写加载速度会有延迟，暂定手写)
 * @returns
 */
function getImportTypeList(){  
	$.httpGet("/indexing/importtype/list", function(resp) {
		$("#importTypeTimer").combobox("loadData", resp);
	});
	
	$("#importTypeTimer").combobox({
		valueField : 'value',
		textField: 'valueName',
		onLoadSuccess : function() {
			if($("#importTypeValue").val() != null && $("#importTypeValue").val() != ""){
				$("#importTypeTimer").combobox("setValue", $("#importTypeValue").val());
			}
		},
		panelHeight:'auto'
	});
}

/**
 * 保存定时任务
 * @returns
 */
function saveCronTask(){
	if($('#cron_task_form').form("validate")){
		var data = $('#cron_task_form').getFormData();
		var arr = $('#startTimeTimer').timespinner('getValue').split(":");
		var startTime = new Date();
		startTime.setHours(arr[0]);
		startTime.setMinutes(arr[1]);
		//alert("startTime="+startTime);
		data.startTime = startTime;
//		data.createAt = new Date(data.createAt.replace(/\-/g,'/'));
//		data.updateAt = new Date(data.updateAt.replace(/\-/g,'/'));
		//alert(JSON.stringify(data));
		$.httpPost("/indexing/timer/save",data, true, function(resp) {
			$("#id").val(resp.id);
			$('#cron_task_dialog').dialog('close');
		});
	}else{
		$.messager.alert('提示信息', "表单验证失败！",'error');
	}
}

/******************手动更新******************/

/**
 * 手动更新弹出框
 */
$("#manual_update_dialog").dialog({
	title: "手动更新",
	width:'500px',
	height:'200px',
	modal: true,
	closed: true
})

/**
 * 加载采集类型下拉列表
 */
$(function(){
	$("#importTypeManual").combobox({
		valueField : 'value',
		textField: 'valueName',
		editable:false,
		panelHeight:'auto'
	});
	
	$.httpGet("/indexing/importtype/list", function(resp) {
		$("#importTypeManual").combobox("loadData", resp);
	});
})

/**
 * 显示手动更新页面
 * @returns
 */
function setImportType(){
	$('#manual_update_dialog').dialog('open');
}

/**
 * 手动更新确定方法
 * @returns
 */
function confirm(){
	var importType = $('#importTypeManual').combobox('getValue');
	if(importType == null || "" == importType){
		return;
	}
	var importTypeInt = parseInt(importType);
	$.httpPost("/indexing/manualupdate/confirm",importTypeInt, true, function(resp) {
		$('#manual_update_dialog').dialog('close');
		$('#indexing_m_table').datagrid("reload");
	});
}


function pdmFailQueryClear(){
    $('#pdmFailQueryForm').form('clear');
    $('#pfqStartTime').val("");
	$('#pfqEndTime').val("");
}

function pdmFailQuerySubmit() {
    var opts = $('#indexing_pfq_table').datagrid('options');
    var queryParameter=new Object();
    /** 绑定查询字段 */
    if($("#pfqStartTime").val()!=''){
        queryParameter.startTime = new Date($("#pfqStartTime").val().replace(/\-/g,'/'));
    }

    /** 绑定查询字段 */
    if($("#pfqEndTime").val()!=''){
        queryParameter.endTime = new Date($("#pfqEndTime").val().replace(/\-/g,'/'));
    }

    queryParameter.docId = $("#docId").val();
    queryParameter.docTitle = $("#docTitle").val() ;
    queryParameter.failState = $("#failState").val();
    opts.queryParams=queryParameter;
    $('#indexing_pfq_table').datagrid("reload");
}

function viewPdmFailDetail() {
    $('#w').window('open')
}

$("#fullAmount_task_dialog").dialog({
    title: "设置全量更新定时器",
    width:'800px',
    height:'300px',
    modal: true,
    closed: true
    /*	onClose : function() {

        }*/
})


function setFullAmountTimer() {
    $('#fullAmount_task_dialog').dialog('open');
    $("#sDate").datebox('setValue','')
    $("#eDate").datebox('setValue','')
    $("#taskDate").datebox('setValue','')
    $('#taskTime').timespinner('setValue','');
}

/**
 * 保存定时任务
 * @returns
 */
function saveFullAmountCronTask(){

        var sDate=$("#sDate").val()
		var eDate=$("#eDate").val()
		var taskDate=$("#taskDate").val()
        var taskTime = $('#taskTime').timespinner('getValue');

		var curTime = new Date().Format('yyyy-MM-dd');
		//把字符串格式转化为日期类
		// var sd = new Date(Date.parse(sDate));
		// var ed = new Date(Date.parse(eDate));
		//var td =new Date(Date.parse(taskDate));

		if(sDate==''){
            $.messager.alert('提示信息', '采集开始日期必填','error');
            return;
		}
		if(eDate==''){
			$.messager.alert('提示信息', '采集结束日期必填','error');
			return;
		}
		if(taskDate==''){
			$.messager.alert('提示信息', '任务开始日期必填','error');
			return;
		}
		if(taskTime==''){
			$.messager.alert('提示信息', '任务开始时间必填','error');
			return;
		}



		if(sDate>eDate){
            $.messager.alert('提示信息', '采集开始日期必须小于结束日期','error');
            return;
		}

		if(eDate>curTime){
            $.messager.alert('提示信息', '采集结束日期不能大于当日','error');
            return;
		}

		if(taskDate<curTime){
            $.messager.alert('提示信息', '任务开始日期不能小于当日','error');
            return;
		}

		var taskDateTime=taskDate+" "+taskTime
		taskDateTime=new Date(Date.parse(taskDateTime))
		if(taskDateTime<=new Date()){
            $.messager.alert('提示信息', '任务开始时间必须大于当前时间','error');
            return;
		}

        var param=new Object();
        param.sDate=sDate;
        param.eDate=eDate;
        param.taskDate=taskDate;
        param.taskTime=taskTime;

        $.httpPost("/indexing/timer/saveFullAmount",param, true, function(resp) {
            $('#fullAmount_task_dialog').dialog('close');
        });

}