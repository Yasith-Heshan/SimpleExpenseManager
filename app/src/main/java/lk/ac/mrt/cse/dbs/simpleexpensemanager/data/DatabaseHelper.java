package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "190222P.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        String createAccountTable = "CREATE TABLE accountTable (account_no TEXT PRIMARY KEY, bank TEXT, account_holder TEXT, balance DOUBLE)";
        String createTransactionTable = "CREATE TABLE transactionTable (transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, account_no TEXT, type TEXT, amount DOUBLE," +
                "date DATE, FOREIGN KEY (account_no) " +
                "REFERENCES accountTable(account_no));";
        sqLiteDB.execSQL(createAccountTable);
        sqLiteDB.execSQL(createTransactionTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addNewAccount(Account account){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("account_no",account.getAccountNo());
        contentValues.put("account_holder",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());

        database.insert("accountTable",null,contentValues);

    }

    public Map<String,Account> getAllAccounts(){
        Map<String,Account> accounts = new HashMap<>();
        String query = "SELECT * FROM accountTable;";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
//                int id = cursor.getInt(0);
                String account_no = cursor.getString(0);
                String bank = cursor.getString(1);
                String holder = cursor.getString(2);
                double balance = cursor.getDouble(3);
                Account account = new Account(account_no,bank,holder,balance);
                accounts.put(account_no,account);


            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return accounts;
    }
    public boolean removeAccount(String accountNo){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM accountTable WHERE account_no=?;";
        SQLiteStatement stmt = database.compileStatement(query);
        stmt.bindString(1,accountNo);
        int result = stmt.executeUpdateDelete();
        return result>0;

    }
    public boolean updateBalance(Account account){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE accountTable SET balance = ? where account_no=?;";
        SQLiteStatement stmt = database.compileStatement(query);
        stmt.bindDouble(1,account.getBalance());
        stmt.bindString(2,account.getAccountNo());
        int result = stmt.executeUpdateDelete();
        return result>0;
    }

    public boolean addNewTransaction(Transaction transaction){
        SQLiteDatabase database = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());
        String date = calendar.get(Calendar.YEAR) + ","+calendar.get(Calendar.MONTH)+","+calendar.get(Calendar.DAY_OF_MONTH);

        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no",transaction.getAccountNo());
        contentValues.put("date",date);
        contentValues.put("amount",transaction.getAmount());
        contentValues.put("type",String.valueOf(transaction.getExpenseType()));

        long result = database.insert("transactionTable", null, contentValues);
        return result>0;

    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM transactionTable";

        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {

                /*
                    account_no TEXT,
                    type TEXT,
                    amount DOUBLE,
                       date DATE
                 */
                String account_no = cursor.getString(1);
                String type = cursor.getString(2);
                Double amount = cursor.getDouble(3);
                String[] datearray = cursor.getString(4).split(",");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(datearray[0]),Integer.parseInt(datearray[1]),Integer.parseInt(datearray[2]));
                Date date = calendar.getTime();

                Transaction transaction = new Transaction(date,account_no, ExpenseType.valueOf(type.toUpperCase()),amount);
                transactions.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return transactions;
    }
}
