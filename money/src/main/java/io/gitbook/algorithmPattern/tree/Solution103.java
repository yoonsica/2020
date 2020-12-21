package io.gitbook.algorithmPattern.tree;

import io.gitbook.algorithmPattern.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution103 {

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        List<List<Integer>> list = new ArrayList<>();
        LinkedList<TreeNode> queue = new LinkedList<>();
        LinkedList<Integer> tmpList = new LinkedList<>();
        queue.addLast(root);
        queue.addLast(null);
        boolean isLeft = false;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) {

                if (!isLeft) {
                    tmpList.addLast((Integer) node.val);
                } else {
                    tmpList.addFirst((Integer) node.val);
                }

                if (node.left != null) {
                    queue.addLast(node.left);
                }
                if (node.right != null) {
                    queue.addLast(node.right);
                }
            } else {
                list.add(tmpList);
                tmpList = new LinkedList<>();
                isLeft = !isLeft;
                if (!queue.isEmpty()) {
                    queue.addLast(null);
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        List<List<Integer>> list = new Solution103().zigzagLevelOrder(root);
        list.forEach(System.out::println);
    }
}
