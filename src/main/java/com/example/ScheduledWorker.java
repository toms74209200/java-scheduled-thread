package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduledWorker implements Runnable, AutoCloseable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final int COUNT_LIMIT = 10;
    private int count = 0;

    private final List<WorkerListener> listeners = new ArrayList<>();

    public void addListener(WorkerListener listener) {
        listeners.add(listener);
    }

    public void notify(WorkerResult result) {
        listeners.forEach(listener -> listener.onEvent(result));
    }

    @Override
    public void close() throws Exception {
        listeners.clear();
    }

    @Override
    public void run() {
      count++;
      log.info("count: {}", count);
      notify(new WorkerResult(count >= COUNT_LIMIT));
    }


}
