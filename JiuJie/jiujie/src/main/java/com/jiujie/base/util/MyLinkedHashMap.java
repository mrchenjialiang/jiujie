package com.jiujie.base.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 6918023506928428613L;
    private int maxSize;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        if (maxSize == 0) {
            return false;
        }
        return size() > maxSize;
    }
}
