package com.neusoft.arltr.indexing.service;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import com.neusoft.arltr.indexing.repository.PdmDocInfoFailRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PdmDocInfoFailService {

    private static final Logger logger = Logger.getLogger(PdmDocInfoFailService.class.getName());

    @Autowired
    private PdmDocInfoFailRepository pdmDocInfoFailRepository;

    public void addPdmDocInfoFail(PdmDocInfoFail pdmDocInfoFail){

        logger.info("pmd文档同步出现错误，增加错误记录");
        Date now =new Date();
        PdmDocInfoFail pdmDocInfoFail1=pdmDocInfoFailRepository.findByDocId(pdmDocInfoFail.getDocId());
        if(pdmDocInfoFail1!=null){
            pdmDocInfoFail.setuAt(now);
            logger.info("pdm文档docId已经存在，更新原因错误记录");
            pdmDocInfoFail.setId(pdmDocInfoFail1.getId());
        }else{
            pdmDocInfoFail.setcAt(now);
            pdmDocInfoFail.setuAt(now);
        }
        pdmDocInfoFailRepository.save(pdmDocInfoFail);

        logger.info("pmd文档同步出现错误，增加错误记录完成");
    }

    public void removePdmDocInfoFail(String documentId){
        PdmDocInfoFail pdmDocInfoFail=pdmDocInfoFailRepository.findByDocId(documentId);
        if(pdmDocInfoFail!=null){
            logger.info("docId为："+documentId+"文档索引建立完成，移除失败状态");
            pdmDocInfoFailRepository.delete(pdmDocInfoFail);
            logger.info("docId为："+documentId+"文档索引建立完成，移除失败状态完成");
        }
    }
}
