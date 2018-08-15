var keyword=[];
var count=[];
var time=[];
var detailCount=[];
var keywordDetail=[];
var optionLegendName;
$(document).ready(function(){
	onSearch('7days');
	var cmEchartH = $(window).height()-220;
	$(".cm-height500").height(cmEchartH);
})

function setChart(){
// 基于准备好的dom，初始化echarts图表
var myChart1 = echarts.init(document.getElementById('d_h_chart1'));
var myChart2 = echarts.init(document.getElementById('d_h_chart2'));

option1 = {	
	color: ['#3398DB'],
    title: {
        /*text: '关键词搜索热度',*/
        subtext: '前10名',
        subtextStyle:{
        	fontFamily:'yahei, Verdana, sans-serif',
        	fontStyle:'normal',
        	fontWeight:'normal',
        },
        textStyle:{
        	fontFamily:'yahei, Verdana, sans-serif',
        	fontStyle:'normal',
        	fontWeight:'normal',
        	fontSize:14
        }
    },
    tooltip: {
        /*trigger: 'axis',
        axisPointer: {
            type: 'cross'
        }*/
    },
    legend: {
    	top:20,
    	left:'40%',
        data: ['关键词搜素热度（搜索次）']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
    	type: 'value',
        splitLine: {
            show: false
        },
        boundaryGap: [0, 0.01]
    },
    yAxis: {
        axisLabel: {
            interval: 0,
            rotate: 30
        },
        type: 'category',
        data:keyword
    },
    series: [
        {
            type: 'bar',
            stack: 'chart',
            z: 3,
            barWidth : 10,
            label: {
                normal: {
                    position: 'right',
                    show: true
                }
            },
            name: '关键词搜素热度（搜索次）',
            data:count
        }
    ]
};

option2 = {
    color: ['#3398DB'],
    tooltip : {
        /*trigger: 'axis',*/
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
    	left:'34%',
    	data: [optionLegendName]
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
        	type: 'category',
            show: false,
            data: keywordDetail
        },
        {
            position:'bottom',
            type : 'category',
            data :time
        }
    ],
    yAxis : [
        {
        	splitLine: {
                show: false
            },
            type : 'value'
        }
    ],
    series : [
        {
            name:optionLegendName,
            type:'bar',
            barWidth: 30,
            data:detailCount
        }
    ]
};
//为echarts对象加载数据
myChart1.setOption(option1);
myChart2.setOption(option2);

}

function onSearch(type){
	$.httpGet('/statistics/hot/data/' + type ,function(resp) {
		$.each(resp.Top.keyword, function(index, item){
			keyword.push(item);
		})
		$.each(resp.Top.count, function(index, item){
			count.push(item);
		})
		$.each(resp.detail.time, function(index, item){
			time.push(item);
		})
		$.each(resp.detail.keywordDetail, function(index, item){
			keywordDetail.push(item);
		})
		$.each(resp.detail.count, function(index, item){
			detailCount.push(item);
		})
		switch(type)
			{
			case '7days':
				optionLegendName='单日搜索最高';
			  break;
			case '1month':
				optionLegendName='单周搜索最高';
			  break;
			case '3months':
				optionLegendName='单月搜索最高';
			break;
			case '6months':
				optionLegendName='单月搜索最高';
			break;
		};
		setChart();
		keyword=[];
		count=[];
		time=[];
		detailCount=[];
		keywordDetail=[];
	});
}

$('.easyui-linkbutton').bind('click', function(e){
	$(".easyui-linkbutton").removeClass('c6')
	$(this).addClass('easyui-linkbutton c6');
});



