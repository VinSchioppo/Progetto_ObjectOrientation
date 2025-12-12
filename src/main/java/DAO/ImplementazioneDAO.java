package DAO;

import ClassModel.*;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
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
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede) VALUES('"
                            + Titolo + "','" + Indirizzo + "'," + NCivico + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede, DataInizio, DataFine," +
                            " MaxIscritti, MaxTeam, DataInizioReg, DataFineReg, DescrizioneProb)" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, Titolo);
            ps.setString(2, Indirizzo);
            ps.setInt(3, NCivico);
            ps.setObject(4, DataInizio);
            ps.setObject(5, DataFine);
            ps.setInt(6, MaxIscritti);
            ps.setInt(7, MaxTeam);
            ps.setObject(8, DataInizioReg);
            ps.setObject(9, DataFineReg);
            ps.setString(10, DescrizioneProb);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException {
        for(Evento currentEvento : eventi){
            try {
                addEventoDB(currentEvento.getTitolo(), currentEvento.getIndirizzoSede(), currentEvento.getNCivicoSede(), currentEvento.getDataInizio(), currentEvento.getDataFine(), currentEvento.getMaxIscritti(), currentEvento.getMaxTeam(), currentEvento.getDataInizioReg(), currentEvento.getDataFineReg(), currentEvento.getDescrizioneProblema());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addUtenteDB(String NomeUtente, String Password) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException{
        for(Utente currentUtente : utenti){
            try {
                addUtenteDB(currentUtente.getNomeUtente(), currentUtente.getPasswordUtente());
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');CALL IscriviEvento('" + NomeUtente + "'," +
                            + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllPartecipanteDB(ArrayList<Utente> partecipanti, int idEvento) throws SQLException {
        for(Utente currentPartecipante : partecipanti){
            try {
                addPartecipanteDB(currentPartecipante.getNomeUtente(), currentPartecipante.getPasswordUtente(), idEvento);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, idEvento) VALUES('"
                            + NomeUtente + "','" + Password + "'," + idEvento + " );");
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
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Giudice(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');INSERT INTO GiudiceEvento(NomeUtente, idEvento) VALUES('"
                            + NomeUtente + "'," + idEvento + ");");
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

    public void addTeamDB(String NomeUtente, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL CreaTeam('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL JoinTeam('" + NomeUtente + "'," + idTeam + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException {
        for(Partecipante currentPartecipante : partecipanti){
            try {
                addCompTeamDB(currentPartecipante.getNomeUtente(), idTeam);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void addProgressoDB(int idTeam, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Progresso(idTeam, Testo) VALUES("
                            + idTeam + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Commento(NomeGiudice, idProgresso, Testo) VALUES('"
                            + NomeGiudice + "'," + idProgresso + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Voto(NomeGiudice, idTeam, Valore) VALUES('"
                            + NomeGiudice + "'," + idTeam + "," + valore + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
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
