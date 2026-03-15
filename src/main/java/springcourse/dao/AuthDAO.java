package springcourse.dao;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class AuthDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Защищенная аутентификация
    public boolean authenticate(String username, String password) {
        String sql = "SELECT 1 FROM admin_user WHERE username = ? AND password = ? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // если есть строка — логин/пароль верные
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // BAD: SQL Injection risk (string concatenation + Statement)
    // Уязвимая аутентификация
    public boolean authenticateVulnerable(String username, String password) {
        String sql =
                "SELECT 1 FROM admin_user " +
                        "WHERE username = '" + username + "' " +
                        "AND password = '" + password + "' " +
                        "LIMIT 1";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}