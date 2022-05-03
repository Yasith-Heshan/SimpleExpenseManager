package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public interface DBAccountDAO {

    //get list of account numbers
    public List<String> getAccountNumbersList();

    //get list of account objects
    public List<Account> getAccountsList();

    //get account object for given account number
    public Account getAccount(String accountNo) throws InvalidAccountException;

    //add account to database
    public void addAccount(Account account);

    //remove account from database
    public void removeAccount(String accountNo) throws InvalidAccountException;

    //update the balance of given account
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException;
}
