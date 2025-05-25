package Controller;
import ClassModel.*;
import java.util.LinkedList;

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
}
