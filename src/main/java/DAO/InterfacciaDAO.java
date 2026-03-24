package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface InterfacciaDAO {

    void disconnect() throws SQLException;
    boolean checkLoginDB(String nomeUtente, String password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Evento

    Evento getEventoDB(int idEvento) throws SQLException;
    List<Evento> getEventiApertiDB(String nomeUtente) throws SQLException;
    int getIdEventoDB() throws SQLException;
    void addEventoDB(Evento evento) throws SQLException;
    int addEventoDB(String titolo, String indirizzo, int nCivico, LocalDate dataInizio, LocalDate dataFine, int maxIscritti, int maxTeam, LocalDate dataInizioReg, LocalDate dataFineReg, String descrizioneProb) throws SQLException;
    void updateEventoDB(int idEvento, String indirizzo, int nCivico, int maxIscritti, int maxTeam) throws SQLException;
    void updateDateEventoDB(int idEvento, LocalDate dataInizio, LocalDate dataFine) throws SQLException;
    void updateDateRegEventoDB(int idEvento, LocalDate dataInizioReg, LocalDate dataFineReg) throws SQLException;
    void updateProblemaDB(List<Evento> eventiProblema) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Partecipante

    Utente getUtenteDB(String nomeUtente) throws SQLException;
    void addUtenteDB(String nomeUtente, String password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella InvitoGiudice

    void getAllInvitiGiudiceDB(Evento evento) throws SQLException;
    void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException;
    void addInvitiGiudiceDB(List<Evento> eventiInvitati) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Partecipante e PartecipanteEvento

    Partecipante getPartecipanteDB(String nomePartecipante) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException;
    void addPartecipanteEventoDB(String nomePartecipante, List<Evento> partecipantiEvento) throws SQLException;
    void updatePartecipanteDB(String nomePartecipante, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Organizzatore e OrganizzatoreEvento

    Organizzatore getOrganizzatoreDB(String nomeUtente) throws SQLException;
    Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException;
    void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException;
    void addOrganizzatoreDB(String nomeUtente, String password, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;
    void addOrganizzatoreEventoDB(String nomeOrganizzatore, List<Evento> eventiOrganizzatore) throws SQLException;
    void updateOrganizzatoreDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Giudice e GiudiceEvento

    Giudice getGiudiceDB(String nomeUtente) throws SQLException;
    List<Giudice> getAllGiudiciDB(Evento evento) throws SQLException;
    List<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException;
    void updateGiudiceDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Team e CompTeam

    List<Team> getAllTeamDB(Evento evento) throws SQLException;
    List<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException;
    int getIdTeamDB() throws SQLException;
    int addTeamDB(Team team) throws SQLException;
    void leaveTeamsDB(String nomePartecipante, List<Team> leaveTeam) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella RischiestaTeam

    void getAllRichiesteTeamDB(Partecipante partecipante) throws SQLException;
    void addRichiesteTeamDB(List<Team> richiesteTeam) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Progresso

    void getAllProgressiDB(Evento evento) throws SQLException;
    void getAllProgressiDB(Partecipante partecipante) throws SQLException;
    int getIdProgressoDB() throws SQLException;
    void addProgressoDB(Progresso progresso) throws SQLException;
    int addProgressoDB(int idTeam, String testo) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Commento

    List<Commento> getAllCommentiDB(String giudice) throws SQLException;
    void addAllCommentiDB(List<Commento> nuoviCommenti) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Voto

    List<Voto> getAllVotiDB(String giudice) throws SQLException;
    void getAllVotiDB(Evento evento) throws SQLException;
    void getAllVotiDB(Partecipante partecipante) throws SQLException;
    void addAllVotiDB(List<Voto> nuoviVoti) throws SQLException;

}
