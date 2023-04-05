package com.kvitka.subtaskbuilder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class SubtaskRegistryService {

    private final AtomicBoolean free = new AtomicBoolean(true);

    private final AtomicLong solvedAmount = new AtomicLong();
    private final AtomicLong totalAmount = new AtomicLong();

    private final AtomicInteger matrixSize = new AtomicInteger();

    private final List<List<String>> results = new CopyOnWriteArrayList<>();

    public synchronized boolean isFree() {
        return free.get();
    }

    public synchronized void saveSubtaskResult(List<String> result) {
        if (free.get()) throw new RuntimeException();
        solvedAmount.incrementAndGet();
        results.add(result);
        if (solvedAmount.get() == totalAmount.get()) free.set(true);
    }

    public synchronized void registerSubtasks(long totalAmount, int matrixSize) {
        if (!free.get()) throw new RuntimeException();
        free.set(false);
        solvedAmount.set(0);
        this.totalAmount.set(totalAmount);
        results.clear();
        this.matrixSize.set(matrixSize);
    }

    public synchronized List<List<String>> getResults() {
        return new ArrayList<>(this.results);
    }

    public synchronized boolean isDone() {
        return solvedAmount.get() == totalAmount.get();
    }

    public synchronized int getMatrixSize() {
        return matrixSize.get();
    }

    public synchronized long[] getCurrentCompleteness() {
        return new long[]{solvedAmount.get(), totalAmount.get()};
    }
}
