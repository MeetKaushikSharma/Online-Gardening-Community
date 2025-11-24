package com.gardening.db;

import com.gardening.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void run(String... args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS posts (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        content LONGTEXT NOT NULL,
                        category VARCHAR(20) NOT NULL DEFAULT 'DISCUSSION',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS comments (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        post_id INT NOT NULL,
                        user_id INT NOT NULL,
                        content LONGTEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (post_id) REFERENCES posts(id),
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            safeAlter(statement, "ALTER TABLE posts ADD COLUMN category VARCHAR(20) NOT NULL DEFAULT 'DISCUSSION'");

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS projects (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        description LONGTEXT NOT NULL,
                        progress INT NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            ensureAdminAccount(connection);
            log.info("Database schema verified.");
        } catch (SQLException e) {
            log.error("Failed to initialize database schema", e);
        }
    }

    private void ensureAdminAccount(Connection connection) throws SQLException {
        String checkSql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, "admin");
            if (checkStmt.executeQuery().next()) {
                return;
            }
        }

        String insertSql = "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, "admin");
            insertStmt.setString(2, "admin");
            insertStmt.setString(3, PasswordUtil.hashPassword("admin123@"));
            insertStmt.setString(4, "ADMIN");
            insertStmt.executeUpdate();
            log.info("Default admin user created (email: admin, password: admin123@)");
        }
    }

    private void safeAlter(Statement statement, String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ignored) {
            // Column likely already exists; ignore the error.
        }
    }
}

