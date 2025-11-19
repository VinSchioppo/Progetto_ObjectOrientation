package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerDatabase{
    private Connection connection;

    public ControllerDatabase(){
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void PrintEventi() {
        PreparedStatement leggiEventi;
        ResultSet rs = null;
        try {
            leggiEventi = connection.prepareStatement("SELECT * FROM Evento");
            rs = leggiEventi.executeQuery();
            rs.next();
            System.out.println(rs.getString("titolo"));
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
