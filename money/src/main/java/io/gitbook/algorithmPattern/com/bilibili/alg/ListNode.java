package io.gitbook.algorithmPattern.com.bilibili.alg;

/**
 * @Classname ListNode
 * @Description TODO
 * @Date 2021/6/8 下午9:22
 * @Author shengli
 */
public class ListNode<T> {
    T val;
    ListNode<T> next;

    public ListNode(T val, ListNode<T> next) {
        this.val = val;
        this.next = next;
    }

    public ListNode(T val) {
        this.val = val;
    }
}
