#This is a demo test for configuring SonarQube, the below code has no functionality past ingestion. This code is intentinally vulnerable. 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class VulnerableApp {

    public static void main(String[] args) {
        try {
            // Hardcoded credentials - SonarQube will flag this
            String dbUrl = "jdbc:mysql://localhost:3306/demo";
            String username = "root";
            String password = "password123";

            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            Statement stmt = conn.createStatement();
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username: ");
            String userInput = scanner.nextLine();
            
            // SQL Injection vulnerability - SonarQube will flag this
            String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
