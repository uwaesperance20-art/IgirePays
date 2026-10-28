package lab2;


import lab1.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {


    boolean CreateTable()throws SQLException;

    boolean createTable() throws SQLException;

    Account registerAccount(Account account) throws SQLException;
    Account findById(String id) throws SQLException;
    List<Account> findAll() throws SQLException;
    boolean updateAccount(Account account) throws SQLException;
    boolean deleteAccount(String id) throws SQLException;
}
