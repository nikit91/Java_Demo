package upb.misc;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Comparable<TreeNode<T>>{

	T data;
	TreeNode<T> parent;
	List<TreeNode<T>> children;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
	}
	
	public void sortChildren() {
		this.children.sort(null);
	}

	public void addChild(TreeNode<T> child) {
		this.children.add(child);
	}

	public void addParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}

	@Override
	public int compareTo(TreeNode<T> arg0) {
		return this.data.toString().compareTo(arg0.data.toString());
	}

}