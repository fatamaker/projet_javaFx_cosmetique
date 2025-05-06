package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private Connection con;
    private static DbConnection dbc;

    private DbConnection() {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/projet", 
                "root", 
                ""
            );
        } catch (Exception ex) {
            System.err.println("Database connection failed:");
            ex.printStackTrace();
        }
    }

    public static DbConnection getDatabaseConnection() {
        if (dbc == null) {
            dbc = new DbConnection();
        }
        return dbc;
    }

    public Connection getConnection() {
        return con;
    }

    public static void main(String[] args) {
        Connection con = null;
        
        try {
            // Get database connection instance
            DbConnection dbConnection = DbConnection.getDatabaseConnection();
            con = dbConnection.getConnection();
            
            // Test connection
            if (con != null && !con.isClosed()) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (Exception e) {
            System.err.println("Error during connection test:");
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                    System.out.println("Connection closed successfully!");
                }
            } catch (Exception ex) {
                System.err.println("Error closing connection:");
                ex.printStackTrace();
            }
        }
    }
}