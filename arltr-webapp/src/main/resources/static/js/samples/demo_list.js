$('#demo_table').datagrid({
	title:'查询结果',
	toolbar:'#tb',
	width:"100%",
	height:650,
	checkOnSelect:false,
	selectOnCheck:false,
	pagination : true,
	pagePosition : "bottom",
	rowStyler: function(index,row){
		if (row.listprice>55.5){
			return 'background-color:#6293BB;color:#fff;'; // return inline style
		}
	},
	rownumbers : "true",
	frozen:true,
	url:'datagrid_data1.json',
	columns:[[
		{field:'ck',checkbox:true},
		{field:'productid',title:'法人代表',width:"5%",align:'center',sortable : true},
		{field:'productname',title:'企业实际经营名称',width:"33%",align:'center',sortable : true},
		{field:'unitcost',title:'企业性质',width:"5%",align:'center',sortable : true},
		{field:'status',title:'开业日期',width:"5%",align:'center',sortable : true},
		{field:'listprice',title:'经营状态',width:"5%",align:'center',sortable : true},
		{field:'attr1',title:'经营地址',width:"10%",align:'center',sortable : true},
		{field:'itemid',title:'联系电话',width:"5%",align:'center',sortable : true},
		{field:'aaa',title:'面积',width:"9%",align:'center',sortable : true},
		{field:'bbb',title:'所属警务区',width:"20%",align:'center',sortable : true},
		{field:'ddd',title:'设置',width:"9%",align:'center',	formatter : function(value, rec) {
			var btn = '<a class="editcls" href="/illegaldocklist/query/'+rec.productid+'">查看详细</a>';
			return btn;
		}}
	]]
});