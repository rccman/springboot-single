package com.rcc.test.test.algorithm;

/**
 * 链表反转
 */
public class ReverseListNode {

    /**
     *
     * 循环反转，借助辅助空间，借助游标
     * @param head
     * @return
     */
    public static Node reverseListNode(Node head) {
        //单链表为空或只有一个节点，直接返回原单链表
        if (head == null || head.getNext() == null) {
            return head;
        }
        //前一个节点指针
        Node preNode = null;
        //当前节点指针
        Node curNode = head;
        //下一个节点指针
        Node nextNode = null;

        while (curNode != null) {
            nextNode = curNode.getNext();//nextNode 指向下一个节点
            curNode.setNext(preNode);//将当前节点next域指向前一个节点
            preNode = curNode;//preNode 指针向后移动
            curNode = nextNode;//curNode指针向后移动
        }

        return preNode;
    }

    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
//        Node node = reverseListNode(node1);
        Node node = reverseLinkedList(node1);
        printNodeList(node);


    }

    private static void printNodeList(Node node) {
        System.out.println(node.getData());
        if(node.getNext()!=null){
            printNodeList(node.getNext());
        }

    }

    /**
     * 递归反转
     * @param node
     * @return
     */
    static Node reverseLinkedList(Node node) {
        if (node == null || node.next == null) {
            return node;
        } else {
            Node headNode = reverseLinkedList(node.next);
            node.next.next = node;
            node.next = null;
            return headNode;
        }
    }
}
