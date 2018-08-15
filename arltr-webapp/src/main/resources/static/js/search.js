var queryHeld = "";
var userQueryHistory = [];

/**
 * 初始化
 */
function init(){

	queryList();
	var resultInput_w=$("#sMark1").width()-110;
	$(".search-input-down").width(resultInput_w);
	$('.dropdown-toggle').dropdown();
	
	searchBoxScrollTop();
	
	setSuggestion();
	
	getUserHistory();
	
	getHotTop();
}

/**
 * 获取用户查询记录
 */
function getUserHistory() {
	$.ajax({
		type:'GET',
		url:'/statistics/querylisthistory?timestamp=' + Date.parse(new Date()),
		data: "userId=" + $("#userId").val(),
		success: function(resp){
			if (resp.code == 1000) {
				userQueryHistory = resp.body;
				
				if (userQueryHistory.length > 0) {
					userQueryHistory.sort(function (a, b) {
						return b[0] - a[0];
					});
					
					$.each(userQueryHistory, function(i, qh){
						
						if (i > 4) {
							return false;
						}
						
                		$("#userHistory").append(
                				"<a href='/search?query=" + encodeURI(qh[1]) + "' class='list-group-item'>" +
        						"<span class='badge'>" + qh[0]  + "</span>" + qh[1] + "</a>");
                	});
					
					$("#userHistory").removeClass("hidden");
				}
			}
		}
	});
}

/**
 * 获取热词
 */
function getHotTop() {
	$.ajax({
		type:'GET',
		url:'/statistics/queryhistorytopfive?timestamp=' + Date.parse(new Date()),
		success: function(resp){
			if (resp.code == 1000) {
				var hotTop = resp.body;
				
				for (var key in hotTop) {
					$("#hotTop").append(
            				"<a href='/search?query=" + encodeURI(key) + "' class='list-group-item'>" +
            						"<span class='badge'>" + hotTop[key]  + "</span>" + key + "</a>");
				}
				
				$("#hotTop").removeClass("hidden");
			}
		}
	});
}

/**
 * 输入提示设置
 */
function setSuggestion() {
	var time;
	
	$("#query").focus(function () {
		time = setInterval(suggestion, 500);  
    });

    $("#query").blur(function(){
    	clearInterval(time);
	});
    
    // 焦点离开输入框及提示框时的处理
    $(document).click(function(e){
		e = window.event || e;
		var obj = e.srcElement || e.target;
		if(!$(obj).is("#suContent *, #query")) {
			$("#suContent").hide();
			queryHeld = "";
		}
	});
    
    // 输入提示的上下移动以及回车处理
    $(document).keydown(function(event){

		if ($("#suContent").is(":visible") == true) {
			if (event.keyCode == 13) {
				// 回车
				var nowItem = $("#suList").children(".search-list-item-hover");
				
				if (nowItem.length > 0) {
//					$("#query").val(($(nowItem).text()));
//            		queryList();
//            		$("#suContent").hide();
            		window.location.href = "/search?query=" + encodeURI($(nowItem).text());
				}
			} else if (event.keyCode == 38) {
				// ↑
				var nowItem = $("#suList").children(".search-list-item-hover");
				
				if (nowItem.length > 0) {
					var nextItem = $(nowItem[0]).prev();
					
					if (nextItem.length > 0) {
						$(nowItem).removeClass("search-list-item-hover");
						$(nextItem).addClass("search-list-item-hover");
					}
				} else {
					$($("#suList").children()[$("#suList").children().length - 1]).addClass("search-list-item-hover");
				}
				
				return false;
			} else if (event.keyCode == 40) {
				// ↓
				$("#query").blur();
				
				var nowItem = $("#suList").children(".search-list-item-hover");
				
				if (nowItem.length > 0) {
					var nextItem = $(nowItem[0]).next();
					
					if (nextItem.length > 0) {
						$(nowItem).removeClass("search-list-item-hover");
						$(nextItem).addClass("search-list-item-hover");
					}
				} else {
					$($("#suList").children()[0]).addClass("search-list-item-hover");
				}
				
				return false;
			}
		}
    });  
}

/**
 * 顶部固定
 */
function searchBoxScrollTop() {
	
	$('.search-box-sf').scrollToFixed({
		dontSetWidth: true, 
		fixed: function() {
			$('.search-box-sf').attr('style', $('.search-box-sf').attr('style').replace('top: 0px;',''))
		}
	});
	
	$('.scrollToFixed-placeholder').scrollToFixed( {
		preFixed: function() { 
			
			$('.search-box-sf').addClass('search-box-border');
		},
		postFixed: function() {
			
			$('.search-box-sf').removeClass('search-box-border');
		}
    });
}

/**
 * 收起工具
 */
function clearTool() {
	$("#tools_bottom").hide();
	$("#tools_top").slideToggle();
}

/**
 * 清除搜索条件
 */
