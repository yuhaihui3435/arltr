package com.neusoft.arltr.indexing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.indexing.CronTask;

/**
 * 定时任务数据仓库
 * @author ye.yy
 *
 */
public interface CronTaskRepository extends CrudRepository<CronTask, Integer>,JpaSpecificationExecutor<CronTask>{

}
