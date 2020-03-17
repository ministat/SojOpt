package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultipleThreadingSoj {
    private int _threadNo = 4;
    private SojNvlPerf _sojNvlPerf;

    public MultipleThreadingSoj(int thd, SojNvlPerf sojNvlPerf) {
        _threadNo = thd;
        _sojNvlPerf = sojNvlPerf;
    }

    private int rangeLength(int totalEntries, int processors, int index) {
        int ret = totalEntries / processors;
        int left = totalEntries % processors;
        if (index < left) {
            ret++;
        }
        return ret;
    }

    public void RunAll() {
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(_threadNo);

        List<Task> taskList = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < _threadNo; i++) {
            int end = rangeLength(_sojNvlPerf.totalEntries(), _threadNo, i);
            assert (end >= 1);
            Task task = new Task(_sojNvlPerf, startIndex, end-1);
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<IResult>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results of " + _sojNvlPerf.name() + " ======");
        long totalSz = 0;
        long totalDur = 0;
        for (int i = 0; i < resultList.size(); i++) {
            Future<IResult> future = resultList.get(i);
            try {
                IResult result = future.get();
                long sz = result.stringSize();
                long dur = result.durationMs();
                totalDur += dur;
                totalSz += sz;
                System.out.println("\t" + i +  "thd throughput: " + (double)(sz / dur) * 1000 + " bytes/s");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Overall throughput: " + (double)(totalSz / totalDur) * 1000 + " bytes/s");
        System.out.println("Overall duration: " + totalDur + "(ms) total strings " + totalSz + " bytes");
    }
}

class Task implements Callable<IResult> {

    private SojNvlPerf _sojNvlPerf;
    private int _start;
    private int _end;

    public Task(SojNvlPerf sojNvlPerf, int s, int e) {
        _sojNvlPerf = sojNvlPerf;
        _start = s;
        _end =e;
    }

    @Override
    public IResult call() throws Exception {
        _sojNvlPerf.warmup(_start, _end);
        long start = System.currentTimeMillis();
        long sz = _sojNvlPerf.rangeProcess(_start, _end);
        long end = System.currentTimeMillis();
        long dur = end - start;
        Result r = new Result(_start, _end, sz, dur);
        return r;
    }
}

class Result implements IResult {
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

interface IResult {
    public int startIndex();
    public int endIndex();
    public long stringSize();
    public long durationMs();
}
