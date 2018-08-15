package com.neusoft.arltr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.model.indexing.ScheduleJob;
import com.neusoft.arltr.indexing.service.CronTaskService;
import com.neusoft.arltr.indexing.service.QuartzConfigService;

/**
 * 启动时，取到定时任务数据
 * @author ye.yy
 *
 */
@Component
public class CronTaskLoader implements CommandLineRunner {
	
	@Autowired
	CronTaskService cronTaskService;
	
	@Autowired
	QuartzConfigService quartzConfigService;

	@Override
	public void run(String... arg0) throws Exception {
		CronTask cronTask = this.cronTaskService.getCronTask();
		if(cronTask == null){
			return;
		}
		short importType = cronTask.getImportType();
		String jobName = importType == 1 ? "索引全量更新" : "索引增量更新";
		//设置任务属性
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setJobName(jobName);
		scheduleJob.setJobGroup(String.valueOf(importType));
		scheduleJob.setCronExpression(this.quartzConfigService.configCronExpression(cronTask));
		//启动调度
		this.quartzConfigService.startQuartz(scheduleJob);
	}

}
