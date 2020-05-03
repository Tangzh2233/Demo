package com.edu.JavaLearning.算法;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/3/31 9:58 PM
 * <p>
 * Hash(保证查询复杂度为O(1)) + 链表
 **/
public class LRUCache<K, V> {

    private int maxSize;
    private int size;
    private Node head;
    private Node tail;

    private Map<K, Node> mapData = new ConcurrentHashMap<>();

    public LRUCache(int size) {
        this.maxSize = size;
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.pre = head;
    }

    public V get(K key) {

        Node<K, V> node = mapData.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node node = mapData.get(key);
        if (node == null) {
            Node item = new Node(key, value);
            mapData.put(key, item);
            addNode(item);
            if (size >= maxSize) {
                Node last = removeLast();
                mapData.remove(last.key);
                --size;
            }
            ++size;
        } else {
            node.value = value;
            removeNode(node);
            moveToHead(node);
        }
    }


    private void addNode(Node<K, V> node) {
        node.pre = head;
        node.next = head.next;
        head.next.pre = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        Node pre = node.pre;
        Node next = node.next;

        pre.next = next;
        next.pre = pre;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }

    private Node removeLast() {
        Node pre = tail.pre;
        removeNode(pre);
        return pre;
    }

    public static class Node<K, V> {
        K key;
        V value;
        Node pre;
        Node next;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}


/**
 * 最简单的LRU实现,继承LinkedHashMap,设置accessOrder=true
 * 重写removeEldestEntry方法
 *
 * @param <K>
 * @param <V>
 */
class LRUCacheForLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private int size;

    public LRUCacheForLinkedHashMap(int size) {
        super(size, 0.75f, true);
        this.size = size;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }

}
