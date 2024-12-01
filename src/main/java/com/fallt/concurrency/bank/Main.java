package com.fallt.concurrency.bank;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        BankAccount account1 = bank.createAccount(BigDecimal.valueOf(10000));
        UUID uuid1 = account1.getNumber();
        BankAccount account2 = bank.createAccount(BigDecimal.valueOf(10000));
        UUID uuid2 = account2.getNumber();

        try (ExecutorService executorService = Executors.newFixedThreadPool(20)) {
            for (int i = 0; i < 10000; i++) {
                executorService.submit(() -> createRandomTransfer(bank, uuid1, uuid2, BigDecimal.valueOf(150)));
            }
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Total balance: " + bank.getTotalBalance());
    }

    private static void createRandomTransfer(ConcurrentBank bank, UUID acc1, UUID acc2, BigDecimal amount) {
        if (random.nextBoolean()) {
            bank.transfer(acc1, acc2, amount);
            System.out.println("Transfer from: " + acc1.toString() + " to " + acc2.toString());
        } else {
            bank.transfer(acc2, acc1, amount);
            System.out.println("Transfer from: " + acc2.toString() + " to " + acc1.toString());
        }
    }

}
