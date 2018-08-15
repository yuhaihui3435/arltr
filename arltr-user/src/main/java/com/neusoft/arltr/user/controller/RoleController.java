package com.neusoft.arltr.user.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.constant.Constant;
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.Orgnazation;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.utils.TreeNode;
import com.neusoft.arltr.common.utils.TreeNodeBuilder;
import com.neusoft.arltr.common.utils.TreeNodeUtil;
import com.neusoft.arltr.user.repository.OrgnazationRepository;
import com.neusoft.arltr.user.repository.RoleRepository;
import com.neusoft.arltr.user.repository.UserRepository;

/**
 * 角色管理控制器
 * 
 * @author lishuang
 *
 */
@RestController
@RequestMapping("/user/role")
public class RoleController {
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrgnazationRepository orgnazationRepository;
	
	
	/**
	* 根据id获取词条
	* 
	* @param id 主键
	* @return 词条实体类
	*/
	@PostMapping("/gettree")
	public RespBody<List<TreeNode>> getUserRoleTree() {
		Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
		Specification<Role> specification = new Specification<Role>(){
            @Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}};
		RespBody<List<TreeNode>> resp = new RespBody<List<TreeNode>>();
		List<Role> rolelist = new ArrayList<Role>() ;
		Role role=new Role();
		role.setId(-1);
		role.setName("角色管理");
		role.setParentId(null);
		rolelist.add(role);
		role.setChildren((List<Role>)roleRepository.findAll(specification, sort));
        List<TreeNode> TreeNodeList =TreeNodeUtil.entityTreeToNodeTree(rolelist, true, 
				new TreeNodeBuilder<Role>() {
			@Override
					public TreeNode createTreeNode(Role entity) {
						
						TreeNode node=new TreeNode();
				        node.setId(entity.getId().toString());
				        if(("-1").equals(entity.getId().toString())){
				        	node.setPid("");
				        	node.setState("open");
				        }
				        else{
				        	node.setPid("-1");
				        }
				        node.setText(entity.getName());
				        return node;
					}
				}
			);
        resp.setBody(TreeNodeList);
		return resp;
	}
	
	/**
	* 保存角色
	* 
	* @param entity 角色实体类
	* @return 返回信息
	*/
	@PostMapping("/save")
	public RespBody<Role> saveLexicon(@RequestBody Role entity){
		RespBody<Role> resp = new RespBody<Role>();
		Role content = new Role();
		try{ 
			content = roleRepository.save(entity);
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("角色：["+entity.getName()+"]重复!");
		}
		resp.setBody(content);
		return resp;
	}
	
	/**
	* 删除角色
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id){
		try{ 
			roleRepository.delete(id);
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("该角色下已经有人员配置，不能删除");
		}
        return new RespBody<String>();
	}
	
	/**
	 * 人员组织机构树
	 * @return
	 */
	@PostMapping("/getorgusertree")
	public RespBody<List<TreeNode>> getOrgUserTree(@RequestParam(value="roleId") Integer roleId) {
		RespBody<List<TreeNode>> resp = new RespBody<List<TreeNode>>();
		List<Orgnazation> orglist = orgnazationRepository.findByParentOrgCodeOrderByOrgOrder(Constant.TREE_ROOT_CODE);
//		List<Orgnazation> orglist = new ArrayList<Orgnazation>() ;
//		Orgnazation org=new Orgnazation();
//		org.setId(-1);
//		org.setName("总部");
//		orglist.add(org);
//		org.setChildren((List<Orgnazation>)orgnazationRepository.findAll());
		List<TreeNode> TreeNodelist= TreeNodeUtil.entityTreeToNodeTree(orglist, true, 
				new TreeNodeBuilder<Orgnazation>() {
			
					@Override
					public TreeNode createTreeNode(Orgnazation entity) {
						
						TreeNode node=new TreeNode();
				        node.setId(entity.getCode());
//				        if(("-1").equals(entity.getId().toString())){
//				        	node.setPid("");
//				        	node.setState("open");
//				        	node.setText("总部");
//				        	node.setId("-1");
//				        }
//				        else{
//				        	node.setPid("-1");
//				        }
				        node.setPid(entity.getParentOrgCode());
				        node.setText(entity.getName());
				        
				        return node;
					}
				}
			);
		setOrgUsers(TreeNodelist,roleId);
		resp.setBody(TreeNodelist);
		return resp;
	}
	@PostMapping("/getorgtree")
	public RespBody<List<TreeNode>> getOrgTree() {
		RespBody<List<TreeNode>> resp = new RespBody<List<TreeNode>>();
		List<Orgnazation> orglist = orgnazationRepository.findByParentOrgCodeOrderByOrgOrder(Constant.TREE_ROOT_CODE);
//		List<Orgnazation> orglist = new ArrayList<Orgnazation>() ;
//		Orgnazation org=new Orgnazation();
//		org.setId(-1);
//		org.setName("总部");
//		orglist.add(org);
//		org.setChildren((List<Orgnazation>)orgnazationRepository.findAll());
		List<TreeNode> TreeNodelist= TreeNodeUtil.entityTreeToNodeTree(orglist, true, 
				new TreeNodeBuilder<Orgnazation>() {
			
					@Override
					public TreeNode createTreeNode(Orgnazation entity) {
						
						TreeNode node=new TreeNode();
				        node.setId(entity.getCode());
//				        if(("-1").equals(entity.getId().toString())){
//				        	node.setPid("");
//				        	node.setState("open");
//				        	node.setId("1");
//				        }
//				        else{
//				        	node.setPid("-1");
//				        }
				        node.setPid(entity.getParentOrgCode());
				        node.setText(entity.getName());
				        
				        return node;
					}
				}
			);
		//setOrgUsers(TreeNodelist);
		resp.setBody(TreeNodelist);
		return resp;
	}
	/**
	 * 获取组织机构方法
	 * 
	 * @param orgCode 组织机构编码
	 * @param roleId 根据角色过滤组织结构树
	 * @return 组织机构的用户节点列表
	 */
	private void setOrgUsers(List<TreeNode> orgTree,Integer roleId) {
		for (TreeNode org : orgTree) {
			// 设置下属组织机构节点
			if (org.getChildren() != null && org.getChildren().size() > 0) {
				setOrgUsers(org.getChildren(),roleId);
			}
			
			List<TreeNode> orgUserList = getOrgUserTree(org.getId(),roleId);
			
			if (orgUserList.size() > 0) {
				
				if (org.getChildren() == null) {
					org.setChildren(new ArrayList<TreeNode>());
				}
				
				// 把组织下的用户列表添加到组织节点下
				org.getChildren().addAll(0, orgUserList);
				
				if (org.getState() == null || org.getState().isEmpty()) {
					org.setState("closed");
				}
			}
			
		}
	}
	
	/**
	 * 获取组织机构下的用户节点列表
	 * 
	 * @param orgCode 组织机构编码
	 * @return 组织机构的用户节点列表
	 */
	private List<TreeNode> getOrgUserTree(String orgCode,Integer roleId) {
		List<User> userList = userRepository.findByOrgCode(orgCode,roleId);
		List<TreeNode> orgUserTree = new ArrayList<TreeNode>();
		
		if (userList != null) {
			for (User user : userList) {
				TreeNode node = new TreeNode();
				node.setId(String.valueOf(user.getId()));
		        node.setPid(orgCode);
		        node.setText(user.getEmployeeName());
		        node.addAttribute("isUserNode", true);
		        node.addAttribute("userInfo", user);
		        orgUserTree.add(node);
			}
		}
		return orgUserTree;
	}

	/**
	* 保存角色菜单
	* 
	* @param id 主键
	* @param menus 菜单集合
	* @return 返回信息
	*/
	@PostMapping("/{id}/menus/save")
	public RespBody<String> saveRoleMenus(@PathVariable("id")Integer id, @RequestBody HashSet<Menu> menus){
		Role role=roleRepository.findOne(id);
		role.setMenus(menus);
		roleRepository.save(role);
		
		return new RespBody<String>();
	}
	
	/**
	* 获取角色列表
	* @return 返回信息
	*/
	@PostMapping("/list")
	public RespBody<List<Role>> getRoleList(){
		Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
		Specification<Role> specification = new Specification<Role>(){
            @Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}};
		RespBody<List<Role>> resp = new RespBody<List<Role>>();
		List<Role> roleList=(List<Role>) roleRepository.findAll(specification, sort);
		resp.setBody(roleList);
		return resp;
	}
	/**
	* 根据角色获取菜单
	* @param id 角色id
	* @return 返回信息
	*/
	@PostMapping("/{id}/menus")
	public RespBody<Role> getMenusByRoleId(@PathVariable("id") Integer id){
		RespBody<Role> resp = new RespBody<Role>();
		Role role=roleRepository.findOne(id);
		if (role == null) {
			throw new BusinessException("角色不存在");
		}
		resp.setBody(role);
		return resp;
	}
	
	/**
	 * 保存角色配置用户方法
	 * @param id 角色ID
	 * @param list 用户ID集合
	 * @return
	 */
	@PostMapping("/roleusersave")
	@Transactional
	public RespBody<Object> save(@RequestParam(value="id") Integer id,@RequestParam(value="useridlist", defaultValue = "0") List<Integer> list){
		RespBody<Object> rep=new RespBody<Object>();
		roleRepository.deleteByRoleId(id);
		for(int i=0;i<list.size();i++){
			if(list.get(i)!=0){
				roleRepository.insert(list.get(i), id);
			}
		}
		return rep;
	}
	
	/**
	 * 根据角色Id 获取拥有该角色的用户
	 * @param id 角色ID
	 * @return
	 */
	@PostMapping("/rolerelauser")
	public RespBody<List<User>> getRoleRelaUser(@RequestParam(value="id") Integer id){
		RespBody<List<User>> rep=new RespBody<List<User>>();
		List<User> list=userRepository.getRoleRelaUser(id);
		rep.setBody(list);
		return rep;
	}
}
