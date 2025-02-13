import java.sql.*;
import java.util.Scanner;

public class SQLInjectionDemo {

    // 🚨 Hardcoded Database Credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter username: ");
        String userInput = scanner.nextLine();

        // 🚨 SQL Injection Vulnerability
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) { 

            if (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
