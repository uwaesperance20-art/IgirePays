package lab2;

import lab1.Account;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public boolean CreateTable() throws SQLException {
        return false;
    }

    @Override
    public boolean createTable() throws SQLException {
        String createSql = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_id VARCHAR(55) PRIMARY KEY,
                customer_id VARCHAR(8) NOT NULL,
                account_type VARCHAR(240) NOT NULL,
                balance DECIMAL(21,6) NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
            )
            """;

        try (Connection connection = JdbConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createSql);
            return true;
        }
    }

    @Override
    public Account registerAccount(Account account) throws SQLException {
        String checkSql = "SELECT 1 FROM customers WHERE customer_id = ?";
        String insertSql = """
            INSERT INTO accounts (account_id, account_type, balance, created_at, customer_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = JdbConnection.getConnection()) {
            // Check whether the customer exists
            try (PreparedStatement check = connection.prepareStatement(checkSql)) {
                check.setString(1, account.getCustomerId());
                try (ResultSet result = check.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("Customer ID " + account.getCustomerId() + " does not exist.");
                    }
                }
            }

            // Insert the account
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setString(1, account.getAccountId());
                statement.setString(2, account.getAccountType());
                statement.setDouble(3, account.getBalance());
                statement.setObject(4, account.getCreatedAt());
                statement.setString(5, account.getCustomerId());
                statement.executeUpdate();
            }
        }
        return account;
    }

    @Override
    public Account findById(String id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection connection = JdbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getString("account_id"));
                    account.setAccountType(rs.getString("account_type"));
                    account.setBalance(rs.getDouble("balance"));
                    account.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    account.setCustomerId(rs.getString("customer_id"));
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public List<Account> findAll() throws SQLException {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = JdbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getString("account_id"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getDouble("balance"));
                account.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                account.setCustomerId(rs.getString("customer_id"));
                accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public boolean updateAccount(Account account) throws SQLException {
        String sql = """
            UPDATE accounts
            SET account_type = ?, balance = ?, created_at = ?
            WHERE account_id = ?
            """;

        try (Connection connection = JdbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountType());
            statement.setDouble(2, account.getBalance());
            statement.setObject(3, account.getCreatedAt());
            statement.setString(4, account.getAccountId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Account " + account.getAccountId() + " not found.");
                return false;
            }
            System.out.println("Account " + account.getAccountId() + " updated successfully.");
            return true;
        }
    }

    @Override
    public boolean deleteAccount(String id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection connection = JdbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
