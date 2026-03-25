package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase instance = null;
    private Connection connection = null;
    private static final String NOME = "vincenzo";
    private static final String PASSWORD = "admin";
    private static final String URL = "jdbc:postgresql://localhost:5432/Hackathon";

    private ConnessioneDatabase() throws SQLException {
        connection = DriverManager.getConnection(URL, NOME, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed())
            instance = new ConnessioneDatabase();
        return instance;
    }
}
