package com.github.vizaizai.util.value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liaochongwei
 * @date 2021/2/9 14:08
 */
public class NameValues<K,V> implements Collection<NameValue<K,V>> {
    private final List<NameValue<K,V>> nameValues = new LinkedList<>();

    public List<V> getValues(K name) {
        return nameValues.stream().filter(e-> e.getName().equals(name))
                                  .map(NameValue::getValue)
                                  .collect(Collectors.toList());
    }
    public Set<K> names() {
        return nameValues.stream().map(NameValue::getName).collect(Collectors.toCollection(TreeSet::new));
    }

    public void remove(String name) {
        nameValues.removeIf(e-> e.getName().equals(name));
    }
    public List<NameValue<K,V>> getNameValues() {
        return nameValues;
    }

    @Override
    public int size() {
        return nameValues.size();
    }

    @Override
    public boolean isEmpty() {
        return nameValues.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return nameValues.contains(o);
    }

    @Override
    public Iterator<NameValue<K, V>> iterator() {
        return nameValues.iterator();
    }

    @Override
    public Object[] toArray() {
        return nameValues.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return nameValues.toArray(a);
    }

    @Override
    public boolean add(NameValue<K, V> nameValue) {
        return nameValues.add(nameValue);
    }

    @Override
    public boolean remove(Object o) {
        return nameValues.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return nameValues.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends NameValue<K, V>> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        return nameValues.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return nameValues.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return nameValues.removeAll(c);
    }

    @Override
    public void clear() {
        nameValues.clear();
    }
}