function clearCondition(){
	$("#tools_bottom").hide();
	$("#tools_top").slideToggle();
	$("#sort").attr("data-value", "1");
	$("#sort").text("按照相关性排序");
	$("#queryField").attr("data-value", "1");
	$("#queryField").text("搜索全部");
	$("#source").attr("data-value", "*");
	$("#source").text("全部来源");
	resetOptBtn();
	queryList();
}

/**
 * 条件查询工具
 */
function showToolsBottom(){
	$("#tools_top").slideUp(function(){
		$("#tools_bottom").removeClass("hidden");
		$("#tools_bottom").slideDown();
	});
}

/**
 * 查询
 */
function queryList() {

	$(".search-loadiing-body").show();
	$(".search-fenye").show();
	var urlValue = "";
	var data;

	var keyword = $('#query').val();
	keyword = $.trim(keyword);
	if (keyword != "") {
		
		data = "keyword=" + keyword + "&queryField=" + $("#queryField").attr("data-value") + "&orderby=" + $("#sort").attr("data-value") + "&source=" + $("#source").attr("data-value");
		
		$.ajax({
			type : 'POST',
			data : data,
			url : '/search/query',
			success : function(resp) {

				var msg = resp.body;
				
				if (msg.totalElements > 0) {
					$('#numspan').text("为您找到相关结果约" + msg.totalElements + "个");
				} else {
					$('#numspan').text("没有找到相关结果");
				}
				var highlightedList = msg.highlighted;
				var urlList = msg.content;
				
				creatTable(keyword, highlightedList, urlList);

				$(".search-loadiing-body").hide();
				
				var element = $('#bp-3-element-sm-test');
				if (highlightedList.length > 0) {
					options = {
						size : msg.size + "",// 每页大小
						bootstrapMajorVersion : 3,
						currentPage : msg.number + 1,// 當前頁
						numberOfPages : msg.numberOfElements,// 当前一共有多少条数据
						totalPages : msg.totalPages,// 总共有多少页
						onPageChanged : function(e, oldPage, newPage) {
							pageSearch(data, keyword, oldPage, newPage);
						}
					};
					element.bootstrapPaginator(options);
				} else {
					$(".search-fenye").hide();
				}

			}
		})
	}

}
/**
 * 翻页查询
 * 
 * @param value
 *            ajax需要的参数拼接
 * @param queryKey
 *            查询的关键字
 * @param oldPage
 * @param newPage
 */
function pageSearch(value,queryKey,oldPage, newPage){
	
	$(document).scrollTop(0);
	$(".search-loadiing-body").show();
	
	$.ajax({
		type:'POST',
		url:'/search/query',
		data: value+"&pageNum="+(newPage - 1)+"&pageSize=10",
		success: function(resp){
			
			var msg = resp.body;
			
			var highlightedList = msg.highlighted;
			var urlList = msg.content;
			
			creatTable(queryKey,highlightedList,urlList);
			$(".search-loadiing-body").hide();
		}
	});
}

/**
 * 查询结果显示
 */
function creatTable(queryKey,highlightedList,urlList){
	
	$(".tab-content").empty();
	
   $.each(highlightedList,function(i,n){
	   
	   //高亮显示的所有数据集合（包含title和content）
	   var temp =highlightedList[i];
	   
	   var highlightsMap = convertHighlightsToMap(temp.highlights)
	   
	   var id_temp = urlList[i].id;
	   
	   var row = $("#rowTemplate").clone();
	   
	   var titleLink = row.find(".title-link");
	   var viewUri = "/search/view?url=" + escape(temp.entity.url)
	   + "&wd=" + encodeURI(queryKey) + "&title=" + encodeURI(temp.entity.title)
	   + "&source=" + encodeURI(temp.entity.source + "&docId=" + encodeURI(temp.entity.id) + "&originalId=" + encodeURI(temp.entity.originalId));
	   titleLink.html(highlightsMap["title"] ? highlightsMap["title"] : temp.entity.title);
	   titleLink.attr("href", viewUri);
	   row.find(".content").html(highlightsMap["content"] ? highlightsMap["content"] : (temp.entity.content.length > 200 ? temp.entity.content.substring(0, 200) + " ..." : temp.entity.content));
	   row.find(".link-url").text(temp.entity.url);
	   row.find(".link-url").attr("href", viewUri);
	   row.find(".cache-url").attr("href", "/snapshoot?id="+id_temp+"&queryKey="+encodeURI(queryKey));
	   var classificationName = temp.entity.classificationName;
	   if (!classificationName || classificationName == "") {
		   classificationName = "普通";
	   }
	   row.find(".classification").text(classificationName);
	   row.find(".lastModified").text(temp.entity.lastModified);
	   row.find(".author").text(temp.entity.author);
	   row.removeClass("hidden");
	   $(".tab-content").append(row);
	   
   });
}

/**
 * 将高亮内容数组转换为map
 */
