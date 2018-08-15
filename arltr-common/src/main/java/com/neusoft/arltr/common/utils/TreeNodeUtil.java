package com.neusoft.arltr.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.arltr.common.constant.Constant;

/**
 * tree节点转换工具类
 * @author neusoft
 *
 */
public class TreeNodeUtil {

    /**
	 * 将实体树转化为easyUI的tree
	 * 
     * @param <T> 实体类型
	 * @param entityTree 实体树
	 * @param includeChildren 是否包含子节点
	 * @param nodeBuilder 节点创建接口
	 * 
	 * @return easyUI树结构
	 */
    @SuppressWarnings("unchecked")
	public static <T> List<TreeNode> entityTreeToNodeTree(List<T> entityTree, boolean includeChildren,
			TreeNodeBuilder<T> nodeBuilder) {
		
		List<TreeNode> tree=new ArrayList<TreeNode>();
		
		try {
			
			for (T entity : entityTree) {
				
				// 回调创建节点方法创建新节点
				TreeNode node = nodeBuilder.createTreeNode(entity);
				
				// 获取实体的子节点
				List<T> children = (List<T>) entity.getClass().getMethod("getChildren").invoke(entity);
				
				if (children != null && children.size() > 0) {
					if (node.getState() == null || node.getState().isEmpty()) {
						node.setState(Constant.NODE_STATE_CLOSED);
					}
					
					// 加载子节点
					if (includeChildren) {
						node.setChildren(entityTreeToNodeTree(children, includeChildren, nodeBuilder));
					}
				}
				
				tree.add(node);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tree;
	}
}
