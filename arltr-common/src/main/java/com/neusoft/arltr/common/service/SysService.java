/**
 * 
 */
package com.neusoft.arltr.common.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.SysAuth;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.utils.TreeNode;

/**
 * 系统管理服务接口类
 * 
 *
 *
 */
@FeignClient("arltr-user")
public interface SysService {

	/**
	 * 获取指定用户信息
	 * 
	 * @param id 用户id
	 * 
	 * @return RespBody<User>
	 */
	@GetMapping("/user/{id}")
	public RespBody<User> getUser(@PathVariable("id") Integer id);
	/**
	 * 根据用户id或者其他登录信息获取人员
	 * @param id
	 * @return
	 */
	@GetMapping("/user/login/validate/{id}")
	public RespBody<User> getUserOfUserIdOrOtherId(@PathVariable("id") String id);
	
	/**
	* 返回左侧的角色树
	* 
	* @return 角色列表
	*/
	@PostMapping("/user/role/gettree")
	public RespBody<List<TreeNode>> getUserRoleTree();

	/**
	* 保存用户信息
	* 
	* @param user 用户实体类
	* @return 返回信息
	*/
	@PostMapping("/user/save")
	public RespBody<User> saveUser(@RequestBody User user);
	/**
	 * 根据用户权限过滤菜单显示
	 * @param user
	 * @return
	 */
	@PostMapping("/user/ofuser/{userid}")
	public List<Menu> getMenusOfUser(@PathVariable("userid") Integer userid);
	/**
	 * 根据 菜单id获取菜单列表
	 * @param menuId
	 * @return
	 */
	@GetMapping("/user/getmenu/{menuId}")
	public Menu getMenu(@PathVariable("menuId") Integer menuId);
	/**
	* 保存角色
	* 
	* @param entity 角色实体类
	* @return 返回信息
	*/
	@PostMapping("/user/role/save")
	public RespBody<Role> save(@RequestBody Role entity);
	
	/**
	* 删除角色
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/user/role/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id);
	

	/**
	 * 人员组织机构树
	 * @return
	 */
	@PostMapping("/user/role/getorgusertree")
	public RespBody<List<TreeNode>> getOrgUserTree(@RequestParam(value="roleId",defaultValue = "0") Integer roleId);
	/**
	 * 组织机构树
	 * @return
	 */
	@PostMapping("/user/role/getorgtree")
	public RespBody<List<TreeNode>> getOrgTree();
	/**
	 * 获取访问权限
	 * @return
	 */
	@PostMapping("/user/sysauth/getsysauth")
	public RespBody<List<SysAuth>> getSysAuth();
	/**
	 * 根据权限Id 获取拥有该权限的用户
	 * @param id 权限ID
	 * @return
	 */
	@PostMapping("/user/sysauth/usersysauth")
	public RespBody<List<User>> getUserSysAuth(@RequestParam(value="id") Integer id);
	/**
	 * 根据用户id 返回用户具有的所有权限
	 * @param id 用户id
	 * @return
	 */
	@PostMapping("user/sysauth/sysauthall")
	public RespBody<List<SysAuth>> getAll(@RequestParam(value="id") Integer id);
	/**
	 * 保存用户权限方法
	 * @param id 权限ID
	 * @param list 用户ID集合
	 * @return
	 */
	@PostMapping("/user/sysauth/save")
	public RespBody<Object> save(@RequestParam(value="id") Integer id,@RequestParam(value="useridlist", defaultValue = "0") List<Integer> list);
	/**
	* 根据角色获取菜单
	* @param id 角色id
	* @return 返回信息
	*/
	@PostMapping("/user/role/{id}/menus")
	public RespBody<Role> getMenusByRoleId(@PathVariable("id") Integer id);
	/**
	* 保存角色菜单
	* @param id 角色id
	* @param menus 菜单集合
	* @return 返回信息
	*/
	@PostMapping("/user/role/{id}/menus/save")
	public RespBody<String> saveRoleMenus(@PathVariable("id") Integer id,@RequestBody HashSet<Menu> menus);
	/**
	 * 获取菜单树
	 * @param 菜单id
	 * @param role 角色
	 * @return RespBody<List<Menu>>
	 */
	@PostMapping("/user/menus/{parentId}")
	public RespBody<List<Menu>> findByParentId(@PathVariable("parentId") Integer parentId,@RequestBody Role role);
	/**
	* 返回左侧的角色树
	* 
	* @return 角色列表
	*/
	@PostMapping("/user/role/list")
	public RespBody<List<Role>> getUserRoleList();
	/**
	 * 用户查询方法
	 * @param pageNumber
	 * @param pageSize
	 * @param user
	 * @return
	 */
	@PostMapping("user/query")
	public RespBody<ListPage> search(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,User user);

	/**
	 * 根据角色Id 获取拥有该角色的用户
	 * @param id 权限ID
	 * @return
	 */
	@PostMapping("/user/role/rolerelauser")
	public RespBody<List<User>> getRoleRelaUser(@RequestParam(value="id") Integer id);
	
	/**
	 * 保存角色用户方法
	 * @param id 角色ID
	 * @param list 用户ID集合
	 * @return
	 */
	@PostMapping("/user/role/roleusersave")
	public RespBody<Object> roleSave(@RequestParam(value="id") Integer id,@RequestParam(value="useridlist", defaultValue = "0") List<Integer> list);
	
	/**
	 * 根据枚举类型获取枚举列表
	 * 
	 * @param type 枚举类型
	 * @return 枚举列表
	 */
	@PostMapping("/enumeration/list")
	public RespBody<List<Enumeration>> getListByType(@RequestParam("type") String type);
	
	/**
	 * 判断用户是否被授予访问权
	 * 
	 * @param source 访问资源id
	 * @param originalId 源系统id
	 * @param user 当前用户
	 */
	@PostMapping("/user/sysauth/verification")
	public RespBody<Boolean> isUserAuthorized(@RequestParam("source") Integer source, @RequestParam("originalId") String originalId
			, @RequestBody User user);
	
}
