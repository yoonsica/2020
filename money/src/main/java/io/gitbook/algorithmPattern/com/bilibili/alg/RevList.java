package io.gitbook.algorithmPattern.com.bilibili.alg;

/**
 * @Classname RevList
 * @Description 反转单链表
 * @Date 2021/6/8 下午4:02
 * @Author shengli
 */
public class RevList {
    public static void main(String[] args) {
        ListNode<Integer> node1 = new ListNode<>(1);
        ListNode<Integer> node2 = new ListNode<>(2);
        ListNode<Integer> node3 = new ListNode<>(3);
        ListNode<Integer> node4 = new ListNode<>(4);
        ListNode<Integer> node5 = new ListNode<>(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        System.out.println("初始链表：");
        printList(node1);
        RevList revList = new RevList();
        System.out.println("反转后：");
        //printList(revList.iterate(node1));
        printList(revList.recursion(node1));
    }

    /**
     * 打印链表
     * @param head
     * @param <T>
     */
    public static <T> void printList(ListNode<T> head) {
        while (head != null && head.next != null) {
            System.out.print(head.val+"->");
            head = head.next;
        }
        if (head != null) {
            System.out.print(head.val);
        }
    }

    /**
     * 循环方式逆转链表
     * @param head
     * @param <T>
     * @return
     */
    public <T> ListNode<T> iterate(ListNode<T> head){
        ListNode<T> prev = null,next;
        while (head != null) {
            next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    /**
     * 递归方式逆转链表
     * @param head
     * @param <T>
     * @return
     */
    public <T> ListNode<T> recursion(ListNode<T> head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode<T> new_head = recursion(head.next);
        head.next.next = head;
        head.next = null;
        return new_head;
    }
}
