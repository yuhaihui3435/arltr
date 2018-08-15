package com.neusoft.arltr.indexing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.indexing.DataImportLogs;

/**
 * 索引维护数据仓库
 * @author wuxl
 *
 */
public interface IndexingRepository extends CrudRepository<DataImportLogs, Integer>,JpaSpecificationExecutor<DataImportLogs> {

}
