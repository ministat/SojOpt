package org.example;

import org.javatuples.Triplet;

public abstract class CachedSojNvlImpl implements ISojNvl{
    protected boolean _useCache = false;
    protected ThreadLocal<Triplet<String, String, String>> _cache = new ThreadLocal<Triplet<String, String, String>>();

    public CachedSojNvlImpl() {
        this(false);
    }
    public CachedSojNvlImpl(boolean useCache) {
        _useCache = useCache;
    }
}
