/**
 * 
 */
package com.neusoft.arltr.indexing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfo;

/**
 * 
 * PDM文档信息数据仓库
 *
 */
public interface PdmDocInfoRepository extends CrudRepository<PdmDocInfo, Integer> {

	public List<PdmDocInfo> findByDataState(Integer dataState);

	public PdmDocInfo findByDocumentIdAndVersionSno(String documentId,String versionSno);


}
