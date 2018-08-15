package com.neusoft.arltr.indexing.service.impl;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.model.indexing.ScheduleJob;
import com.neusoft.arltr.indexing.job.DataImportJob;
import com.neusoft.arltr.indexing.service.QuartzConfigService;

/**
 * 调度配置Service实现类
 * @author ye.yy
 *
 */
@Service
public class QuartzConfigServiceImpl implements QuartzConfigService {
	
	private static Logger logger = Logger.getLogger(QuartzConfigServiceImpl.class);
	
	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	@Override
	public String configCronExpression(CronTask cronTask) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(cronTask.getStartTime());
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		short frequency = cronTask.getFrequency();
		String cronExpression = "";
		switch(frequency){
			case 1 : cronExpression = "0 "+minute+" "+hour+" * * ?";  //每天
				break;
			case 2 : cronExpression = "0 "+minute+" "+hour+" ? * "+ dayofweek; //每周
				break;
			case 3 : cronExpression = "0 "+minute+" "+hour+" "+day+"/15" +" "+"* ?";  //半个月
				break;
		}
		return cronExpression;
	}

	@Override
	public void startQuartz(ScheduleJob scheduleJob) {
		//获取调度器容器
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if(scheduleJob != null){
			//获取触发器标识  
	        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
	        try {
	        	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				if(null==trigger){
					logger.info(">>>>>>>>>>>>>>>新建定时任务<<<<<<<<<<<<<");
					//创建任务  
		            JobDetail jobDetail = JobBuilder.newJob(DataImportJob.class)  
		                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())  
		                    .build();
		            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob); 
		            //表达式调度构建器  
		            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob  
		                    .getCronExpression());
		            //按新的cronExpression表达式构建一个新的trigger  
		            trigger = TriggerBuilder.newTrigger()  
		                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())  
		                    .withSchedule(scheduleBuilder)  
		                    .build();
		            scheduler.scheduleJob(jobDetail, trigger);
				}else{
					logger.info(">>>>>>>>>>>>>>>更新定时任务<<<<<<<<<<<<<");
					// Trigger已存在，那么更新相应的定时设置  
		            //表达式调度构建器  
		            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob  
		                    .getCronExpression());
		            //按新的cronExpression表达式重新构建trigger  
		            trigger = trigger.getTriggerBuilder()  
		                    .withIdentity(triggerKey)  
		                    .withSchedule(scheduleBuilder)  
		                    .build();  
		            //按新的trigger重新设置job执行  
		            scheduler.rescheduleJob(triggerKey, trigger);
				}
				logger.info(">>>>>>>>>>>>>>>定时任务设置成功<<<<<<<<<<<<<");
			} catch (SchedulerException e) {
				e.printStackTrace();
			} 
		}
	}

}
