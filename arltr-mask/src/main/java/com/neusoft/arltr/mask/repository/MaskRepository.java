
package com.neusoft.arltr.mask.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.neusoft.arltr.common.entity.mask.MaskWord;

/** 
* 类功能描述:
* @author 作者: 
* @version 创建时间：2017年6月22日 下午2:24:15 
*  
*/
public interface MaskRepository extends PagingAndSortingRepository<MaskWord, Integer>,JpaSpecificationExecutor<MaskWord> {
}
