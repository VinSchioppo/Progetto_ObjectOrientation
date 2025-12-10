package DAO;

import ClassModel.Evento;
import ClassModel.Giudice;
import ClassModel.Organizzatore;
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

    public void addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede) VALUES('" + Titolo + "','" + Indirizzo + "'," + NCivico + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException {
        for(Evento currentEvento : eventi){
            try {
                addEventoDB(currentEvento.getTitolo(), currentEvento.getIndirizzoSede(), currentEvento.getNCivicoSede());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('" + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllPartecipantiDB(ArrayList<Utente> utenti) throws SQLException{
        for(Utente currentPartecipante : utenti){
            try {
                addPartecipanteDB(currentPartecipante.getNomeUtente(), currentPartecipante.getPasswordUtente());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }


    public void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, idEvento) VALUES('" + NomeUtente + "','" + Password + "'," + idEvento + " );");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException{
        for(Organizzatore currentOrganizzatore : organizzatori){
            try {
                addOrganizzatoreDB(currentOrganizzatore.getNomeUtente(), currentOrganizzatore.getPasswordUtente(), currentOrganizzatore.getIdEventoOrganizzato());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Giudice(NomeUtente, Password) VALUES('" + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
            ps = connection.prepareStatement("INSERT INTO GiudiceEvento(NomeUtente, idEvento) VALUES('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }
    public void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException{
        for(Giudice currentGiudice : giudici){
            try {
                addGiudiceDB(currentGiudice.getNomeUtente(), currentGiudice.getPasswordUtente(), currentGiudice.getIdEventoGiudicato());
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
