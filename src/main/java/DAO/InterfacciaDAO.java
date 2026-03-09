package DAO;
import ClassModel.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface InterfacciaDAO {

    void disconnect();
    boolean checkLoginDB(String NomeUtente, String Password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Evento

    Evento getEventoDB(int IdEvento) throws SQLException;
    ArrayList<Evento> getEventiApertiDB(String NomeUtente) throws SQLException;
    int getIdEventoDB() throws SQLException;
    void addEventoDB(Evento evento) throws SQLException;
    int addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException;
    void updateEventoDB(int IdEvento, String Indirizzo, int NCivico, int MaxIscritti, int MaxTeam) throws SQLException;
    void updateDateEventoDB(int IdEvento, LocalDate DataInizio, LocalDate DataFine) throws SQLException;
    void updateDateRegEventoDB(int IdEvento, LocalDate DataInizioReg, LocalDate DataFineReg) throws SQLException;
    void updateProblemaDB(ArrayList<Evento> eventiProblema) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Partecipante

    Utente getUtenteDB(String NomeUtente) throws SQLException;
    void addUtenteDB(String NomeUtente, String Password) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella InvitoGiudice

    void getAllInvitiGiudiceDB(Evento evento) throws SQLException;
    void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException;
    void addInvitiGiudiceDB(ArrayList<Evento> eventiInvitati) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Partecipante e PartecipanteEvento

    Partecipante getPartecipanteDB(String NomePartecipante) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento) throws SQLException;
    void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException;
    void addPartecipanteEventoDB(String NomePartecipante, ArrayList<Evento> partecipantiEvento) throws SQLException;
    void updatePartecipanteDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Organizzatore e OrganizzatoreEvento

    Organizzatore getOrganizzatoreDB(String NomeUtente) throws SQLException;
    Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException;
    void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException;
    void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;
    void addOrganizzatoreEventoDB(String NomeOrganizzatore, ArrayList<Evento> eventiOrganizzatore) throws SQLException;
    void updateOrganizzatoreDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Giudice e GiudiceEvento

    Giudice getGiudiceDB(String NomeUtente) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento) throws SQLException;
    ArrayList<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException;
    void updateGiudiceDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulle tabelle Team e CompTeam

    ArrayList<Team> getAllTeamDB(Evento evento) throws SQLException;
    ArrayList<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException;
    int getIdTeamDB() throws SQLException;
    int addTeamDB(Team team) throws SQLException;

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

    ArrayList<Commento> getAllCommentiDB(String Giudice) throws SQLException;
    void addAllCommentiDB(ArrayList<Commento> nuoviCommenti) throws SQLException;

    /*******************************************************************************************************/

    //Operazioni sulla tabella Voto

    ArrayList<Voto> getAllVotiDB(String Giudice) throws SQLException;
    void getAllVotiDB(Evento evento) throws SQLException;
    void getAllVotiDB(Partecipante partecipante) throws SQLException;
    void addAllVotiDB(ArrayList<Voto> nuoviVoti) throws SQLException;

}
