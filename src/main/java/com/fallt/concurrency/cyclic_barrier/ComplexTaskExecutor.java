package com.fallt.concurrency.cyclic_barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {

    private final ExecutorService executorService;
    private final CyclicBarrier barrier;

    public ComplexTaskExecutor(int countOfThread) {
        executorService = Executors.newFixedThreadPool(countOfThread);
        barrier = new CyclicBarrier(countOfThread);
    }

    public void executeTasks(int numberOfTasks) {
        for (int i = 0; i < numberOfTasks; i++) {
            final int id = i;
            executorService.execute(() -> {
                ComplexTask task = new ComplexTask();
                task.execute();
                System.out.println("Task " + id + " was execute");
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception!");
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
