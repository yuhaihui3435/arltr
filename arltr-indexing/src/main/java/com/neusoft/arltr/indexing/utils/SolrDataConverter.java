/**
 * 
 */
package com.neusoft.arltr.indexing.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.neusoft.arltr.common.constant.Constant;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.utils.ChineseToPinyin;
import com.neusoft.arltr.indexing.model.Project;

/**
 * Solr文档转换工具类
 * 
 *
 * 
 */
public class SolrDataConverter {

	/**
	 * 将TDM接口数据转换为Solr文档
	 * 
	 * @param source TDM接口数据
	 * @param target 目标Solr文档
	 *
	 */
	public static void convert(Project source, Result target) {
		
		StringBuilder content = new StringBuilder();
		appendFieldToContent(content, "试验代号", source.getCode());
		appendFieldToContent(content, "试验时间", source.getCreatedate().toString());
		appendFieldToContent(content, "试验负责人", source.getManager());
		appendFieldToContent(content, "产品型号", source.getModelid());
		appendFieldToContent(content, "研制阶段", source.getStage());
		appendFieldToContent(content, "试验类型", source.getTestcategory());
		appendFieldToContent(content, "考核产品", source.getModelproduct());
		appendFieldToContent(content, "产品路径", source.getProduct1());
		appendFieldToContent(content, "产品路径", source.getProduct2());
		appendFieldToContent(content, "产品路径", source.getProduct3());
		appendFieldToContent(content, "产品路径", source.getProduct4());
		appendFieldToContent(content, "产品路径", source.getProduct5());
		appendFieldToContent(content, "产品路径", source.getProduct6());
		appendFieldToContent(content, "产品路径", source.getProduct7());
		appendFieldToContent(content, "产品路径", source.getProduct8());
		appendFieldToContent(content, "产品路径", source.getProduct9());
		appendFieldToContent(content, "产品路径", source.getProduct10());
		appendFieldToContent(content, "其他负责人", source.getOthermanager());
		appendFieldToContent(content, "责任部门", source.getTestapartment());
		appendFieldToContent(content, "试验地点", source.getTestplace());
		appendFieldToContent(content, "任务来源", source.getTasksource());
		appendFieldToContent(content, "试验依据", source.getTestbasis());
		appendFieldToContent(content, "试验数据命名简写", source.getDatashortname());
		appendFieldToContent(content, "试验数据解析方法简述", source.getDataanalysisresume());
		appendFieldToContent(content, "试验遇到的技术问题简要描述", source.getTechniquequestion());
		appendFieldToContent(content, "试验结论概述", source.getTestconclusion());
		appendFieldToContent(content, "试验结论补充说明", source.getConclusionexplain());
		appendFieldToContent(content, "试验目的", source.getIntention());
		appendFieldToContent(content, "创建人", source.getCreator());
		
		target.setTitle(source.getName());
		target.setTitleAbbre(ChineseToPinyin.getPinYinAbbre(target.getTitle()));
		target.setTitlePinyin(ChineseToPinyin.getPinYin(target.getTitle()));
		target.setContent(content.toString());
		target.setAuthor(source.getManager());
		target.setAuthorAbbre(ChineseToPinyin.getPinYinAbbre(target.getAuthor()));
		target.setAuthorPinyin(ChineseToPinyin.getPinYin(target.getAuthor()));
		target.setCache(content.toString());
		target.setTimestamp(System.currentTimeMillis());
		target.setLastModified(new Date());

		
		target.setUrl(source.getUrl());
	}
	
	/**
	 * 将TDM接口数据转换为Solr文档
	 * 
	 * @param source TDM接口数据
	 * @return 目标Solr文档
	 */
	public static Result convert(Project source) {
		
		Result target = new Result();
		target.setSource(Constant.SOURCE_TDM);
		target.setScore(0);
		target.setResourcename("TDM");
		target.setContentType("1");
		target.setOriginalId(source.getId());
		target.setClassification("60"); // TDM默认密级：非密
		
		convert(source, target);
		
		return target;
	}
	
	private static void appendFieldToContent(StringBuilder content, String fieldName, String fieldValue) {
		
		if (StringUtils.isNotEmpty(fieldValue)) {
			content.append(fieldName);
			content.append("：");
			content.append(fieldValue);
			content.append("    ");
		}
	}
}
