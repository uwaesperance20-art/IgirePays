package lab2;

import lab1.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO{
    @Override
    public boolean CreateTable() throws SQLException {
        String createTransaction = """
            CREATE TABLE transactions (
                transaction_id   VARCHAR(200) PRIMARY KEY,
                account_id       VARCHAR(50)  NOT NULL,
                reference_id     VARCHAR(100) NOT NULL,
                transaction_type VARCHAR(50)  NOT NULL,
                amount           DECIMAL(20,4) NOT NULL,
                created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_id) REFERENCES accounts(account_id)
            )
            """;

        try (Connection connection = JdbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(createTransaction)) {
            statement.executeUpdate();
            return true;
        }
    }

@Override
public Transaction registerTransaction(Transaction tx) throws SQLException {
    String sql = """
            INSERT INTO transactions(account_id, reference_id, transaction_type, amount, created_at)
            VALUES (?, ?, ?, ?, ?)
            """;

    try (Connection conn = JdbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tx.getAccountId());
        ps.setString(2, tx.getReferenceId());
        ps.setString(3, tx.getTransactionType());
        ps.setDouble(4, tx.getAmount());
        ps.setObject(5, tx.getTimestamp());
        ps.executeUpdate();
    }
    return tx;
}

    @Override
    public List<Transaction> findAll() throws SQLException {
    String sql = "SELECT * FROM transactions";
    List<Transaction> transactions = new ArrayList<>();

    try (Connection conn = JdbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            transactions.add(new Transaction(
                    rs.getString("reference_id"),
                    rs.getString("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getObject("created_at", LocalDateTime.class)
            ));
        }
    }
    return transactions;
}

@Override
public void viewTransactionHistory(String accountId) throws SQLException {
    String sql = "SELECT * FROM transactions WHERE account_id = ?";
    try (Connection conn = JdbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, accountId);
        try (ResultSet rs = ps.executeQuery()) {
            System.out.println("Transaction history for account " + accountId + ":");
            while (rs.next()) {
                System.out.println(
                        rs.getString("reference_id") + " | " +
                                rs.getString("transaction_type") + "|"+
                                rs.getDouble("amount") +"|"+
                                rs.getTimestamp("created_at")
                );
            }
        }
    }
}

@Override
public boolean transferMoney(String fromAccountId, String toAccountId, double amount) throws SQLException {
    Connection conn = JdbConnection.getConnection();
    try {
        conn.setAutoCommit(false);

        // Withdraw from source
        String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(withdrawSql)) {
            ps.setDouble(1, amount);
            ps.setString(2, fromAccountId);
            ps.executeUpdate();
        }

        // Deposit to destination
        String depositSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(depositSql)) {
            ps.setDouble(1, amount);
            ps.setString(2, toAccountId);
            ps.executeUpdate();
        }

        // Record transaction
        String txSql = """
                INSERT INTO transactions(account_id, reference_id, transaction_type, amount, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(txSql)) {
            ps.setString(1, fromAccountId);
            ps.setString(2, "TX" + System.currentTimeMillis());
            ps.setString(3, "TRANSFER");
            ps.setDouble(4, amount);
            ps.setObject(5, LocalDateTime.now());
            ps.executeUpdate();
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
        conn.close();
    }
}

@Override
public boolean deleteTransaction(String id) throws SQLException {
    String sql = "DELETE FROM transactions WHERE reference_id = ?";
    try (Connection conn = JdbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, id);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}

@Override
public boolean isReferenceProcessed(String referenceId) throws SQLException {
    String sql = "SELECT 1 FROM transactions WHERE reference_id = ?";
    try (Connection conn = JdbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, referenceId);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }
}

@Override
public void saveReference(String referenceId) throws SQLException {
    // In this design, references are stored in transactions table itself
    // If you want a separate processed_requests table, implement here
}

    @Override
    public void processTransaction(Transaction tx) {

    }
}
