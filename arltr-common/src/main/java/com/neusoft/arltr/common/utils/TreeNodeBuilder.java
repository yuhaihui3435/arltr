/**
 * 
 */
package com.neusoft.arltr.common.utils;

/**
 * EasyUI树节点创建接口
 * 
 *
 *
 */
public interface TreeNodeBuilder<T> {

	/**
	 * 根据实体类属性创建新的树节点
	 * 
	 * @param <T> 实体类
	 * @return 树节点
	 */
	public TreeNode createTreeNode(T entity);
}
