/*自适应*/
//当浏览器大小变化时

$(document).ready(function() {
    var cmHeight = $(window).height();
    var cmMain = cmHeight - 100;
    var cmText = cmHeight - 143;
    var cmLeftC = cmHeight - 116;
    var cmTreeContent = cmHeight - 173;
    $(".cm-left-c").css("height", cmLeftC);
    $(".cm-content").css("height", cmMain);
    $(".cm-tree").css("height", cmText);
    $(".cm-tree-content").css("height", cmTreeContent);
    $(".cm-center").css("height", cmText);
    $(".cm-btn").addClass("cm-btn-sroll");
});
function cmResize(){
    var cmHeight = $(window).height();
    var cmMain = cmHeight - 100;
    var cmText = cmHeight - 143;
    var cmLeftC = cmHeight - 116;
    var cmTreeContent = cmHeight - 173;
    $(".cm-left-c").css("height", cmLeftC);
    $(".cm-content").css("height", cmMain);
    $(".cm-tree").css("height", cmText);
    $(".cm-tree-content").css("height", cmTreeContent);
    $(".cm-center").css("height", cmText);
};
//分页自适应高度值，各个页面调取cmTableH属性
var cmSearchR=$(".cm-search-tr").height()+160;
var cmTableH = $(window).height()-cmSearchR;
/*console.log("cmSearchR:"+cmSearchR);
console.log("cmTableH:"+cmTableH);*/
//取得权限管理cm-tree的宽度；
var cmWidth = $(window).width();
var cmTreeWidth=cmWidth*0.84*0.2;
$(window).resize(function (){
    cmResize();
});
//针对cm-center底部，操作按钮的fixed固定
$(".cm-center").scroll(function (){
    var cmHeight = $(window).height();
    var viewH = $(this).height();//可见高度
    var contentH = $(this).get(0).scrollHeight;//内容高度
    var scrollTop = $(this).scrollTop();//滚动高度
    var proportionValue=scrollTop/(contentH -viewH);
    if (proportionValue>0.97) { //到达底部100px时,加载新内容
        $(".cm-btn").removeClass("cm-btn-sroll");
        var cmMain = cmHeight - 135;
        $(".cm-content").height(cmMain);
        $(this).height(cmMain-28);
        $(".cm-demo-container").css('padding-bottom','20px');
    } else {
        $(".cm-btn").addClass("cm-btn-sroll");
    }
});


//左侧树形Menu
$(document).ready(function(){
	$("#firstpane h3.menu_head.current").next("div.menu_body").show();
    $("#firstpane h3.menu_head").click(function(){
    	$(this).addClass("current menu_body-check");
    	$("h3.menu_head").next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
        $("h3.menu_head").not(this).removeClass("current menu_body-check");
    });

});
/* 一级菜单，点击操作*/
// otherSrc定义图片全局变量

$(function(){
    //获得当前 menu li 第一个元素的宽度
    var nav_w=$(".find_nav_list li").first().width();
	var positionInit = $("li.find_nav_cur").position().left;
	$(".sideline").animate({left:positionInit},300);
    $(".sideline").animate({width:nav_w});
    $(".sideline").css('display','block');
    $(".find_nav_list li").on('click', function(){
    	$(".sideline").css('display','none');
        $(this).addClass("find_nav_cur").siblings().removeClass("find_nav_cur");
    });
});


/**
 * 基础函数定义
 */
