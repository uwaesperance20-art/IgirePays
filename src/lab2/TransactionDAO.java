package lab2;

import lab1.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {

    boolean CreateTable()throws SQLException;
    Transaction registerTransaction(Transaction transaction) throws SQLException;

    List<Transaction> findAll() throws SQLException;

    void viewTransactionHistory(String accountId) throws SQLException;

    boolean transferMoney(String fromAccountId, String toAccountId, double amount) throws SQLException;
    boolean deleteTransaction(String id) throws SQLException;
    boolean isReferenceProcessed(String referenceId) throws SQLException;
    void saveReference(String referenceId) throws SQLException;
    void processTransaction(Transaction tx);
}
