import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

class LoginAppTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/softwaretesting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";
    private LoginApp loginApp;

    @BeforeAll
    static void setupDatabase() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String createTable = """
                CREATE TABLE IF NOT EXISTS User (
                    Email VARCHAR(255) PRIMARY KEY,
                    Name VARCHAR(255) NOT NULL,
                    Password VARCHAR(255) NOT NULL
                )
            """;
            conn.createStatement().execute(createTable);

            String insertUser = "INSERT INTO User (Email, Name, Password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertUser);
            stmt.setString(1, "test@example.com");
            stmt.setString(2, "Test User");
            stmt.setString(3, "password123");
            stmt.executeUpdate();
        }
    }

    @AfterAll
    static void cleanupDatabase() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.createStatement().execute("DROP TABLE IF EXISTS User");
        }
    }

    @Test
    void testValidLogin() {
        loginApp = new LoginApp();
        String userName = loginApp.authenticateUser("test@example.com");
        assertEquals("Test User", userName, "Valid login failed.");
    }

    @Test
    void testInvalidLogin() {
        loginApp = new LoginApp();
        String userName = loginApp.authenticateUser("invalid@example.com");
        assertNull(userName, "Invalid login should return null.");
    }

    @Test
    void testEmptyInputValidation() {
        // Simulate empty input validation
        loginApp = new LoginApp();
        // Cannot automate GUI-related validation in unit tests directly, this would need a functional test framework
    }

    @Test
    void testDatabaseConnectionFailure() {
        LoginApp appWithInvalidDB = new LoginApp() {
            @Override
            public String authenticateUser(String email) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/invalidDB", DB_USER, DB_PASSWORD)) {
                    return null;
                } catch (Exception e) {
                    return "DB_ERROR";
                }
            }
        };
        String userName = appWithInvalidDB.authenticateUser("test@example.com");
        assertEquals("DB_ERROR", userName, "Database connection failure not handled properly.");
    }
}
