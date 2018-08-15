/**
 * 
 */
package com.neusoft.arltr.indexing.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import com.neusoft.arltr.common.constant.Constant;
import com.neusoft.arltr.common.entity.indexing.PdmDocInfo;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.utils.ChineseToPinyin;
import com.neusoft.arltr.indexing.repository.PdmDocInfoRepository;
import com.neusoft.arltr.indexing.repository.SolrRepository;

/**
 * PDM业务Service
 * 
 * @author zhanghaibo
 */
@Service
public class PdmService {
	
	/**数据状态：已建立索引 */
	private static final int DATA_STATE_INDEXED = 2;
	
	@Autowired
	SolrRepository solrRepository;
	
	@Autowired
	PdmDocInfoRepository pdmDocInfoRepository;
	
	@Async
	public void createIndexOfPdmDoc(PdmDocInfo doc, List<String> finishedTaskList, StringBuffer errorInfo) throws Exception {
		
		try {
			
			Result solrDoc = solrRepository.findByOriginalIdAndSource(doc.getDocumentId(), Constant.SOURCE_PDM);
			
			// 建立索引
			if (solrDoc == null) {
				solrDoc = new Result();
			}
			
			solrDoc.setSource(Constant.SOURCE_PDM);
			solrDoc.setScore(0);
			solrDoc.setResourcename("PDM");
			solrDoc.setContentType("2");
			solrDoc.setOriginalId(doc.getDocumentId());
			solrDoc.setClassification(doc.getSecretLevel());
			solrDoc.setTitle(doc.getDocumentTitle());
			solrDoc.setTitleAbbre(ChineseToPinyin.getPinYinAbbre(solrDoc.getTitle()));
			solrDoc.setTitlePinyin(ChineseToPinyin.getPinYin(solrDoc.getTitle()));
			if ("ApplicationData".equals(doc.getFileType())) {
				solrDoc.setContent(getFileContent(doc.getFilePath()));
			} else {
				solrDoc.setContent(" ");
			}
			solrDoc.setAuthor(doc.getAuthor());
			solrDoc.setAuthorAbbre(ChineseToPinyin.getPinYinAbbre(solrDoc.getAuthor()));
			solrDoc.setAuthorPinyin(ChineseToPinyin.getPinYin(solrDoc.getAuthor()));
//			solrDoc.setCache((doc.getFileContent() != null && doc.getFileContent().length() > 32766) ? doc.getFileContent().substring(0, 32765) : doc.getFileContent());
			solrDoc.setCache(" ");
			solrDoc.setTimestamp(System.currentTimeMillis());
			solrDoc.setLastModified(new Date());
			solrDoc.setUrl(doc.getUrl());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("documentCon", doc.getDocumentCon());
			jsonObject.put("documentPhase", doc.getDocumentPhase());
			jsonObject.put("fileSmallType", doc.getFileSmallType());
			jsonObject.put("majorOri", doc.getMajorOri());
			jsonObject.put("majorType", doc.getMajorType());
			jsonObject.put("documentDept", doc.getDocumentDept());
			jsonObject.put("keyWord", doc.getKeyWord());
			jsonObject.put("documentResponDept", doc.getDocumentResponDept());
			
			String extend = jsonObject.toString();

			solrDoc.setExtend(extend);
			solrDoc.setSummary(doc.getDigest());
			
			solrRepository.save(solrDoc);
			
			doc.setDataState(DATA_STATE_INDEXED);
			pdmDocInfoRepository.save(doc);
			
		} catch (Exception e) {
			errorInfo.append("PDM文档建立索引出错【" + e.getMessage() + "】\n");
			throw e;
		} finally {
			finishedTaskList.add("");
		}
	}
	
	private String getFileContent(String filePath) throws Exception {
		
        ContentHandler handler = null;
        InputStream input = null;
        
        try {
            
            Parser parser = new AutoDetectParser();
            Metadata meta = new Metadata();
            
            URL url = new URL(filePath);
    		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            
            input = httpConnection.getInputStream();
            handler = new BodyContentHandler(-1);
            
            parser.parse(input, handler, meta, new ParseContext());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
            	if (input != null) {
            		input.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return handler.toString();
    }
}
