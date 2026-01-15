package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface InterfacciaDAO {
    void disconnect();

    boolean checkLoginDB(String NomeUtente, String Password) throws SQLException;
    boolean checkRegisteredDB(String NomeUtente) throws SQLException;
    ArrayList<Utente> getAllRuoliDB(Utente utente) throws SQLException;

    Evento getDatiEventoDB(int IdEvento) throws SQLException;
    Evento getEventoDB(int IdEvento) throws SQLException;
    Evento getEventoDB(int IdEvento, Organizzatore organizzatore) throws SQLException;
    Evento getEventoDB(int IdEvento, Giudice giudice) throws SQLException;
    ArrayList<Evento> getAllEventiDB(Organizzatore organizzatore) throws SQLException;
    ArrayList<Evento> getEventiApertiDB() throws SQLException;
    ArrayList<Evento> getEventiApertiDB(String NomeUtente) throws SQLException;
    int getIdEventoDB() throws SQLException;
    void addEventoDB(Evento evento) throws SQLException;
    int addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException;
    int addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException;
    void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException;
    void updateEventoDB(int IdEvento, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException;

    Utente getUtenteDB(String NomeUtente) throws SQLException;
    void addUtenteDB(Utente utente) throws SQLException;
    void addUtenteDB(String NomeUtente, String Password) throws SQLException;
    void addUtenteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;
    void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException;

    void getAllInvitiGiudiceDB(Evento evento) throws SQLException;
    void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException;

    Partecipante getPartecipanteDB(String NomePartecipante) throws SQLException;
    Partecipante getPartecipanteDB(String NomePartecipante, Evento evento) throws SQLException;
    Partecipante getPartecipanteDB(String NomePartecipante, Team team) throws SQLException;
    ArrayList<Partecipante> getAllPartecipantiDB(Evento evento) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException;
    void addPartecipanteDB(Partecipante partecipante, int idEvento) throws SQLException;
    void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addPartecipanteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllPartecipanteDB(ArrayList<Partecipante> partecipanti, int idEvento) throws SQLException;
    void updatePartecipanteDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    Organizzatore getOrganizzatoreDB(String NomeUtente) throws SQLException;
    Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException;
    void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException;
    void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException;
    void updateOrganizzatoreDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    Giudice getGiudiceDB(String NomeUtente) throws SQLException;
    Giudice getGiudiceDB(String NomeUtente, Evento evento) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException;
    void addGiudiceDB(Giudice giudice) throws SQLException;
    void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addGiudiceDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException;
    void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException;
    void updateGiudiceDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    Team getTeamDB(int IdTeam) throws SQLException;
    ArrayList<Team> getAllTeamDB(Evento evento) throws SQLException;
    ArrayList<Team> getAllTeamDB(Partecipante partecipante) throws SQLException;
    ArrayList<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException;
    int getIdTeamDB() throws SQLException;
    int addTeamDB(Team team) throws SQLException;
    int addTeamDB(String NomeUtente, int idEvento) throws SQLException;
    void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException;
    void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException;
    void getAllRichiesteTeamDB(Partecipante partecipante) throws SQLException;

    Progresso getProgressoDB(int IdProgresso) throws SQLException;
    ArrayList<Progresso> getAllProgressiDB(int idTeam) throws SQLException;
    void getAllProgressiDB(Evento evento) throws SQLException;
    void getAllProgressiDB(Partecipante partecipante) throws SQLException;
    int getIdProgressoDB() throws SQLException;
    void addProgressoDB(Progresso progresso) throws SQLException;
    int addProgressoDB(int idTeam, String testo) throws SQLException;

    Commento getCommentoDB(int idProgresso, String Giudice) throws SQLException;
    ArrayList<Commento> getAllCommentiDB(int idProgresso) throws SQLException;
    ArrayList<Commento> getAllCommentiDB(String Giudice) throws SQLException;
    void addCommentoDB(Commento commento) throws SQLException;
    void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException;

    Voto getVotoDB(int idTeam, String Giudice) throws SQLException;
    ArrayList<Voto> getAllVotiDB(int idTeam) throws SQLException;
    ArrayList<Voto> getAllVotiDB(String Giudice) throws SQLException;
    void getAllVotiDB(Evento evento) throws SQLException;
    void getAllVotiDB(Partecipante partecipante) throws SQLException;
    void addVotoDB(Voto voto) throws SQLException;
    void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException;
}
