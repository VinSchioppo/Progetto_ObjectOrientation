package Controller;

import ClassModel.*;

import java.util.LinkedList;

public class Controller {

    private static LinkedList<Utente> UtenteRegistrato = new LinkedList<>();

    public static boolean TrovaUtente(String NomeUtente, String PasswordUtente)
    {
        if(NomeUtente.length() < 5  || PasswordUtente.length() < 8) {
                return false;
        }
        else {
            Utente UtenteTmp = new Utente(NomeUtente, PasswordUtente);
            UtenteRegistrato.addLast(UtenteTmp);
            return true;
        }
    }
}
