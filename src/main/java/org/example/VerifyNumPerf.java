package org.example;

public class VerifyNumPerf extends PerfResource implements IPerfEntry {
    public IsNumerical _isNumerical;
    public int _warmup_iters = 10;
    public int _total_iters = 1000;
    public int _repeated = 1;
    private String _values[];

    public VerifyNumPerf(IsNumerical isNumerical, String inputs[], int iter) {
        _isNumerical = isNumerical;
        _values = inputs;
        _total_iters = iter;
    }

    private long runPerfCoreRange(int startIdx, int endIdx) {
        long sz = 0;
        for (int k = 0; k < KEYS.length; k++)
        {
            for (int j = startIdx ; j <= endIdx; j++)
            {
                for (int r = 0; r < _repeated; r++) {
                    _isNumerical.isNumerical(_values[j]);
                    sz += _values[j].length();
                }
            }
        }
        return sz;
    }

    @Override
    public int totalEntries() {
        return _values.length;
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
        System.out.println(_isNumerical.ToString() + " warm up takes " + dur + " (ms)");
        return sz;
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
        return _isNumerical.toString();
    }
}
