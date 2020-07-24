package banking.dao;

import banking.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {
    private static final String URL_TEMPLATE = "jdbc:sqlite:{path-to-database}";

    private static final String CHECK_CARDNUBER_SQL = "SELECT id FROM card WHERE number = ?;";
    private static final String SELECT_CARD_SQL = "SELECT id, number, pin, balance FROM card WHERE number = ?;";
    private static final String MAX_ID_SQL = "SELECT MAX(id) AS id FROM card;";
    private static final String INSERT_SQL = "INSERT INTO card(id, number, pin) VALUES (?, ?, ?);";

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
            return !rs.next();
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
}
