package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface InterfacciaDAO {
    void disconnect();

    void addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException;
    void addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException;
    void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException;

    void addUtenteDB(String NomeUtente, String Password) throws SQLException;
    void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException;

    void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addAllPartecipanteDB(ArrayList<Utente> partecipanti, int idEvento) throws SQLException;

    void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException;

    void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException;

    void addTeamDB(String NomeUtente, int idEvento) throws SQLException;
    void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException;
    void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException;

    void addProgressoDB(int idTeam, String testo) throws SQLException;

    void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException;

    void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException;

    void printEventi();
}
