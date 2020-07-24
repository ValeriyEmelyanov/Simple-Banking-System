package banking.dao;

import banking.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {
    private static final String URL_TEMPLATE = "jdbc:sqlite:{path-to-database}";

    private static final String CHECK_CARDNUBER_SQL = "SELECT id FROM card WHERE number=?;";
    private static final String SELECT_CARD_SQL = "SELECT id, number, pin, balance FROM card WHERE number=?;";
    private static final String MAX_ID_SQL = "SELECT MAX(id) AS id FROM card;";
    private static final String INSERT_SQL = "INSERT INTO card(id, number, pin) VALUES (?,?,?);";
    private static final String UPDATE_BALANCE_SQL = "UPDATE card SET balance = balance + ? WHERE number=?;";
    private static final String SELECT_BALANCE_SQL = "SELECT balance FROM card WHERE number =?;";
    private static final String DELETE_SQL = "DELETE FROM card WHERE number=?;";

    private final SQLiteDataSource dataSource;

    public AccountDao(String filename) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL_TEMPLATE.replace("{path-to-database}", filename));
    }

    public boolean checkCardNumber(String cardNumber) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CHECK_CARDNUBER_SQL);
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }

    private int getMaxId(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(MAX_ID_SQL);
        ResultSet rs = statement.executeQuery();
        int id;
        if (rs.next()) {
            id = rs.getInt("id") + 1;
        } else {
            id = 1;
        }
        return id;
    }

    public Account create(String cardNumber, String pin) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            int id = getMaxId(connection);
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            statement.setInt(1, id);
            statement.setString(2, cardNumber);
            statement.setString(3, pin);
            int result = statement.executeUpdate();
            if (result != 1) {
                return null;
            }
            return new Account(id, cardNumber, pin);
        }
    }

    public Account getByCardnumber(String cardNumber) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_CARD_SQL);
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new Account(rs.getInt("id"), rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
        }
    }

    public int addIncome(String cardNumber, int sum) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE_SQL);
            statement.setInt(1, sum);
            statement.setString(2, cardNumber);
            int result = statement.executeUpdate();
            if (result != 1) {
                return -1;
            }

            statement = connection.prepareStatement(SELECT_BALANCE_SQL);
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return -1;
            }

            connection.commit();

            return rs.getInt("balance");
        }
    }

    public boolean doTransfer(Account account, String cardNumberTo, int sum) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE_SQL);
            statement.setInt(1, -sum);
            statement.setString(2, account.getCardNumber());
            int result = statement.executeUpdate();
            if (result != 1) {
                connection.rollback();
                return false;
            }

            statement = connection.prepareStatement(SELECT_BALANCE_SQL);
            statement.setString(1, account.getCardNumber());
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                connection.rollback();
                return false;
            }
            int balance = rs.getInt("balance");
            account.setBalance(balance);
            if (balance < 0) {
                connection.rollback();
                return false;
            }

            statement = connection.prepareStatement(UPDATE_BALANCE_SQL);
            statement.setInt(1, sum);
            statement.setString(2, cardNumberTo);
            result = statement.executeUpdate();
            if (result != 1) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        }
    }

    public boolean deleteByCardNumber(String cardNumber) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
            statement.setString(1, cardNumber);
            int result = statement.executeUpdate();
            return result == 1;
        }
    }
}
