package Controller;
import ClassModel.*;

import javax.xml.crypto.Data;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Controller {

    private Utente UtenteCorrente;
    private static LinkedList<Utente> UtentiRegistrati = new LinkedList<>();

    public boolean TrovaUtente(String NomeUtente, String PasswordUtente)
    {
        if(NomeUtente.length() < 5  || PasswordUtente.length() < 8) {
                return false;
        }
        else {
            Utente UtenteTmp = new Utente(NomeUtente, PasswordUtente);
            for(Utente u : UtentiRegistrati) {
                if(UtenteTmp.equals(u)) {
                    return false;
                }
            }
            UtentiRegistrati.addLast(UtenteTmp);
            UtenteCorrente = UtenteTmp;
            return true;
        }
    }

    public boolean InserisciDatiUtente(String FNome, String MNome, String LNome, String DataString){

        LocalDate DataConvertita;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DataConvertita = LocalDate.parse(DataString, formatter);
        }
        catch (DateTimeParseException e) {
            return false;
        }

        if((FNome.length() < 3) || (LNome.length() < 3))
        {
            return false;
        }
        else {
            UtenteCorrente.SetDati(FNome, MNome, LNome, DataConvertita);
            UtenteCorrente.PrintDati();
            return true;
        }
    }

}
