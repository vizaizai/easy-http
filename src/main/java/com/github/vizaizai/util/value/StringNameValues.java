package com.github.vizaizai.util.value;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author liaochongwei
 * @date 2020/12/22 14:53
 */
public class StringNameValues implements Collection<StringNameValue> {
    private final List<StringNameValue> nameValues = new LinkedList<>();

    public void add(String name, String value) {
        nameValues.add(new StringNameValue(name, value));
    }

    public List<String> getValues(String name) {
        return nameValues.stream().filter(e-> e.getName().equals(name))
                           .map(NameValue::getValue)
                           .collect(Collectors.toList());
    }

    public Set<String> names() {
        return nameValues.stream().map(NameValue::getName).collect(Collectors.toCollection(TreeSet::new));
    }

    public void remove(String name) {
        nameValues.removeIf(e-> e.getName().equals(name));
    }

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

    public void clear() {
        nameValues.clear();
    }

    @Override
    public Iterator<StringNameValue> iterator() {
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
    public boolean add(StringNameValue stringNameValue) {
        return nameValues.add(stringNameValue);
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
    public boolean addAll(Collection<? extends StringNameValue> c) {
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

    public List<StringNameValue> getNameValues() {
        return nameValues;
    }
}
