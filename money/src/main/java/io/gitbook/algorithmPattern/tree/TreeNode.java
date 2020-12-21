package io.gitbook.algorithmPattern.tree;

import java.util.Stack;

/**
 * 二叉树
 * @author shengli
 */
public class TreeNode<T> {
    T val;
    TreeNode<T> left;
    TreeNode<T> right;

    public TreeNode() {
    }

    public TreeNode(T val) {
        this.val = val;
    }

    public TreeNode(T val, TreeNode<T> left, TreeNode<T> right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }


    public void test(TreeNode<T> root) {
        root.val = null;
    }

    public static void main(String[] args) {
        TreeNode<Character> root = new TreeNode<>('A');
        root.left = new TreeNode<>('B');
        new TreeNode<Character>().test(root);
        System.out.println(root.val);//A or null ?
    }
}

