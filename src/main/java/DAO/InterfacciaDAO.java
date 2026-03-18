package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface InterfacciaDAO {

    void disconnect();
    boolean checkLoginDB(String nomeUtente, String password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Evento

    Evento getEventoDB(int idEvento) throws SQLException;
    ArrayList<Evento> getEventiApertiDB(String nomeUtente) throws SQLException;
    int getIdEventoDB() throws SQLException;
    void addEventoDB(Evento evento) throws SQLException;
    int addEventoDB(String titolo, String indirizzo, int nCivico, LocalDate dataInizio, LocalDate dataFine, int maxIscritti, int maxTeam, LocalDate dataInizioReg, LocalDate dataFineReg, String descrizioneProb) throws SQLException;
    void updateEventoDB(int idEvento, String indirizzo, int nCivico, int maxIscritti, int maxTeam) throws SQLException;
    void updateDateEventoDB(int idEvento, LocalDate dataInizio, LocalDate dataFine) throws SQLException;
    void updateDateRegEventoDB(int idEvento, LocalDate dataInizioReg, LocalDate dataFineReg) throws SQLException;
    void updateProblemaDB(ArrayList<Evento> eventiProblema) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Partecipante

    Utente getUtenteDB(String nomeUtente) throws SQLException;
    void addUtenteDB(String nomeUtente, String password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella InvitoGiudice

    void getAllInvitiGiudiceDB(Evento evento) throws SQLException;
    void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException;
    void addInvitiGiudiceDB(ArrayList<Evento> eventiInvitati) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Partecipante e PartecipanteEvento

    Partecipante getPartecipanteDB(String nomePartecipante) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException;
    void addPartecipanteEventoDB(String nomePartecipante, ArrayList<Evento> partecipantiEvento) throws SQLException;
    void updatePartecipanteDB(String nomePartecipante, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Organizzatore e OrganizzatoreEvento

    Organizzatore getOrganizzatoreDB(String nomeUtente) throws SQLException;
    Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException;
    void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException;
    void addOrganizzatoreDB(String nomeUtente, String password, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;
    void addOrganizzatoreEventoDB(String nomeOrganizzatore, ArrayList<Evento> eventiOrganizzatore) throws SQLException;
    void updateOrganizzatoreDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Giudice e GiudiceEvento

    Giudice getGiudiceDB(String nomeUtente) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException;
    void updateGiudiceDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Team e CompTeam

    ArrayList<Team> getAllTeamDB(Evento evento) throws SQLException;
    ArrayList<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException;
    int getIdTeamDB() throws SQLException;
    int addTeamDB(Team team) throws SQLException;
    void leaveTeamDB(String nomePartecipante, int idTeam) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella RischiestaTeam

    void getAllRichiesteTeamDB(Partecipante partecipante) throws SQLException;
    void addRichiesteTeamDB(ArrayList<Team> richiesteTeam) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Progresso

    void getAllProgressiDB(Evento evento) throws SQLException;
    void getAllProgressiDB(Partecipante partecipante) throws SQLException;
    int getIdProgressoDB() throws SQLException;
    void addProgressoDB(Progresso progresso) throws SQLException;
    int addProgressoDB(int idTeam, String testo) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Commento

    ArrayList<Commento> getAllCommentiDB(String giudice) throws SQLException;
    void addAllCommentiDB(ArrayList<Commento> nuoviCommenti) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Voto

    ArrayList<Voto> getAllVotiDB(String giudice) throws SQLException;
    void getAllVotiDB(Evento evento) throws SQLException;
    void getAllVotiDB(Partecipante partecipante) throws SQLException;
    void addAllVotiDB(ArrayList<Voto> nuoviVoti) throws SQLException;

}
