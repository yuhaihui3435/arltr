package com.neusoft.arltr.indexing.service;

import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.model.indexing.ScheduleJob;

/**
 * 调度配置Service
 * @author ye.yy
 *
 */
public interface QuartzConfigService {

	/**
	 * 根据定时任务信息，配置cron表达式
	 * @param cronTask 定时任务信息
	 * @return cron表达式
	 */
	public String configCronExpression(CronTask cronTask);
	
	/**
	 * 根据定时任务信息，启动调度
	 * @param scheduleJob 计划任务Model
	 */
	public void startQuartz(ScheduleJob scheduleJob);
	
}
