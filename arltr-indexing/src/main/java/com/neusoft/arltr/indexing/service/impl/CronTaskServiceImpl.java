package com.neusoft.arltr.indexing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.indexing.repository.CronTaskRepository;
import com.neusoft.arltr.indexing.service.CronTaskService;

/**
 * 定时任务Service实现类
 * @author ye.yy
 *
 */
@Service
public class CronTaskServiceImpl implements CronTaskService {

	@Autowired
	CronTaskRepository cronTaskRepository;
	
	@Override
	public CronTask getCronTask() {
		CronTask cronTask = null;
		List<CronTask> cronTaskList = (List<CronTask>) this.cronTaskRepository.findAll(); 
		if(cronTaskList != null && cronTaskList.size()>0){
			cronTask = cronTaskList.get(0);  //定时任务只能有一条数据
		}
		return cronTask;
	}

	@Override
	public CronTask saveCronTask(CronTask cronTask) {
		CronTask cronTaskNew = this.cronTaskRepository.save(cronTask);
		return cronTaskNew;
	}

}
