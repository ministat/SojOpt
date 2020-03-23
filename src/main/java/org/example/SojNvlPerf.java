package org.example;

public class SojNvlPerf extends PerfResource implements IPerfEntry {
    public ISojNvl _sojNvl;
    public int _warmup_iters = 10;
    public int _total_iters = 1000;
    public int _repeated = 1;

    public SojNvlPerf(ISojNvl sojNvl, String patterns[], String values[], int totalIters) {
        _sojNvl = sojNvl;
        VALUES = values;
        KEYS = patterns;
        _total_iters = totalIters;
    }

    // repeat the same string for @r times
    public void setRepeated(int r) {
        _repeated = r;
    }

    private long runPerfCoreRange(int startIdx, int endIdx) {
        long sz = 0;
        for (int k = 0; k < KEYS.length; k++)
        {
            for (int j = startIdx ; j <= endIdx; j++)
            {
                for (int r = 0; r < _repeated; r++) {
                    _sojNvl.getTagValue(VALUES[j], KEYS[k]);
                    sz += VALUES[j].length();
                }
            }
        }
        return sz;
    }

    @Override
    public int totalEntries() {
        return VALUES.length;
    }

    @Override
    public long rangeProcess(int start, int end) {
        long sz = 0;
        for (int i = 0; i < _total_iters; i++) {
            sz += runPerfCoreRange(start, end);
        }
        return sz;
    }

    @Override
    public String name() {
        return _sojNvl.ToString();
    }

    @Override
    public long warmup(int s, int e) {
        long sz = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < _warmup_iters; i++) {
            sz += runPerfCoreRange(s, e);
        }
        long end = System.currentTimeMillis();
        long dur = end - start;
        System.out.println(_sojNvl.ToString() + " warm up takes " + dur + " (ms)");
        return sz;
    }
}
