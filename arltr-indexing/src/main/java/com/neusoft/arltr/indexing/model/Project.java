/**
 * 
 */
package com.neusoft.arltr.indexing.model;

/**
 * TDM数据结构
 * 
 *
 */
public class Project {

	/** 试验信息id */
	private String id;
	/** 试验名称 */
	private String name;
	/** 试验代号 */
	private String code;
	/** 试验时间 */
	private String createdate;
	/** 试验负责人 */
	private String manager;
	/** 产品型号 */
	private String modelid;
	/** 研制阶段 */
	private String stage;
	/** 试验类型 */
	private String testcategory;
	/** 考核产品 */
	private String modelproduct;
	/** 产品路径 */
	private String product1;
	/** 产品路径 */
	private String product2;
	/** 产品路径 */
	private String product3;
	/** 产品路径 */
	private String product4;
	/** 产品路径 */
	private String product5;
	/** 产品路径 */
	private String product6;
	/** 产品路径 */
	private String product7;
	/** 产品路径 */
	private String product8;
	/** 产品路径 */
	private String product9;
	/** 产品路径 */
	private String product10;
	/** 其他负责人 */
	private String othermanager;
	/** 责任部门 */
	private String testapartment;
	/** 试验地点 */
	private String testplace;
	/** 任务来源 */
	private String tasksource;
	/** 试验依据 */
	private String testbasis;
	/** 试验数据命名简写 */
	private String datashortname;
	/** 试验数据解析方法简述 */
	private String dataanalysisresume;
	/** 试验遇到的技术问题简要描述 */
	private String techniquequestion;
	/** 试验结论概述 */
	private String testconclusion;
	/** 试验结论补充说明 */
	private String conclusionexplain;
	/** 试验目的 */
	private String intention;
	/** 创建人 */
	private String creator;
	/** url */
	private String url;
	/** 数据操作类型 */
	private Integer optType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getModelid() {
		return modelid;
	}
	public void setModelid(String modelid) {
		this.modelid = modelid;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getTestcategory() {
		return testcategory;
	}
	public void setTestcategory(String testcategory) {
		this.testcategory = testcategory;
	}
	public String getModelproduct() {
		return modelproduct;
	}
	public void setModelproduct(String modelproduct) {
		this.modelproduct = modelproduct;
	}
	public String getProduct1() {
		return product1;
	}
	public void setProduct1(String product1) {
		this.product1 = product1;
	}
	public String getProduct2() {
		return product2;
	}
	public void setProduct2(String product2) {
		this.product2 = product2;
	}
	public String getProduct3() {
		return product3;
	}
	public void setProduct3(String product3) {
		this.product3 = product3;
	}
	public String getProduct4() {
		return product4;
	}
	public void setProduct4(String product4) {
		this.product4 = product4;
	}
	public String getProduct5() {
		return product5;
	}
	public void setProduct5(String product5) {
		this.product5 = product5;
	}
	public String getProduct6() {
		return product6;
	}
	public void setProduct6(String product6) {
		this.product6 = product6;
	}
	public String getProduct7() {
		return product7;
	}
	public void setProduct7(String product7) {
		this.product7 = product7;
	}
	public String getProduct8() {
		return product8;
	}
	public void setProduct8(String product8) {
		this.product8 = product8;
	}
	public String getProduct9() {
		return product9;
	}
	public void setProduct9(String product9) {
		this.product9 = product9;
	}
	public String getProduct10() {
		return product10;
	}
	public void setProduct10(String product10) {
		this.product10 = product10;
	}
	public String getOthermanager() {
		return othermanager;
	}
	public void setOthermanager(String othermanager) {
		this.othermanager = othermanager;
	}
	public String getTestapartment() {
		return testapartment;
	}
	public void setTestapartment(String testapartment) {
		this.testapartment = testapartment;
	}
	public String getTestplace() {
		return testplace;
	}
	public void setTestplace(String testplace) {
		this.testplace = testplace;
	}
	public String getTasksource() {
		return tasksource;
	}
	public void setTasksource(String tasksource) {
		this.tasksource = tasksource;
	}
	public String getTestbasis() {
		return testbasis;
	}
	public void setTestbasis(String testbasis) {
		this.testbasis = testbasis;
	}
	public String getDatashortname() {
		return datashortname;
	}
	public void setDatashortname(String datashortname) {
		this.datashortname = datashortname;
	}
	public String getDataanalysisresume() {
		return dataanalysisresume;
	}
	public void setDataanalysisresume(String dataanalysisresume) {
		this.dataanalysisresume = dataanalysisresume;
	}
	public String getTechniquequestion() {
		return techniquequestion;
	}
	public void setTechniquequestion(String techniquequestion) {
		this.techniquequestion = techniquequestion;
	}
	public String getTestconclusion() {
		return testconclusion;
	}
	public void setTestconclusion(String testconclusion) {
		this.testconclusion = testconclusion;
	}
	public String getConclusionexplain() {
		return conclusionexplain;
	}
	public void setConclusionexplain(String conclusionexplain) {
		this.conclusionexplain = conclusionexplain;
	}
	public String getIntention() {
		return intention;
	}
	public void setIntention(String intention) {
		this.intention = intention;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getOptType() {
		return optType;
	}
	public void setOptType(Integer optType) {
		this.optType = optType;
	}
	
}
