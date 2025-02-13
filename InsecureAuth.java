import java.security.MessageDigest;
import java.util.Scanner;

public class InsecureAuth {

    // 🚨 Hardcoded Credentials
    private static final String HARDCODED_USERNAME = "admin";
    private static final String HARDCODED_PASSWORD = "password123"; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authenticate(username, password)) {
            System.out.println("Authentication Successful!");
        } else {
            System.out.println("Authentication Failed!");
        }

        scanner.close();
    }

    // 🚨 Weak Hashing (MD5)
    public static boolean authenticate(String username, String password) {
        return username.equals(HARDCODED_USERNAME) && hashPassword(password).equals(hashPassword(HARDCODED_PASSWORD));
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 🚨 Weak hashing algorithm
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
