package DAO;
import ClassModel.Evento;
import ClassModel.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfacciaDAO {
    void disconnect();
    void addEvento(String Titolo, String Indirizzo, int NCivico) throws SQLException;
    void addAllEventi(ArrayList<Evento> eventi) throws SQLException;

    void addPartecipante(String NomeUtente, String Password) throws SQLException;
    void addAllPartecipanti(ArrayList<Utente> utenti) throws SQLException;

    void printEventi();
}
