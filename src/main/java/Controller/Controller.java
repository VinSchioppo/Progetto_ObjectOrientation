package Controller;
import ClassModel.*;
import DAO.*;
import Database.ConnessioneDatabase;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Controller {

    private Utente UtenteCorrente = null;
    private static ImplementazioneDAO dao = new ImplementazioneDAO();

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String NomeUtente, String Password) {
        boolean check = false;
        try {
            check = dao.checkLoginDB(NomeUtente, Password);
            if (check) UtenteCorrente = dao.getUtenteDB(NomeUtente);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return check;
    }

    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String NomeUtente, String Password) {
        boolean check = false;
        try {
            if(!dao.checkRegisteredDB(NomeUtente)) {
                UtenteCorrente = new Utente(NomeUtente, Password);
                check = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return check;
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
