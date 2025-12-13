package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface InterfacciaDAO {
    void disconnect();

    Evento getEventoDB(int IdEvento) throws SQLException;
    int getIdEventoDB() throws SQLException;
    void addEventoDB(Evento evento) throws SQLException;
    int addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException;
    int addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException;
    void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException;

    Utente getUtenteDB(String NomeUtente) throws SQLException;
    void addUtenteDB(Utente utente) throws SQLException;
    void addUtenteDB(String NomeUtente, String Password) throws SQLException;
    void addUtenteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;
    void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException;

    Partecipante getPartecipanteDB(String NomePartecipante, Evento evento) throws SQLException;
    void addPartecipanteDB(Partecipante partecipante, int idEvento) throws SQLException;
    void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addPartecipanteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllPartecipanteDB(ArrayList<Partecipante> partecipanti, int idEvento) throws SQLException;

    Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException;
    void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException;
    void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException;

    Giudice getGiudiceDB(String NomeUtente, Evento evento) throws SQLException;
    void addGiudiceDB(Giudice giudice) throws SQLException;
    void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addGiudiceDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException;

    Team getTeamDB(int IdTeam) throws SQLException;
    int getIdTeamDB() throws SQLException;
    int addTeamDB(String NomeUtente, int idEvento) throws SQLException;
    void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException;
    void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException;

    Progresso getProgressoDB(int IdProgresso) throws SQLException;
    ArrayList<Progresso> getAllProgressoDB(int idTeam) throws SQLException;
    int getIdProgressoDB() throws SQLException;
    void addProgressoDB(Progresso progresso) throws SQLException;
    int addProgressoDB(int idTeam, String testo) throws SQLException;

    void addCommentoDB(Commento commento) throws SQLException;
    void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException;

    void addVotoDB(Voto voto) throws SQLException;
    void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException;

    void printEventi();
}
