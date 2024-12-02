package com.fallt.concurrency.cyclic_barrier;

public class ComplexTask {

    public void execute(){
        System.out.println("Start a difficult task");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception!");
            Thread.currentThread().interrupt();
        }
        System.out.println("End a difficult task");
    }

}