$(function () {

    /**
     * ajax请求（get）
     *
     * @param url 请求URL
     * @param callback 回调函数
     */
    jQuery.httpGet = function (url, callback) {
        $.getJSON(addTimestamp(url), function (data) {
            if (data) {
                if (1000 == data.code) {
                    callback(data.body);
                } else if (1001 == data.code) {
                    $.messager.alert("提示信息", data.msg, "error");
                } else if (1002 == data.code) {
                    $.messager.alert("提示信息", "系统发生错误：" + data.msg, "error");
                }
            } else {
                $.messager.alert("提示信息", "返回数据异常", "error");
            }
        });
    };

    /**
     * ajax请求（post）
     *
     * @param url 请求URL
     * @param param 请求参数（json）
     * @param showMsg 是否显示提交成功提示信息
     * @param callback 回调函数
     */
    jQuery.httpPost = function (url, param, showMsg, callback, errCallback) {

        $.ajax({
            type: "post",
            dataType: "json",
            contentType: "application/json",
            url: addTimestamp(url),
            data: JSON.stringify(param),
            success: function (data) {
                if (data) {
                    if (1000 == data.code) {
                        if (showMsg) {
                            $.messager.alert("提示消息", "提交成功", "info");
                        }

                        if (callback) {
                            callback(data.body);
                        }
                    } else if (1001 == data.code) {
                        $.messager.alert("提示信息", data.msg, "error", function () {
                                if (errCallback) {
                                    errCallback();
                                }
                            }
                        );

                    } else if (1002 == data.code) {
                        $.messager.alert("提示信息", "系统发生错误：" + data.msg, "error");
                    }
                } else {
                    $.messager.alert("提示信息", "返回数据异常", "error");
                }
            }
        });
    };

    /**
     * easyui列表封装
     */
    $.fn.dg = function (options) {

    	var dgOptions = {
                title: isBlank(options.title) ? "查询结果" : options.title,
                width: isBlank(options.width) ? "100%" : options.width,
                striped: isBlank(options.striped) ? true : options.striped,
                fitColumns: isBlank(options.fitColumns) ? false : options.fitColumns,
                toolbar: isBlank(options.toolbar) ? null : options.toolbar,
                height: isBlank(options.height) ? cmTableH : options.height,
                rownumbers: isBlank(options.rownumbers) ? true : options.rownumbers,
                singleSelect: isBlank(options.singleSelect) ? false : options.singleSelect,
                pagination: isBlank(options.pagination) ? true : options.pagination,
                remoteSort: isBlank(options.remoteSort) ? false : options.remoteSort,
                pagePosition: isBlank(options.pagePosition) ? "bottom" : options.pagePosition,
                frozen: isBlank(options.frozen) ? true : options.frozen,
                url: addTimestamp(options.url),
                data: isBlank(options.data) ? null : options.data,
                method: "post",
                columns: options.columns
        };
    	
    	if (!isBlank(options.onLoadSuccess)) {
    		dgOptions.onLoadSuccess = options.onLoadSuccess;
    	}
    	if (!isBlank(options.onClickCell)) {
    		dgOptions.onClickCell = options.onClickCell;
    	}
    	if (!isBlank(options.onClickRow)) {
    		dgOptions.onClickRow = options.onClickRow;
    	}
    	if (!isBlank(options.onEndEdit)) {
    		dgOptions.onEndEdit = options.onEndEdit;
    	}
    	
        $(this).datagrid(dgOptions);
    };

    /**
     * 获取表单数据处理
     */
    $.fn.getFormData = function () {
        var o = {};
        var a = this.serializeArray();

        var disabledItems = $(this).find("[disabled='disabled']");

        // 对表单内的无效控件进行串行化
        $.each(disabledItems, function () {
            if (this.name && this.value) {
                var name = this.name;
                var val = this.value;

                a.push({"name": name, "value": val});
            }
        });

        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });

        return o;
    };
});

/**
 * 判断是否空值
 * 添加全是空格也为空的判断
 */
function isBlank(o) {
	
	if (typeof(o) == "boolean") {
		return false;
	}
	
    return (o == null || o == "" || typeof(o) == "undefined");
}

/**
 * 转时间戳函数
 */

function getDateFormatter(rowIndex, rowData) {
	if(rowIndex==null){
		return
	}
    var date = new Date(rowIndex);
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    return Y + M + D + h + m + s;
}

/**
 * 电话验证，参数值phoneNum
 */
$(function () {
    $.extend($.fn.validatebox.defaults.rules, {
        phoneNum: {
            validator: function (value) {
                var cmccMobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
                var tel = /^\d{3,4}?\d{7,8}$/;
                return tel.test(value)
                    || (value.length == 11 && cmccMobile
                        .test(value));
            },
            message: "请正确填写您的联系电话."
        },
        idcared: {
            validator: function (value, param) {
                var flag = isCardID(value);
                return flag == true ? true : false;
            },
            message: '不是有效的身份证号码'
        },
        integer: {// 验证正整数 
            validator: function (value) {
                return /^[1-9]*[1-9][0-9]*$/i.test(value);
            },
            message: '请输入正整数'
        },
        intOrFloat: {// 验证整数或小数      
            validator: function (value) {
                //  /^\d+(\.\d{1,2})?$/.test(endrate)
                return /^\d+(\.\d{1,2})?$/i.test(value);
            },
            message: '请输入数字，并确保格式正确'
        },
        maxLength: {// 验证最大输入长度
            validator: function (value, param) {
                //return param[0] >= value.length;
                return param[0] >= value.replace(/[^x00-xff]/g, "01").length;
            },
            message: '不能输入长度大于{0}位字符的内容.'
        },
        qq: {// 验证QQ
            validator: function (value) {
                return /^[1-9][0-9]{4,9}$/.test(value);
            },
            message: '请输入正确的QQ号码'
        },
        passport: {  //验证护照
            validator: function (value) {
                var reg = /^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+|E[0-9]{8}|H[0-9]{10}|M[0-9]{10}$/;
                return reg.test(value);
            },
            message: '请输入正确的护照号码'
        },
        code: {  //验证只能输入数字或英文
            validator: function (value) {
                return /^[A-Za-z0-9]+$/.test(value);
            },
            message: '请输入数字或英文'
        },
        number: {  //验证只能输入数字
            validator: function (value) {
                return /^[0-9]+$/.test(value);
            },
            message: '请输入数字'
        },
        isBlank: {
            validator: function (value, param) { return $.trim(value) != '' },
            message: '不能为空格'
        }
    })
});

/*表单使用验证*/
$('.inputl-check86').textbox({
    width:'86%'
});
$('.input-check72').textbox({
    width:'72%'
});


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

/**
 * 给请求URI添加时间戳
 */
function addTimestamp(uri) {
	
	if (isBlank(uri)) {
		return "";
	}
	
	if (uri.indexOf("?") > 0) {
		uri = uri + "&timestamp=" + new Date();
	} else {
		uri = uri + "?timestamp=" + new Date();
	}
	
	return uri;
}

/**
* 返回前页处理
*/
function goBack() {
	location.href=document.referrer;
}