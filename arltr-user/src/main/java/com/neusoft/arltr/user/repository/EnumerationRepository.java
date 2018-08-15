/**
 * 
 */
package com.neusoft.arltr.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.neusoft.arltr.common.entity.user.Enumeration;


/**
 * 枚举表仓库接口
 * 
 * @author zhanghaibo
 *
 */
public interface EnumerationRepository extends PagingAndSortingRepository<Enumeration, Integer> {
	
	@Query(value="select * from management_enumeration where type=?1",nativeQuery=true)
	public List<Enumeration> getEnumerationList(String type);

}
