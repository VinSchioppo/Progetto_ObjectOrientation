package ClassModel;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

public class Utente {

    String NomeUtente;
    String PasswordUtente;
    String FNome;
    String MNome;
    String LNome;
    LocalDate DataNascita;
    
    LinkedList<Evento> EventiIscritti = null;
    LinkedList<Team> TeamUniti = null;

    public Utente(String NomeUtente, String PasswordUtente) {
        
        this.NomeUtente = NomeUtente;
        this.PasswordUtente = PasswordUtente;
    
    }

    public void SetDati(String FNome, String MNome, String LNome, LocalDate DataNascita) {

        this.FNome = FNome;
        this.MNome = MNome;
        this.LNome = LNome;
        this.DataNascita = DataNascita;
    
    }

    public void PrintDati() {
        
        System.out.println("Nome ClassModel.Utente: " + this.NomeUtente);
        System.out.println("\nNome: " + this.FNome);
        
        if(!this.MNome.isEmpty())
            System.out.println(" " + this.MNome);

        System.out.println(" " + this.LNome);
        System.out.println("\nData di Nascita: " + this.DataNascita);
    
    }

}
