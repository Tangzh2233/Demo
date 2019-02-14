package com.edu.Java基础重温.集合;

import java.util.Map;

/**
 * @Author: tangzh
 * @Date: 2018/12/21$ 3:04 PM$
 **/
public class MyTreeMap<K,V> {


    static class MyTreeNode<K,V> extends MyNode<K,V>{
        MyNode<K,V> parent;
        MyNode<K,V> left;
        MyNode<K,V> right;
        MyNode<K,V> prev;
        boolean red;

        MyTreeNode(int hash, K k, V v, MyNode<K, V> next,MyTreeNode<K,V> parent) {
            super(hash, k, v, next);
            this.parent = parent;
        }

        MyNode<K,V> find(int h,Object key){
            return findTreeNode(h,key);
        }
        final MyTreeNode<K,V> findTreeNode(int hash,Object key){
            return null;
        }

    }



    static class MyNode<K,V> implements Map.Entry<K,V>{
        final int hash;
        final K key;
        volatile V val;
        volatile MyNode<K,V> next;

        MyNode(int hash,K k,V v,MyNode<K,V> next){
            this.hash = hash;
            this.key = k;
            this.val = v;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V value) {
            throw  new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return key.hashCode()^val.hashCode();
        }

        @Override
        public String toString() {
            return key + "=" + val;
        }
    }
}
