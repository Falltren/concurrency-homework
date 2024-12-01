package com.fallt.concurrency.blocking_queue;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        BlockingQueue<String> blockingQueue = new BlockingQueue<>();
        Thread producer = new Thread(() ->
        {
            for (int i = 0; i < 15; i++) {
                String str = "String " + i;
                try {
                    blockingQueue.enqueue(str);
                    LOGGER.log(Level.INFO, "Added new string: {0}", str);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() ->
        {
            for (int i = 0; i < 15; i++) {
                try {
                    String string = blockingQueue.dequeue();
                    LOGGER.log(Level.INFO, "Get string: {0}", string);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        producer.start();
        consumer.start();
    }

}
