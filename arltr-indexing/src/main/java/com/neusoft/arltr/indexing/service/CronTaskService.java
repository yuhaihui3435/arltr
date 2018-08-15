package com.neusoft.arltr.indexing.service;

import com.neusoft.arltr.common.entity.indexing.CronTask;

/**
 * 定时任务Service
 * @author ye.yy
 *
 */
public interface CronTaskService {
	
	/**
	 * 查询定时任务
	 * @return CronTask 定时任务信息
	 */
	public CronTask getCronTask();
	
	/**
	 * 保存定时任务
	 * @param cronTask 定时任务信息
	 * @return CronTask 成功返回的定时任务信息
	 */
	public CronTask saveCronTask(CronTask cronTask);
	
}
