package com.neusoft.arltr.indexing.job;

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
		logger.info(">>>>>>>>>>>>>>>执行索引维护Job<<<<<<<<<<<<<");
		cronTaskService = (CronTaskService) SpringContextUtil.getBean("cronTaskServiceImpl");
		indexingService = (IndexingService) SpringContextUtil.getBean("indexingService");
		CronTask cronTask = this.cronTaskService.getCronTask();
		this.indexingService.insertData(INDEX_TASK_TYPE_AUTO, cronTask.getImportType(), cronTask, null);
	}

}
