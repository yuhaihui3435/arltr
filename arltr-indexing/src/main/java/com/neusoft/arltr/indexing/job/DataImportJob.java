package com.neusoft.arltr.indexing.job;

import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.model.indexing.ScheduleJob;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.indexing.service.CronTaskService;
import com.neusoft.arltr.indexing.service.IndexingService;
import com.neusoft.arltr.indexing.utils.SpringContextUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 索引维护Job
 * @author ye.yy
 *
 */
@DisallowConcurrentExecution
public class DataImportJob implements Job {
	
	private static Logger logger = Logger.getLogger(DataImportJob.class);
	
	/**任务类型——自动*/
	private static final short INDEX_TASK_TYPE_AUTO = 1;
	
	@Autowired
	CronTaskService cronTaskService;
	
	@Autowired
	IndexingService indexingService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		cronTaskService = (CronTaskService) SpringContextUtil.getBean("cronTaskServiceImpl");
		indexingService = (IndexingService) SpringContextUtil.getBean("indexingService");
		ScheduleJob scheduleJob=(ScheduleJob) arg0.getJobDetail().getJobDataMap().get("scheduleJob");
		String jobName=scheduleJob.getJobName();
		String desc=scheduleJob.getDesc();
		DateFormat bf = new SimpleDateFormat("yyyy-MM-dd");
		CronTask cronTask=null;
		if("索引全量更新".equals(jobName)){
			logger.info(">>>>>>>>>>>>>>>执行索引增量更新<<<<<<<<<<<<");
			logger.info("本次索引全量更新的日期范围是:"+desc);
			if(StringUtils.isBlank(desc)){
				logger.error("本次索引全量更新的范围错误，任务失败");
			}else {
				String[] dateRange=desc.split("\\|");
				String sDateStr=dateRange[0].trim();
				String eDateStr=dateRange[1].trim();
				List<String> dateList=new ArrayList<String>();
				try {
					Date sDate=DateUtils.parseDate(sDateStr,"yyyy-MM-dd");
					Date eDate=DateUtils.parseDate(eDateStr,"yyyy-MM-dd");
					int tc=DateUtils.truncatedCompareTo(sDate,eDate, Calendar.DATE);
					if(tc>0){
						logger.error("开始日期:"+sDateStr+"应该在结束日期:"+eDateStr+"之前，任务失败");
						throw new BusinessException("全量更新任务执行失败，开始日期应该在结束日期之前");
					}

					boolean calDate=true;
					Date date=sDate;
					if(tc<0)dateList.add(sDateStr);
					while (calDate){
						date=DateUtils.addDays(date,1);
						if(DateUtils.truncatedCompareTo(date,eDate,Calendar.DATE)==-1){
							dateList.add(bf.format(date));
						}else{
							calDate=false;
						}
					}
					dateList.add(eDateStr);
					logger.info("全量索引更新的日期范围处理结束，开始执行全量更新的任务");

					String taskInfo="【"+sDateStr+"至"+eDateStr+"】";
					dateList.stream().forEach(d->this.indexingService.fullAmountInsertData(INDEX_TASK_TYPE_AUTO, null,d,taskInfo ));
					logger.info(taskInfo+"执行完成");
				} catch (ParseException e) {
					logger.error("日期格式不正确，任务失败");
					throw new BusinessException("全量更新任务执行失败，日期格式不正确");
				}

			}
		}else {
			logger.info(">>>>>>>>>>>>>>>执行索引增量Job<<<<<<<<<<<<<");
			cronTask = this.cronTaskService.getCronTask();
			this.indexingService.insertData(INDEX_TASK_TYPE_AUTO, cronTask.getImportType(), cronTask, null);
		}
	}

}
