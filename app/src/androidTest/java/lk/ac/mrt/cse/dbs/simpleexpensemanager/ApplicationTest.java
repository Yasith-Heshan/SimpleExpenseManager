/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    private static ExpenseManager expenseManager;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);
    }

    @Test
    public void testAddacount(){
        expenseManager.addAccount("19022P","BOC","Yasith",1000.00);
        List<String> accNumbers = expenseManager.getAccountNumbersList();
        assertTrue(accNumbers.contains("19022P"));
    }
    @Test
    public void testIncomeTransaction(){
        expenseManager.addAccount("19022P","BOC","Yasith",1000.00);
        try {
            expenseManager.updateAccountBalance("190222P",12,03,2022, ExpenseType.INCOME,"100.00");

            List<Transaction> transactions =  expenseManager.getTransactionLogs();
            Transaction last = transactions.get(transactions.size()-1);
            assertEquals("190222P",last.getAccountNo());
            assertEquals(ExpenseType.INCOME,last.getExpenseType());
            assertEquals(100.00,last.getAmount(),0.0);
            assertEquals("12-04-2022",new SimpleDateFormat("dd-MM-yyyy").format(last.getDate()));
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testExpenseTransaction(){
        expenseManager.addAccount("19022P","BOC","Yasith",1000.00);
        try {
            expenseManager.updateAccountBalance("190222P",12,03,2022, ExpenseType.EXPENSE,"100.00");

            List<Transaction> transactions =  expenseManager.getTransactionLogs();
            Transaction last = transactions.get(transactions.size()-1);
            assertEquals("190222P",last.getAccountNo());
            assertEquals(ExpenseType.EXPENSE,last.getExpenseType());
            assertEquals(100.00,last.getAmount(),0.0);
            assertEquals("12-04-2022",new SimpleDateFormat("dd-MM-yyyy").format(last.getDate()));
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }
}