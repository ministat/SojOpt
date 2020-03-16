package org.example;

public interface IPerfEntry {
    int totalEntries();
    long warmup(int start, int end);
    long rangeProcess(int start, int end);
    String name();
}
