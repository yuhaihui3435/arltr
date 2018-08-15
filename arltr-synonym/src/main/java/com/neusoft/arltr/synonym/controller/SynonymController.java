package com.neusoft.arltr.synonym.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.data.domain.Sort.Order;
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
import com.neusoft.arltr.common.entity.synonym.Synonym;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.utils.SolrClientHelper;
import com.neusoft.arltr.synonym.repository.SynonymRepository;
/**
 * 同义词控制类
 * @author wuxl
 *
 */
@RestController
@RequestMapping("/synonym")
public class SynonymController {
	@Autowired
	SynonymRepository synonymRepository;
	@Autowired
	SolrClientHelper solrClientHelper;
	
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param order 升降排序
	* @param sort  排序字段
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody Synonym condition){
		
		Order viewOrder;
		if("asc".equals(order)){
			viewOrder=new Order(Sort.Direction.ASC,sort);
		}else{
			viewOrder=new Order(Sort.Direction.DESC,sort);
		}
		
		Sort sorts=new Sort(viewOrder, new Sort.Order(Sort.Direction.DESC, sort));
		Pageable pageable =new PageRequest(pageNumber-1,pageSize,sorts);
		
		Specification<Synonym> specification = new Specification<Synonym>() {
			@Override
			public Predicate toPredicate(Root<Synonym> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> word = root.get("word");
				Path<String> synonymWord = root.get("synonymWord");

				List<Predicate> list = new ArrayList<Predicate>();
				//词条
				if (condition.getWord() != null && !"".equals(condition.getWord()) ) {
					list.add(cb.like(word, "%"+condition.getWord()+"%"));
				}
				//同义词
				if (condition.getSynonymWord() != null && !"".equals(condition.getSynonymWord())) {
					list.add(cb.like(synonymWord, "%"+condition.getSynonymWord()+"%"));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));		
			}
		};

		RespBody<ListPage> resbody = new RespBody<ListPage>();
		Page<Synonym>  respage = synonymRepository.findAll(specification,pageable);
		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		
		return resbody;
	}
	/**
	* 根据id获取同义词
	* 
	* @param id 主键
	* @return 同义词实体类
	*/
	@GetMapping("/{id}")
	public RespBody<Synonym> getOne(@PathVariable("id") Integer id){
		Synonym infor = synonymRepository.findOne(id);
		RespBody<Synonym> resInfor = new RespBody<Synonym>();
		resInfor.setBody(infor);
		return resInfor;
	}
	/**
	* 保存同义词
	* 
	* @param entity 同义词实体类
	* @return 返回信息
	*/
	@PostMapping("/save")
	public RespBody<Synonym> save(@RequestBody Synonym entity){
		Synonym content = new Synonym();
		RespBody<Synonym> resInfor = new RespBody<Synonym>();
		try{
			 content = synonymRepository.save(entity);
			 resInfor.setBody(content);
			//同步Solr上的同义词
			Map<String, Object> synInfo = new HashMap<String,Object>();
			//词条
			String word = content.getWord();
			//同义词串
			String synword = content.getSynonymWord();
			//同义词分隔成数组
			String[] synwordArray = synword.split(",");
			synInfo.put(word, synwordArray);
			solrClientHelper.saveSynonym(synInfo);
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("词条：["+entity.getWord()+"]重复!");
		}

		return resInfor;
	}
	
	/**
	* 批量保存同义词
	* 
	* @param entity 同义词实体类
	* @return 返回信息
	*/
	@PostMapping("/saveall")
	public RespBody<String> saveAll(@RequestBody List<Synonym> entities){
		
		for (Synonym entity : entities) {
			
			try{
				synonymRepository.save(entity);
				
			}catch (DataIntegrityViolationException e) {
				continue;
			}
			
			Map<String, Object> synInfo = new HashMap<String,Object>();
			
			//词条
			String word = entity.getWord();
			//同义词串
			String synword = entity.getSynonymWord();
			//同义词分隔成数组
			String[] synwordArray = synword.split(",");
			synInfo.put(word, synwordArray);
			
			solrClientHelper.saveSynonym(synInfo);
		}

		return new RespBody<String>();
	}
	
	/**
	* 删除同义词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id){
		
		Synonym entity = synonymRepository.findOne(id);
		String word = entity.getWord();
		
		synonymRepository.delete(id);
		
		solrClientHelper.removeSynonym(word);
		
		return new RespBody<String>();
	}
	
	/**
	* 批量删除同义词
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<Synonym> entities){
		
		List<String> words = new ArrayList<String>();
		
		for (Synonym entity : entities) {
			words.add(entity.getWord());
		}
		
		synonymRepository.delete(entities);
		
		solrClientHelper.removeSynonym(words.toArray(new String[]{}));
		
		return new RespBody<String>();
	}
}