function convertHighlightsToMap(highlights) {
	
	var highlightsMap = {};
	
	$.each(highlights,function(i, 	node){
		highlightsMap[node.field.name] = node.snipplets;
	});
	
	return highlightsMap;
}

/**
 * 根据特定字段搜索
 */
function queryByField(_this) {
	
	$("#queryField").attr("data-value", $(_this).attr("data-value"));
	$("#queryField").text($(_this).text());
	
	resetOptBtn();
	queryList();
}

/**
 * 根据特定字段排序
 */
function orderBy(_this) {
	
	$("#sort").attr("data-value", $(_this).attr("data-value"));
	$("#sort").text($(_this).text());
	
	resetOptBtn();
	queryList();
}

/**
 * 根据来源搜索
 */
function queryBySource(_this) {
	
	$("#source").attr("data-value", $(_this).attr("data-value"));
	$("#source").text($(_this).text());
	
	resetOptBtn();
	queryList();
}

/**
 * 清除条件
 */
function resetOptBtn() {
	
	if ($("#queryField").attr("data-value") == "1" && $("#sort").attr("data-value") == "1" && $("#source").attr("data-value") == "*") {
		$("#toggleTools").show();
		$("#clearOpts").hide();
	} else {
		$("#toggleTools").hide();
		$("#clearOpts").show();
		$("#clearOpts").removeClass("hidden");
	}
	
}

/**
 * 输入提示
 */
function suggestion() {
	
	var queryNow = $.trim($("#query").val());
	
	if (queryNow != "" && queryNow != queryHeld) {
		
		queryHeld = queryNow;
		$("#suList").empty();
		var suggestMap = {};
		
		$.each(userQueryHistory,function(i, 	qh){

			if ($("#suList").children().length >= 3) {
				return false;
			}
			
			if (suggestMap[qh[1]]) {
				return true;
			}
			
			if (qh[1].indexOf(queryNow) >= 0  ||
				(qh[2] && qh[2].indexOf(queryNow) >= 0) ||
				(qh[3] && qh[3].indexOf(queryNow) >= 0)) {
				
				if (qh[1] != queryNow) {
					$("#suList").append("<li class='search-list-item'>" + qh[1] + "</li>");
					
					suggestMap[qh[1]] = 1;
				}
			}
    	});
		
		showSuggestion();
		
		$.ajax({
			type:'GET',
			url:'/search/suggestion',
			data: "&keyword=" + encodeURI(queryNow),
			success: function(resp){
				
				if (resp) {
	                if (1000 == resp.code) {
	                	
	                	var suggestionList = resp.body;
	                	
	                	var sugestContent = 
	                	$.each(suggestionList,function(i, 	suggest){
	                		
	                		if ($("#suList").children().length >= 5) {
	            				return false;
	            			}
	                		
	                		if (suggestMap[suggest] || suggest == queryNow) {
	            				return true;
	            			}
	                		
	                		$("#suList").append("<li class='search-list-item'>" + suggest + "</li>");
	                		suggestMap[suggest] = 1;
	                	});
	                	
	                	showSuggestion();
	                }
				}
			}
		});
    }
}

/**
 * 输入提示展示控制
 */
function showSuggestion() {
	
	$(".search-list-item").click(function() {
		window.location.href = "/search?query=" + encodeURI($(this).text());
	});
	
	$(".search-list-item").hover(
		function(){
			$("#suList").children(".search-list-item-hover").removeClass("search-list-item-hover");
			 $(this).addClass("search-list-item-hover");
		},
		function(){
			$(this).removeClass("search-list-item-hover");
		}
	);
	
	if ($("#suList").children().length > 0) {
		$("#suContent").show();
		$("#suContent").removeClass("hidden");
	}
}

function isEleInList(ele, list) {
	
	var retVal = false;
	
	$.each(list, function(i, 	item){
		
		if (item == ele) {
			retVal = true;
			return false;
		}
	});
	
	return retVal;
}
//===============退出按钮 由于没有引入easyui采用原始的提示框=====================
function loginOut_bt(){
	 var r=confirm("是否确定退出?");
	 if (r==true){
		 clearSession();
	 }
}
//退出后清除session信息
function clearSession(){
	$.ajax({
        type: "post",
        url: "/user/login/clearsesssion",
        success: function (data) {
        	if (data) {
                if (1000 == data.code) {
                	closeWebPage();
                }
            }
        }
    });
}
//兼容所有浏览器的关闭窗口的方法
function closeWebPage() {
//	$('#logoutForm').submit();
	if (navigator.userAgent.indexOf("MSIE") > 0) {//close IE  
		if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
			window.opener = null;
			window.close();
		} else {
			window.open('', '_top');
			window.top.close();
		}
	} else if (navigator.userAgent.indexOf("Firefox") > 0) {//close firefox  
		window.location.href = 'about:blank ';
	} else {//close chrome;It is effective when it is only one.  
		window.opener = null;
		window.open('', '_self');
		window.close();
	}
}  
//===============退出按钮 由于没有引入easyui采用原始的提示框=====================