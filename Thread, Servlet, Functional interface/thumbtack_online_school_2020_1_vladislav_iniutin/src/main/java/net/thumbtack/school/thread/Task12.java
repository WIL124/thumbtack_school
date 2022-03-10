package net.thumbtack.school.thread;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


//Все методы, вносящие в экземпляр класса закрываются для записи (lock.writeLock.lock)
//Прочие методы закрываются для чтения (lock.readLock.lock)
//Получаем класс-обёртку над классом HashMap

class MyConcurrentHashMap <K,V>{
    private final HashMap<K,V> hashMap;
    static ReadWriteLock lock = new ReentrantReadWriteLock();

    public MyConcurrentHashMap(int initialCapacity, float loadFactor) {
        hashMap = new HashMap<>(initialCapacity, loadFactor);
    }

    public MyConcurrentHashMap(int initialCapacity) {
        hashMap = new HashMap<>(initialCapacity);
    }

    public MyConcurrentHashMap() {
        hashMap = new HashMap<>();
    }

    public MyConcurrentHashMap(Map<K,V> m) {
        hashMap = new HashMap<>(m);
    }

    public int size() {
        lock.readLock().lock();
        try {
            return hashMap.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return hashMap.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Object get(Object key) {
        lock.readLock().lock();
        try {
            return hashMap.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(Object key) {
        lock.readLock().lock();
        try {
            return hashMap.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            System.out.println("put " + value + " into key " + key);
            return hashMap.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void putAll(Map<K,V> m) {
        lock.writeLock().lock();
        try {
            hashMap.putAll(m);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V remove(K key) {
        lock.writeLock().lock();
        try {
            return hashMap.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            hashMap.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsValue(V value) {
        lock.readLock().lock();
        try {
            return hashMap.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            return hashMap.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Collection<V> values() {
        lock.readLock().lock();
        try {
            return hashMap.values();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Map.Entry<K,V>> entrySet() {
        lock.readLock().lock();
        try {
            return hashMap.entrySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    public V getOrDefault(K key, V defaultValue) {
        lock.readLock().lock();
        try {
            return hashMap.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V putIfAbsent(K key, V value) {
        lock.writeLock().lock();
        try {
            return hashMap.putIfAbsent(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean remove(K key, V value) {
        lock.writeLock().lock();
        try {
            return hashMap.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean replace(K key, V oldValue, V newValue) {
        lock.writeLock().lock();
        try {
            return hashMap.replace(key, oldValue, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Object replace(K key, V value) {
        lock.writeLock().lock();
        try {
            return hashMap.replace(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction) {
        lock.readLock().lock();
        try {
            return hashMap.computeIfAbsent(key, mappingFunction);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V computeIfPresent(K key, BiFunction<? super K,? super V, ? extends V> remappingFunction) {
        lock.readLock().lock();
        try {
            return hashMap.computeIfPresent(key, remappingFunction);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V compute(K key, BiFunction<? super K,? super V, ? extends V> remappingFunction) {
        lock.readLock().lock();
        try {
            return hashMap.compute(key, remappingFunction);
        } finally {
            lock.readLock().unlock();
        }
    }

    public V merge(K key, V value, BiFunction<? super V,? super V, ? extends V> remappingFunction) {
        lock.readLock().lock();
        try {
            return hashMap.merge(key, value, remappingFunction);
        } finally {
            lock.readLock().unlock();
        }
    }
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.writeLock().lock();
        try {
            hashMap.forEach(action);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.writeLock().lock();
        try {
            hashMap.replaceAll(function);
        }finally {
            lock.writeLock().unlock();
        }
    }


    public Object clone() {
        lock.readLock().lock();
        try {
            return hashMap.clone();
        }finally {
            lock.readLock().unlock();
        }
    }
}

public class Task12 {
    static public void main(String[] args){
        MyConcurrentHashMap<Integer,Integer> map = new MyConcurrentHashMap<>();
        // в метод put была добавлена строка вывода для теста
        Runnable putOne = () -> {
            map.put(1, 1);
            System.out.println(map.values());
        };
        Runnable putTwo = () -> {
            map.put(1,2);
            System.out.println(map.values());
        };
        Runnable read = () -> {
          System.out.println(map.values());
        };
        new Thread(putOne).start();
        new Thread(putTwo).start();
        new Thread(putOne).start();
        new Thread(read).start();
        new Thread(putTwo).start();
        new Thread(putOne).start();
        new Thread(putTwo).start();
        new Thread(read).start();
        new Thread(putTwo).start();
        new Thread(putOne).start();
        new Thread(putTwo).start();
        new Thread(read).start();
    }
}
