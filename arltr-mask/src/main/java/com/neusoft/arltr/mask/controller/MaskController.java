package com.neusoft.arltr.mask.controller;

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
import com.neusoft.arltr.common.entity.mask.MaskWord;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.mask.repository.MaskRepository;

/** 
* 类功能描述:
* @author 作者: 
* @version 创建时间：2017年6月22日 下午2:48:40 
 * @param <MaskRepository>
*  
*/
@RestController
@RequestMapping("/mask")
public class MaskController {
	@Autowired
	MaskRepository maskRepository;
	
	/**
	* 查询屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@GetMapping("/all")
	public RespBody<List<MaskWord>> getAll(){
		RespBody<List<MaskWord>> resp = new RespBody<List<MaskWord>>();
		
		List<MaskWord> it =  (List<MaskWord>)maskRepository.findAll();
		
		resp.setBody(it);
		
		return resp;
	}
	
	/**
	* 查询单条屏蔽词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@GetMapping("/{id}")
	public RespBody<MaskWord> getOne(@PathVariable("id") Integer id){
		RespBody<MaskWord> resp = new RespBody<MaskWord>();
		MaskWord maskWord = maskRepository.findOne(id);
		if (maskWord == null) {
			throw new BusinessException("屏蔽词不存在");
		}
		resp.setBody(maskRepository.findOne(id));
		
		return resp;
	}
	
	/**
	* 保存屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@PostMapping("/save")
	public RespBody<MaskWord> save(@RequestBody MaskWord entity){
		RespBody<MaskWord> resp = new RespBody<MaskWord>();
		try{
		resp.setBody(maskRepository.save(entity));
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("屏蔽词：["+entity.getWord()+"]重复!");
		}
		return resp;
	}
	
	/**
	* 删除屏蔽词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id){
		maskRepository.delete(id);
		return new RespBody<String>();
	}
	
	/**
	* 删除多条屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@PostMapping("/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<MaskWord> entities){
		maskRepository.delete(entities);
		return new RespBody<String>();
	}
	
	@PostMapping("/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value="order",defaultValue="desc") String order,
			@RequestParam(value="sort",defaultValue="updateAt")String sort,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			@RequestBody MaskWord condition){
		Order ordernew=new Order(Sort.Direction.DESC, "updateAt");;
		if("desc".equals(order)){
			ordernew=new Order(Sort.Direction.DESC, "updateAt");
		}
		else if("asc".equals(order)){
			ordernew=new Order(Sort.Direction.ASC, "updateAt");
		}
		
		Sort sortnew=new Sort(ordernew);			
		Pageable p = new PageRequest(pageNumber-1,pageSize,sortnew);
		Specification<MaskWord> specification = new Specification<MaskWord>() {
			
			@Override
			public Predicate toPredicate(Root<MaskWord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path<String> word = root.get("word");
				if(condition.getWord()!=null && !condition.getWord().equals("")){
					list.add(cb.like(word, "%"+condition.getWord()+"%"));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));	
			}
		};
		RespBody<ListPage> resp = new RespBody<ListPage>();
		Page<MaskWord> findAll = maskRepository.findAll(specification, p);
		resp.setBody(new ListPage(findAll));
		return resp;
	}
	
}
