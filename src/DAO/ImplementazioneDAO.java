package DAO;

import ClassModel.Evento;
import ClassModel.Utente;
import Database.ConnessioneDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImplementazioneDAO implements InterfacciaDAO {
    private Connection connection = null;

    public ImplementazioneDAO(){
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEvento(String Titolo, String Indirizzo, int NCivico) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede) VALUES('" + Titolo + "','" + Indirizzo + "'," + NCivico + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllEventi(ArrayList<Evento> eventi) throws SQLException {
        for(Evento currentEvento : eventi){
            try {
                addEvento(currentEvento.getTitolo(), currentEvento.getIndirizzoSede(), currentEvento.getNCivicoSede());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addPartecipante(String NomeUtente, String Password) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('" + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllPartecipanti(ArrayList<Utente> utenti) throws SQLException{
        for(Utente currentPartecipante : utenti){
            try {
                addPartecipante(currentPartecipante.getNomeUtente(), currentPartecipante.getPassword());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }


    public void printEventi() {
        PreparedStatement leggiEventi;
        ResultSet rs = null;
        try {
            leggiEventi = connection.prepareStatement("SELECT * FROM Evento");
            rs = leggiEventi.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString("titolo"));
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
