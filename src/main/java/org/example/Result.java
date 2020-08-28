package org.example;

public class Result implements IResult {
    private int _startIndex;
    private int _endIndex;
    private long _strSz;
    private long _durationMs;
    Result(int s, int e, long sz, long durMs) {
        _startIndex = s;
        _endIndex = e;
        _strSz = sz;
        _durationMs = durMs;
    }

    @Override
    public int startIndex() {
        return _startIndex;
    }

    @Override
    public int endIndex() {
        return _endIndex;
    }

    @Override
    public long stringSize() {
        return _strSz;
    }

    @Override
    public long durationMs() {
        return _durationMs;
    }
}
