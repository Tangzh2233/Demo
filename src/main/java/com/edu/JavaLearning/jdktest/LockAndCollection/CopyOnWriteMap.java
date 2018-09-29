package com.edu.JavaLearning.jdktest.LockAndCollection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tangzhihao
 * @date 2018/5/10
 */

public class CopyOnWriteMap<K,V> implements Map<K,V>,Cloneable,Serializable{

    private static final long serialVersionUID = 4465801184357678014L;
    private volatile Map<K,V> map;
    final transient ReentrantLock lock = new ReentrantLock();

    public CopyOnWriteMap(){
        map = new HashMap<K,V>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    /**
     * @author: Tangzhihao
     * @date: 2018/5/10 14:4
     * @description:1-->复制新map-->在新map中添加-->引用指向新map
     */
    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            Map<K,V> newMap = new HashMap<K,V>(map);
            V val = newMap.put(key, value);
            map = newMap;
            return val;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.lock();
        try {
            Map<K,V> newMap = new HashMap<>(map);
            newMap.putAll(m);
            map = newMap;

        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public Map<K, V> getMap() {
        return map;
    }

    public void setMap(Map<K, V> map) {
        this.map = map;
    }
}
