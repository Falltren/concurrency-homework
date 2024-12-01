package com.fallt.concurrency.bank;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private final UUID number;
    private BigDecimal balance;
    private final Lock lock;

    public BankAccount(UUID number, BigDecimal balance) {
        this.number = number;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public UUID getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Lock getLock(){
        return lock;
    }

    public void deposit(BigDecimal deposit) {
        lock.lock();
        try {
            balance = balance.add(deposit);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal withdraw) {
        lock.lock();
        try {
            if (balance.compareTo(withdraw) < 0) {
                throw new IllegalArgumentException("You do not have enough money in account " + number);
            }
            balance = balance.subtract(withdraw);
        } finally {
            lock.unlock();
        }
    }

}
