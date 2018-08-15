package com.neusoft.arltr.lexicon.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.lexicon.Lexicon;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.utils.SolrClientHelper;
import com.neusoft.arltr.lexicon.repository.LexionRepository;

/**
 * 词库管理控制器
 * 
 * @author lishuang
 *
 */
@RestController
@RequestMapping("/lexicon")
public class LexiconController {
	@Autowired
	LexionRepository lexionRepository;
	@Autowired
	SolrClientHelper solrClientHelper;
	/**
	* 根据id获取词条
	* 
	* @param id 主键
	* @return 词条实体类
	*/
	@GetMapping("/{id}")
	public RespBody<Lexicon> getLexicon(@PathVariable Integer id) {
		
		RespBody<Lexicon> resp = new RespBody<Lexicon>();
		Lexicon lexicon = lexionRepository.findOne(id);
		
		if (lexicon == null) {
			throw new BusinessException("该词条不存在");
		}
		
		resp.setBody(lexionRepository.findOne(id));
		
		return resp;
	}
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/query")
	public RespBody<ListPage> getLexiconList(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize, @RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody Lexicon lexicon){
		Sort.Order viewSort;
		if("asc".equals(order)){
			 viewSort=new Sort.Order(Sort.Direction.ASC,sort);
		}else{
			 viewSort=new Sort.Order(Sort.Direction.DESC,sort);
		}
		
		Sort sorts=new Sort(viewSort, new Sort.Order(Sort.Direction.DESC, "id"));
		Pageable pageable = new PageRequest(pageNumber-1, pageSize,sorts);
		Specification<Lexicon> specification = new Specification<Lexicon>(){
            @Override
			public Predicate toPredicate(Root<Lexicon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();		
				Path<String> word = root.get("word");
				if(lexicon.getWord()!=null && !lexicon.getWord().equals("")){
					list.add(cb.like(word, "%"+lexicon.getWord()+"%"));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}};
			RespBody<ListPage> resp = new RespBody<ListPage>();
			Page<Lexicon> p=lexionRepository.findAll(specification,pageable);
			ListPage list = new ListPage(p);
			resp.setBody(list);
		return resp;
	}
	/**
	* 保存词条
	* 
	* @param entity 词条实体类
	* @return 返回信息
	*/
	@PostMapping("/save")
	public RespBody<Lexicon> saveLexicon(@RequestBody Lexicon lexicon){
		RespBody<Lexicon> resp = new RespBody<Lexicon>();
		Lexicon content = new Lexicon();
		try{ 
			content = lexionRepository.save(lexicon);
		    solrClientHelper.removeLexiconWordList(lexicon.getWord());
	        solrClientHelper.saveLexiconWordList(lexicon.getWord());
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("词条：["+lexicon.getWord()+"]重复!");
		}
		resp.setBody(content);
		return resp;
	}
	
	/**
	* 批量保存词条
	* 
	* @param lexicons 词条实体类列表
	* @return 返回信息
	*/
	@PostMapping("/saveall")
	public RespBody<String> saveLexicons(@RequestBody List<Lexicon> lexicons){
		
		List<String> words = new ArrayList<String>();
		
		for (Lexicon lexicon : lexicons) {
			try {
				
				lexionRepository.save(lexicon);
				
			} catch (DataIntegrityViolationException e) {
				continue;
			}
			
			words.add(lexicon.getWord());
		}
		
		solrClientHelper.saveLexiconWordList(words.toArray(new String[]{}));
		
		return new RespBody<String>();
	}
	
	/**
	* 删除词条
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id){
		Lexicon lexicon=lexionRepository.findOne(id);
		solrClientHelper.removeLexiconWordList(lexicon.getWord());
		lexionRepository.delete(id);
        return new RespBody<String>();
	}
	/**
	* 批量删除词条
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<Lexicon> lexicon){
		String word[]=new String[lexicon.size()];
		for(int i=0; i<lexicon.size(); i++){
			word[i]=lexicon.get(i).getWord();
			}
		solrClientHelper.removeLexiconWordList(word);
		lexionRepository.delete(lexicon);
		return new RespBody<String>();
	}

}
