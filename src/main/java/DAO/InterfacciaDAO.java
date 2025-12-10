package DAO;
import ClassModel.Evento;
import ClassModel.Giudice;
import ClassModel.Organizzatore;
import ClassModel.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfacciaDAO {
    void disconnect();

    void addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException;
    void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException;

    void addPartecipanteDB(String NomeUtente, String Password) throws SQLException;
    void addAllPartecipantiDB(ArrayList<Utente> utenti) throws SQLException;

    void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException;

    void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException;
    void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException;



    void printEventi();
}
