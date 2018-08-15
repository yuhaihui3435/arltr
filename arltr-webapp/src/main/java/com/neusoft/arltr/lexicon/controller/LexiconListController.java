package com.neusoft.arltr.lexicon.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.lexicon.Lexicon;
import com.neusoft.arltr.common.entity.synonym.Synonym;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.service.LexiconService;
import com.neusoft.arltr.common.service.SynonymService;
import com.neusoft.arltr.constant.SessionKey;

/**
 * 航天词汇列表controller
 * @author lishuang
 *
 */
@Controller
@SessionAttributes({SessionKey.USER, SessionKey.NAV_ID, SessionKey.MENU_ID, SessionKey.ITEM_ID})
public class LexiconListController {
	
	@Autowired
	LexiconService lexiconService;
	
	@Autowired
	SynonymService synonymService;
	
	/**
	 * 航空词汇列表页面入口
	 * 	
	 */

	@GetMapping("/lexicon/list")
	public String list(Model model) {
		
		return "lexicon/lexicon_list";
	}
	/**
	* 条件查询（分页）
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param queryParam 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/lexicon/list/data")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			@RequestParam(value="order",defaultValue="desc") String order,
			@RequestParam(value="sort",defaultValue="updateAt") String sort,Lexicon queryParam) {
		 RespBody<ListPage> data=lexiconService.query(pageNumber, pageSize, order, sort, queryParam);
		 return data.getBody();
	}
	/**
	* 删除词条
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/lexicon/remove/{id}")
	@ResponseBody
	public RespBody<String> remove(@PathVariable("id") Integer id){
		return lexiconService.remove(id);
	}
	
	/**
	* 批量删除词条
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/lexicon/remove/multiple")
	@ResponseBody
	public RespBody<String> removeAll(@RequestBody List<Lexicon> lexicon){
		return lexiconService.removeAll(lexicon);
	}
	
	
//	@PostMapping("/lexicon/fileupload")
//	public void getFileUploadTestMethod(@RequestParam("files[]")MultipartFile[] files){
//		System.out.println(files.length+"======================");
//		System.out.println(files[0].getOriginalFilename()+"====================");
//		RespBody<String> res = new RespBody<String>();
//		res.setMsg("ok");
//		return "lexicon/lexicon_list";
//	}
	@PostMapping("/lexicon/fileupload")
	public void getFileUploadTestMethod(HttpServletResponse response, @RequestParam("files[]")MultipartFile[] files, @ModelAttribute(SessionKey.USER) User user) throws IOException{
		System.out.println(files.length+"======================");
		System.out.println(files[0].getOriginalFilename()+"====================");
		RespBody<String> res = new RespBody<String>();
		res.setMsg("ok");
		
		for (MultipartFile file : files) {
			parseOwl(file,  user);
		}
		
		try {
			response.setContentType("text/html");
			response.getOutputStream().write("\"files\" : {\"name\" : \"aaa\", \"size\":\"123\"}".getBytes());
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseOwl(MultipartFile owlFile, User user) throws IOException {
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);

		Date now = new Date();
		
		try {
			ontModel.read((FileInputStream)owlFile.getInputStream(), "");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			throw new BusinessException("OWL文件解析错误");
		}
		
		String baseURI = ontModel.getNsPrefixURI("");
		
		ObjectProperty F = ontModel.getObjectProperty(baseURI + "F");
		ObjectProperty D = ontModel.getObjectProperty(baseURI + "D");

		List<Lexicon> lexicons = new ArrayList<Lexicon>();
		List<Synonym> synonyms = new ArrayList<Synonym>();
		
		for (NodeIterator ii = ontModel.listObjectsOfProperty(F); ii.hasNext();) {
			
			Individual node =  ii.next().as(Individual.class);
			
			String word = node.getLocalName();
			
			if (word!= null && !word.trim().isEmpty()) {
				
				Lexicon lexicon = new Lexicon();
				lexicon.setWord(word);				
				
				lexicon.setCreateAt(now);
				lexicon.setCreateBy(user.getId());
				lexicon.setCreateByName(user.getEmployeeName());
				lexicon.setUpdateAt(now);
				lexicon.setUpdateBy(user.getId());
				lexicon.setUpdateByName(user.getEmployeeName());
				
				lexicons.add(lexicon);
				
				NodeIterator ite = node.listPropertyValues(D);
				
				String synonymWord  = "";
				while (ite.hasNext()) {
					
					RDFNode sameNode =  ite.next();
					
					synonymWord += "," + sameNode.asNode().getLocalName();
				}
				
				if (!synonymWord.isEmpty()) {
					synonymWord = synonymWord.replaceFirst(",", "");
					
					Synonym synonym = new Synonym();
					
					synonym.setWord(word);
					synonym.setSynonymWord(synonymWord);
					
					synonym.setCreateAt(now);
					synonym.setCreateBy(user.getId());
					synonym.setCreateByName(user.getEmployeeName());
					synonym.setUpdateAt(now);
					synonym.setUpdateBy(user.getId());
					synonym.setUpdateByName(user.getEmployeeName());
					
					synonyms.add(synonym);
				}
			}
		}
		
		lexiconService.saveAll(lexicons);
		synonymService.saveAll(synonyms);
	}
}
