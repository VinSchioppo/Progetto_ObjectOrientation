package Controller;
import ClassModel.*;
import DAO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Controller {

    private Utente UtenteCorrente = null;
    private Partecipante PartecipanteCorrente = null;
    private Organizzatore OrganizzatoreCorrente = null;
    private Giudice GiudiceCorrente = null;
    private static ImplementazioneDAO dao = new ImplementazioneDAO();

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            success = dao.checkLoginDB(NomeUtente, Password);
            if(success) UtenteCorrente = dao.getUtenteDB(NomeUtente);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            if(!dao.checkRegisteredDB(NomeUtente)) {
                UtenteCorrente = new Utente(NomeUtente, Password);
                success = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi in cui l'utente ha un ruolo.

    public ArrayList<String> listaEventiUtente() {
        ArrayList<String> listaEventi = null;
        try {
            PartecipanteCorrente = dao.getPartecipanteDB(UtenteCorrente.getNomeUtente());
            OrganizzatoreCorrente = dao.getOrganizzatoreDB(UtenteCorrente.getNomeUtente());
            GiudiceCorrente = dao.getGiudiceDB(UtenteCorrente.getNomeUtente());
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if(PartecipanteCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            PartecipanteCorrente.firstEvento();
            while(PartecipanteCorrente.getEvento() != null){
                String Evento = PartecipanteCorrente.getEvento().getIdEvento() + " " + PartecipanteCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                PartecipanteCorrente.nextEvento();
            }
        }
        if(OrganizzatoreCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            OrganizzatoreCorrente.firstEvento();
            while(OrganizzatoreCorrente.getEvento() != null){
                String Evento = OrganizzatoreCorrente.getEvento().getIdEvento() + " " + OrganizzatoreCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                OrganizzatoreCorrente.nextEvento();
            }
        }
        if(GiudiceCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            GiudiceCorrente.firstEvento();
            while(GiudiceCorrente.getEvento() != null){
                String Evento = GiudiceCorrente.getEvento().getIdEvento() + " " + GiudiceCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                GiudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    public boolean InserisciDatiUtente(String FNome, String MNome, String LNome, String DataString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {

            LocalDate DataConvertita = LocalDate.parse(DataString);

            if ((FNome.length() < 3) || (LNome.length() < 3)) {


                return false;

            } else {

                //UtenteCorrente.SetDati(FNome, MNome, LNome, DataConvertita);

            }

            return true;

        } catch (DateTimeParseException e) {
            System.out.println("errore");
            return false;
        }
    }

}
