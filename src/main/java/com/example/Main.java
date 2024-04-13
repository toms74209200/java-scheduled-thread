package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        log.info("start");

        try (ScheduledExecutorService service = Executors.newScheduledThreadPool(2)) {
            try (ScheduledWorker worker1 = new ScheduledWorker()) {
                WorkerListener listener1 = createListener(worker1);
                worker1.addListener(listener1);
                service.scheduleAtFixedRate(worker1, 0, 2, TimeUnit.SECONDS);
            }

            try (ScheduledWorker worker2 = new ScheduledWorker()) {
                WorkerListener listener2 = createListener(worker2);
                worker2.addListener(listener2);
                service.scheduleAtFixedRate(worker2, 0, 3, TimeUnit.SECONDS);
            }

            int count = 0;
            for (long i = 0; i < 12; i++) {
                sleep(1000);
                log.info((++count) + " s");
            }
            service.shutdown();
        }

    }

    private static WorkerListener createListener(ScheduledWorker worker) {
        return result -> {
            log.info("notify");
            if (result.ok()) {
                log.info("disposed");
                try {
                    worker.close();
                } catch (Exception ignore) {}
            }
        };
    }

    private static void sleep(long time) {
        for (long i = 0; i < time; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) {}
        }
    }
}