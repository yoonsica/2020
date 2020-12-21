package io.gitbook.algorithmPattern.tree;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    TreeNode<Character> root;
    BinaryTree binaryTree;
    @BeforeEach
    void setUp() {
        binaryTree = new BinaryTree();
        List<Character> nodeList = Arrays.asList('A','B','D','G','H','C','E','I','F');
        List<Character> midList = Arrays.asList('G', 'D', 'H', 'B', 'A', 'E', 'I', 'C', 'F');
        root = binaryTree.createTreeByPreAndMidOrder(nodeList, midList);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void preOrderRec() {
        List<Character> list = binaryTree.preOrderRec(root);
        list.forEach(System.out::print);
        assertEquals(list,Arrays.asList('A','B','D','G','H','C','E','I','F'));
    }

    @Test
    void preOrder() {
        List<Character> list = binaryTree.preOrder(root);
        list.forEach(System.out::print);
        assertEquals(list,Arrays.asList('A','B','D','G','H','C','E','I','F'));
    }


    @Test
    void createTreeByPreAndMidOrder() {

    }

    @Test
    void midOrderRec() {
        //List<Character> list = binaryTree.midOrderRec(root);
    }

    @Test
    void midOrder() {
        binaryTree.midOrder(root);
    }

    @Test
    void lastOrderRec() {
        assertEquals(binaryTree.lastOrderRec(root),Arrays.asList('G','H','D','B','I','E','F','C','A'));
    }

    @Test
    void lastOrder() {
        assertEquals(binaryTree.lastOrder(root),Arrays.asList('G','H','D','B','I','E','F','C','A'));
    }

    @Test
    void levelOrder() {
        assertEquals(binaryTree.levelOrder(root),Arrays.asList('A','B','C','D','E','F','G','H','I'));
    }
}