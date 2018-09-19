package com.neusoft.arltr.indexing.repository;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PdmDocInfoFailRepository extends CrudRepository<PdmDocInfoFail, Integer>,JpaSpecificationExecutor<PdmDocInfoFail> {

    PdmDocInfoFail findByDocId(String docId);
}
