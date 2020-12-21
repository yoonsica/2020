package io.gitbook.algorithmPattern.tree;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * @author shengli
 * 二叉树
 */
public class BinaryTree {
    /**
     * 前序遍历(递归)
     * @param root
     */
    public <T> List<T> preOrderRec(TreeNode<T> root){
        List<T> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        list.add(root.val);
        list.addAll(preOrderRec(root.left));
        list.addAll(preOrderRec(root.right));
        return list;
    }

    /**
     * 前序遍历（非递归）
     * @param root
     */
    public <T> List<T> preOrder(TreeNode<T> root) {
        if (root == null) {
            return null;
        }
        //在Java中，当对象作为参数传递时，实际上传递的是一份“引用的拷贝”。
        //不用担心root会被改变，此处的root是外部root的一个引用的拷贝，改变这个root的引用不会改变外部root的引用
        //但是直接改变root的值是会改变外部root对应结点的值
        Stack<TreeNode<T>> stack = new Stack();
        List<T> list = new ArrayList<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                //System.out.print(node.val);
                list.add(root.val);
                stack.push(root);
                root = root.left;
            } else {
                root = stack.pop().right;
            }
        }
        return list;
    }

    /**
     * 中序遍历（递归）
     * @param root
     */
    public <T> List<T> midOrderRec(TreeNode<T> root){
        List<T> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        list.addAll(midOrderRec(root.left));
        list.add(root.val);
        list.addAll(midOrderRec(root.right));
        return list;
    }

    /**
     * 中序遍历 非递归
     * @param root
     */
    public <T> List<T> midOrder(TreeNode<T> root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode<T>> stack = new Stack<>();
        List<T> list = new ArrayList<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                root = root.left;
            } else {
                root = stack.pop();
                list.add(root.val);
                root = root.right;
            }
        }
        return list;
    }

    /**
     * 后序遍历 递归
     * @param root
     */
    public <T> List<T> lastOrderRec(TreeNode<T> root){
        List<T> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        list.addAll(lastOrderRec(root.left));
        list.addAll(lastOrderRec(root.right));
        list.add(root.val);
        return list;
    }

    /**
     * 后序遍历非递归
     * @param root
     */
    public <T> List<T> lastOrder(TreeNode<T> root) {
        if (root == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        TreeNode<T> lastVisited = null;
        Stack<TreeNode<T>> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                root = root.left;
            } else {
                //没有左孩子了
                TreeNode<T> node = stack.peek();
                if (node.right == null || node.right.equals(lastVisited)) {
                    //没有右结点，或者右结点刚被访问过
                    list.add(stack.pop().val);
                    lastVisited = node;
                } else {
                    root = node.right;
                }
            }
        }
        //双栈法 前序遍历的逆序
        /*LinkedList<T> list = new LinkedList<>();
        Stack<TreeNode<T>> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                list.addFirst(root.val);
                root = root.right;


            } else {
                root = stack.pop();
                root = root.left;
            }
        }*/
        return list;
    }

    /**
     * 利用前序和中序序列生成二叉树
     * @param preOder
     * @param midOrder
     * @param <T>
     * @return 二叉树根结点
     */
    public <T> TreeNode createTreeByPreAndMidOrder(List<T> preOder, List<T> midOrder) {
        if (CollectionUtils.isEmpty(preOder) || CollectionUtils.isEmpty(midOrder)) {
            return null;
        }
        return createTreeByPreAndMidOrder(preOder, 0, preOder.size() - 1, midOrder, 0, midOrder.size() - 1);
    }

    /**
     * 1234567 3241657
     * 利用前序和中序序列递归创建二叉树
     * @param preOder 前序序列
     * @param preStart 前序序列开始
     * @param preEnd 前序序列结束
     * @param midOrder 中序序列
     * @param midStart 中序序列开始
     * @param midEnd 中序序列结束
     * @return 二叉树根结点
     */
    private <T> TreeNode<T> createTreeByPreAndMidOrder(List<T> preOder, int preStart, int preEnd,
                                                    List<T> midOrder, int midStart, int midEnd) {
        if (preStart > preEnd || midStart > midEnd) {
            return null;
        }
        //前序序列第一个结点为根结点
        TreeNode<T> root = new TreeNode<>(preOder.get(preStart));
        int midPos;
        //在中序序列定位根结点位置
        for (midPos = midStart; midPos <= midEnd; midPos++) {
            if (midOrder.get(midPos).equals(root.val)) {
                break;
            }
        }
        //构造左子树，结束位置可根据具体序列推出，起始位置+左子树结点个数，注意不要包含根结点了
        root.left = createTreeByPreAndMidOrder(preOder, preStart + 1, preStart + midPos - midStart,
                midOrder, midStart, midPos - 1);
        //构造右子树，前序序列起点为左子树结束位置+1即可
        root.right = createTreeByPreAndMidOrder(preOder, preStart + midPos - midStart + 1, preEnd,
                midOrder, midPos + 1, midEnd);
        return root;
    }

    /**
     * 层次遍历
     */
    public <T> List<T> levelOrder(TreeNode<T> root) {
        if (root == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            root = queue.poll();
            list.add(root.val);
            if (root.left != null) {
                queue.offer(root.left);
            }
            if (root.right != null) {
                queue.offer(root.right);
            }
        }
        return list;
    }

    /**
     * 深度搜索 从上到下
     * @param root
     * @param list
     * @param <T>
     */
    public <T> void dfs(TreeNode<T> root,List<T> list) {
        if (root == null) {
            return;
        }
        list.add(root.val);
        dfs(root.left, list);
        dfs(root.right, list);
    }

    /**
     * 深度搜索 从下到上
     * @param root
     * @param <T>
     * @return
     */
    public <T> List<T> dfs(TreeNode<T> root) {
        List<T> list = new ArrayList<>();
        if (root != null) {
            List<T> left = dfs(root.left);
            List<T> right = dfs(root.right);
            list.add(root.val);
            list.addAll(left);
            list.addAll(right);
        }
        return list;
    }
}
