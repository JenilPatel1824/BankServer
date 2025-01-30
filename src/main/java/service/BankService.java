package service;

import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private static final Logger logger = LoggerFactory.getLogger(BankService.class);
    private static final ConcurrentHashMap<Long, Account> accounts = new ConcurrentHashMap<>();

    public BankService() {
        logger.info("Initializing accounts...");
        accounts.put(1L, new Account(101, 1, 1000.0));
        accounts.put(2L, new Account(102, 2, 500.0));
        accounts.put(3L, new Account(103, 3, 1200.0));
    }

    public boolean isValidUser(long userId, long accountNumber) {
        Account account = accounts.get(userId);
        return account != null && account.getAccountNumber() == accountNumber;
    }

    public double checkBalance(long userId, long accountNumber) {
        if (!isValidUser(userId, accountNumber)) return -1;
        return accounts.get(userId).getBalance();
    }

    public double deposit(long userId, long accountNumber, double amount) {
        if (!isValidUser(userId, accountNumber)) return -1;
        return accounts.get(userId).deposit(amount);
    }

    public double withdraw(long userId, long accountNumber, double amount) {
        if (!isValidUser(userId, accountNumber)) return -1;
        return accounts.get(userId).withdraw(amount);
    }
}
