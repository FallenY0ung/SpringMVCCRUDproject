package springcourse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class ReplicationService {

    @Value("${db.primary.url}")
    private String primaryUrl;

    @Value("${db.primary.username}")
    private String primaryUsername;

    @Value("${db.primary.password}")
    private String primaryPassword;

    @Value("${db.replica.url}")
    private String replicaUrl;

    @Value("${db.replica.username}")
    private String replicaUsername;

    @Value("${db.replica.password}")
    private String replicaPassword;

    public void replicateAll() {
        Connection primaryConnection = null;
        Connection replicaConnection = null;

        try {
            Class.forName("org.postgresql.Driver");

            primaryConnection = DriverManager.getConnection(primaryUrl, primaryUsername, primaryPassword);
            replicaConnection = DriverManager.getConnection(replicaUrl, replicaUsername, replicaPassword);

            primaryConnection.setReadOnly(true);
            replicaConnection.setAutoCommit(false);

            clearReplica(replicaConnection);

            replicateAdminUsers(primaryConnection, replicaConnection);
            replicatePeople(primaryConnection, replicaConnection);

            replicaConnection.commit();
        } catch (Exception e) {
            if (replicaConnection != null) {
                try {
                    replicaConnection.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw new RuntimeException("Replication failed: " + e.getMessage(), e);
        } finally {
            if (primaryConnection != null) {
                try {
                    primaryConnection.close();
                } catch (SQLException ignored) {
                }
            }
            if (replicaConnection != null) {
                try {
                    replicaConnection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private void clearReplica(Connection replicaConnection) throws SQLException {
        try (Statement st = replicaConnection.createStatement()) {
            st.execute("TRUNCATE TABLE admin_user, person RESTART IDENTITY CASCADE");
        }
    }

    private void replicateAdminUsers(Connection primaryConnection, Connection replicaConnection) throws SQLException {
        String selectSql = "SELECT id, username, password FROM admin_user";
        String insertSql = "INSERT INTO admin_user (id, username, password) VALUES (?, ?, ?)";

        try (Statement selectStatement = primaryConnection.createStatement();
             ResultSet rs = selectStatement.executeQuery(selectSql);
             PreparedStatement insertStatement = replicaConnection.prepareStatement(insertSql)) {

            while (rs.next()) {
                insertStatement.setInt(1, rs.getInt("id"));
                insertStatement.setString(2, rs.getString("username"));
                insertStatement.setString(3, rs.getString("password"));
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }

    private void replicatePeople(Connection primaryConnection, Connection replicaConnection) throws SQLException {
        String selectSql = "SELECT id, name, age, email FROM person";
        String insertSql = "INSERT INTO person (id, name, age, email) VALUES (?, ?, ?, ?)";

        try (Statement selectStatement = primaryConnection.createStatement();
             ResultSet rs = selectStatement.executeQuery(selectSql);
             PreparedStatement insertStatement = replicaConnection.prepareStatement(insertSql)) {

            while (rs.next()) {
                insertStatement.setInt(1, rs.getInt("id"));
                insertStatement.setString(2, rs.getString("name"));
                insertStatement.setInt(3, rs.getInt("age"));
                insertStatement.setString(4, rs.getString("email"));
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }
}