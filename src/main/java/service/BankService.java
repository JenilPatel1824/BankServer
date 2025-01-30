package service;

import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BankService
{
    private static final Logger logger = LoggerFactory.getLogger(BankService.class);

    private static final ConcurrentHashMap<Long, Account> accounts = new ConcurrentHashMap<>();

    public BankService()
    {
        logger.info("Initializing accounts...");

        accounts.put(1L, new Account(101, 1, 1000.0));

        accounts.put(2L, new Account(102, 2, 500.0));

        accounts.put(3L, new Account(103, 3, 1200.0));
    }

    public boolean isValidUser(long userId, long accountNumber)
    {
        logger.debug("Validate user called for {} {}",userId , accountNumber);

        Account account = accounts.get(userId);

        if(account!=null)
        {
            if(account.getAccountNumber()==accountNumber)
            {
                return true;
            }
        }
        return false;
    }

    public double checkBalance(long userId, long accountNumber)
    {
        return accounts.computeIfPresent(userId, (key, account) -> {

            if (isValidUser(userId,accountNumber))
            {
                double balance = account.getBalance();

                logger.info("checkBalance Successfull for {} {} : {}",userId,accountNumber,balance);

                return account;
            }
            else
            {
                logger.error("Account number mismatch or invalid account");

                return null;
            }
        }) != null ? accounts.get(userId).getBalance() : -1;
    }

    public double withdraw(long userId,long accountNumber,double withdrawAmount)
    {
        logger.debug("Withdrawing {} from account number: {}", withdrawAmount, accountNumber);

        return accounts.computeIfPresent(userId, (key, account) -> {

            if (isValidUser(userId,accountNumber))
            {
                double balance;

                if(withdrawAmount>=0 && account.getBalance()>=withdrawAmount)
                {
                    balance=account.setBalance(account.getBalance()-withdrawAmount);

                    logger.info("withdraw Successfull for {} {} : new balance: {}",userId,accountNumber,balance);
                }
                else
                {
                    logger.error("Insufficient Balance or invalid amount!!");
                }
                return account;
            }
            else
            {
                logger.error("Account number mismatch or invalid account");

                return null;
            }
        }) != null ? accounts.get(userId).getBalance() : -1;
    }

    public double deposit(long userId,long accountNumber,double depositAmount)
    {
        logger.debug("Depositing {} to account number: {}", depositAmount, accountNumber);

        return accounts.computeIfPresent(userId, (key, account) -> {

            if (isValidUser(userId,accountNumber))
            {
                if(depositAmount>=0)
                {
                    double balance = account.setBalance(depositAmount+account.getBalance());

                    logger.info("deposit Successfull for {} {} : {}",userId,accountNumber,balance);
                }
                else
                {
                    logger.error("Balance not >=0");
                }
                return account;
            }
            else
            {
                logger.error("Account number mismatch or invalid account");

                return null;
            }
        }) != null ? accounts.get(userId).getBalance() : -1;
    }
}
