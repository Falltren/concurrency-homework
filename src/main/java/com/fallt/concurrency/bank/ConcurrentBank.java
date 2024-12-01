package com.fallt.concurrency.bank;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ConcurrentBank {

    private final Map<UUID, BankAccount> accounts;

    public ConcurrentBank() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public BankAccount createAccount(BigDecimal amount) {
        UUID number = UUID.randomUUID();
        BankAccount bankAccount = new BankAccount(number, amount);
        accounts.put(number, bankAccount);
        return bankAccount;
    }

    public void transfer(UUID from, UUID to, BigDecimal amount) {
        BankAccount accountFrom = accounts.get(from);
        BankAccount accountTo = accounts.get(to);
        if (accountTo == null || accountFrom == null) {
            System.out.println("Incorrect from or to account number");
            return;
        }
        Lock firstLock = accountFrom.getLock();
        Lock secondLock = accountTo.getLock();
        if (firstLock.hashCode() > secondLock.hashCode()) {
            firstLock.lock();
            try {
                secondLock.lock();
                try {
                    accountFrom.withdraw(amount);
                    accountTo.deposit(amount);
                } finally {
                    secondLock.unlock();
                }
            } finally {
                firstLock.unlock();
            }
        } else {
            secondLock.lock();
            try {
                firstLock.lock();
                try {
                    accountFrom.withdraw(amount);
                    accountTo.deposit(amount);
                } finally {
                    firstLock.unlock();
                }
            } finally {
                secondLock.unlock();
            }
        }

    }

    public BigDecimal getTotalBalance() {
        return accounts.values()
                .stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
