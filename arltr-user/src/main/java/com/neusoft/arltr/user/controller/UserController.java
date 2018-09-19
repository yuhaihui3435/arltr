/**
 * 
 */
package com.neusoft.arltr.user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.user.repository.EnumerationRepository;
import com.neusoft.arltr.user.repository.UserRepository;
import com.neusoft.arltr.user.service.MenuService;

/**
 * 用户信息控制器
 * 
 *
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	EnumerationRepository enumerationRepository;
	@Autowired
	MenuService menuService;
	
	@GetMapping("/{id}")
	public RespBody<User> getUser(@PathVariable Integer id) {
		
		RespBody<User> resp = new RespBody<User>();
		User user = userRepository.findOne(id);
		
		if (user == null) {
			throw new BusinessException("用户不存在");
		}
		
		resp.setBody(userRepository.findOne(id));
		
		return resp;
	}
	
	@PostMapping("/save")
	public RespBody<User> saveUser(@RequestBody User user) {
		RespBody<User> resp=new RespBody<User>();
		user.setUpdateAt(new Date());
		User entity=userRepository.save(user);
		resp.setBody(entity);
		return resp;
	}
	/**
	 * 用户查询方法
	 * @param pageNumber
	 * @param pageSize
	 * @param user
	 * @return
	 */
	@PostMapping("/query")
	public RespBody<ListPage> search(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestBody User user){
		RespBody<ListPage> rep=new RespBody<ListPage>();
		Order ordernew=new Order(Sort.Direction.DESC, "updateAt");;
		if("desc".equals(order)){
			ordernew=new Order(Sort.Direction.DESC, "updateAt");
		}
		else if("asc".equals(order)){
			ordernew=new Order(Sort.Direction.ASC, "updateAt");
		}
		
		Sort sort=new Sort(ordernew);
		Pageable pageable = new PageRequest(pageNumber-1, pageSize,sort);
		Specification<User> specification = new Specification<User>(){

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//Join<User, Organization> t= root.join("organization", JoinType.LEFT);				
				Path<String> userName = root.get("userName");
				Path<String> employeeName = root.get("employeeName");
				Path<String> employeeNo = root.get("employeeNo");
				Path<String> employeeTitle = root.get("employeeTitle");
				Path<String> employeeOrg = root.get("employeeOrg");
				if(user.getUserName()!=null && !user.getUserName().equals("")){
					list.add(cb.like(userName, "%"+user.getUserName()+"%"));
				//	sb.append("and  user.loginId="+user.getLoginId());
				}
				if(user.getEmployeeName()!=null && !user.getEmployeeName().equals("")){
					list.add(cb.like(employeeName, "%"+user.getEmployeeName()+"%"));
				//	sb.append("and  user.name  like "+ "%"+user.getName()+"%");
				}
				if(user.getEmployeeNo()!=null && !user.getEmployeeNo().equals("")){
					list.add(cb.like(employeeNo, "%"+user.getEmployeeNo()+"%"));
				}
				if(user.getEmployeeTitle()!=null && !user.getEmployeeTitle().equals("")){
					list.add(cb.equal(employeeTitle, user.getEmployeeTitle()));
				}
				if(user.getEmployeeOrg()!=null && !user.getEmployeeOrg().equals("")){
					list.add(cb.equal(employeeOrg, user.getEmployeeOrg()));
				}
				
				//查询是否离职不为1的数据
				list.add(cb.or(cb.notEqual(root.get("endFlag"), "1"), cb.isNull(root.get("endFlag"))));
				
				//list.add(cb.equals(t.get("orgid")));
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}};
			Page<User> p=userRepository.findAll(specification,pageable);
//			List<Enumeration> securitylist=enumerationRepository.getEnumerationList("SECURITY");
//			List<Enumeration> dutylist=enumerationRepository.getEnumerationList("TITLE");
//			List<User> list=p.getContent();
//			for(Enumeration e:securitylist){
//				 for(User u:list){
//					 if(e.getValue().equals(String.valueOf(u.getSecurityClass()))){
//						u.setSecurityClassName(e.getValueName());
//					 }
//				 }
//			 }
//			for(Enumeration e:dutylist){
//				 for(User u:list){
//					 if(e.getValue().equals(u.getEmployeeTitle())){
//						u.setEmployeeTitleName(e.getValueName());
//					 }
//				 }
//			 }
			rep.setBody(new ListPage(p));
		return rep;
	
	}
	
	@PostMapping("/ofuser/{userid}")
	public List<Menu> getMenusOfUser(@PathVariable Integer userid){
		User user = userRepository.findOne(userid);
		return menuService.getMenusOfUser(user);
	}
	@GetMapping("/getmenu/{menuId}")
	public Menu getMenu(@PathVariable("menuId") Integer menuId){
		return menuService.getMenu(menuId);
	}
	@GetMapping("/login/validate/{id}")
	public RespBody<User> getUserOfUserIdOrOtherId(@PathVariable("id") String id){
		RespBody<User> resp = new RespBody<User>();
		User user = userRepository.findByUserNameOrEmployeeNo(id, id);
		if (null != user) {
			resp.setBody(user);
		}
		return resp;
	}
}
