import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.nio.file.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.*;
import java.security.*;
import java.util.regex.*;

public class VulnerableApp {

    // Hardcoded credentials (SonarQube will flag this)
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "password123";
    private static final String SECRET_KEY = "mysecretkey12345"; // Insecure hardcoded cryptographic key

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // SQL Injection Vulnerability
        System.out.print("Enter username: ");
        String userInput = scanner.nextLine();
        checkUser(userInput);

        // Command Injection
        System.out.print("Enter command: ");
        String command = scanner.nextLine();
        executeCommand(command);

        // Path Traversal
        System.out.print("Enter filename to read: ");
        String filename = scanner.nextLine();
        readFile(filename);

        // Insecure Hashing
        System.out.print("Enter password to hash: ");
        String password = scanner.nextLine();
        insecureHash(password);

        // Insecure Encryption
        System.out.print("Enter text to encrypt: ");
        String text = scanner.nextLine();
        encryptData(text);

        scanner.close();
    }

    // 🚨 SQL Injection Vulnerability
    public static void checkUser(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM users WHERE username = '" + username + "'"; // 🚨 Vulnerable to SQL Injection
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🚨 Command Injection
    public static void executeCommand(String userInput) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = os.contains("win") ? "cmd.exe /c " + userInput : "/bin/sh -c " + userInput;
            Runtime.getRuntime().exec(command); // 🚨 Vulnerable to Command Injection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🚨 Path Traversal Vulnerability
    public static void readFile(String filename) {
        try {
            File file = new File("/var/data/" + filename); // 🚨 Allows path traversal (../../etc/passwd)
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    // 🚨 Insecure Hashing
    public static void insecureHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 🚨 Weak hashing algorithm
            md.update(password.getBytes());
            byte[] digest = md.digest();
            System.out.println("Hashed password: " + Base64.getEncoder().encodeToString(digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // 🚨 Insecure Encryption
    public static void encryptData(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); // 🚨 Hardcoded key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🚨 XSS Vulnerability (if used in a web app)
    public static void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userInput = request.getParameter("name");
        response.getWriter().println("<html><body>Welcome, " + userInput + "</body></html>"); // 🚨 No input sanitization
    }

    // 🚨 Insecure Random Generator
    public static void insecureRandom() {
        Random rand = new Random(); // 🚨 Predictable random values
        System.out.println("Random number: " + rand.nextInt(100));
    }

    // 🚨 Unrestricted File Upload (if used in a web app)
    public static void uploadFile(HttpServletRequest request) throws Exception {
        Part filePart = request.getPart("file"); // 🚨 No restriction on file type or size
        String fileName = filePart.getSubmittedFileName();
        File file = new File("/var/uploads/" + fileName);
        Files.copy(filePart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // 🚨 Weak Authentication (if used in a web app)
    public static boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("123456"); // 🚨 Weak authentication
    }

    // 🚨 Log Injection
    public static void logUserAction(String userInput) {
        System.out.println("User action: " + userInput); // 🚨 No sanitization, can be used for log injection
    }

    // 🚨 Hardcoded API Key
    public static void useApi() {
        String apiKey = "SECRET_API_KEY_123"; // 🚨 Hardcoded API key
        System.out.println("Using API key: " + apiKey);
    }
}
