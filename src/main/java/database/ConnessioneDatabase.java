package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase Instance = null;
    public Connection Connection = null;
    private String Nome = "vincenzo";
    private String Password = "admin";
    private String Url = "jdbc:postgresql://localhost:5432/Hackathon";
    private String Driver = "org.postgresql.Driver";

    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(Driver);
            Connection = DriverManager.getConnection(Url, Nome, Password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (Instance == null) {
            Instance = new ConnessioneDatabase();
        } else if (Instance.Connection.isClosed()) {
            Instance = new ConnessioneDatabase();
        }
        return Instance;
    }
}
