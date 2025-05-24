import java.util.Date;
import java.util.LinkedList;

public class Utente {

    String NomeUtente;
    String PasswordUtente;
    String FNome;
    String MNome;
    String LNome;
    Date DataNascita;
    
    LinkedList<Evento> EventiIscritti = null;
    LinkedList<Team> TeamUniti = null;

    public Utente(String NomeUtente, String PasswordUtente) {
        
        this.NomeUtente = NomeUtente;
        this.PasswordUtente = PasswordUtente;
    
    }

    public void SetDati(String FNome, String MNome, String LNome, Date DataNascita) {

        this.FNome = FNome;
        this.MNome = MNome;
        this.LNome = LNome;
        this.DataNascita = DataNascita;
    
    }

    public void PrintDati() {
        
        System.out.println("Nome Utente: " + this.NomeUtente);
        System.out.println("\nNome: " + this.FNome);
        
        if(!this.MNome.isEmpty())
            System.out.println(" " + this.MNome);

        System.out.println(" " + this.LNome);
        System.out.println("\nData di Nascita: " + this.DataNascita);
    
    }

    public void IscriviEvento(Evento evento) {

        evento.EnqueueListaAttesaUtenti(this);

    }

    public void AddEvento(Evento evento) {
        
        if(EventiIscritti == null) 
            EventiIscritti = new LinkedList<Evento>();
        
        EventiIscritti.add(evento);
    
    }

    public Team CreaTeam(String Nome) {
        
        Team new_team = new Team(Nome);
        AddTeam(new_team);
        new_team.MembriTeam = new LinkedList<Utente>();
        new_team.MembriTeam.add(this);
        return new_team;

    }

    public void JoinTeam(Team team) {
        
        team.EnqueueListaAttesa(this);

    }

    public void AddTeam(Team team) {
        
        if(TeamUniti == null) 
            TeamUniti = new LinkedList<Team>();
        
        TeamUniti.add(team);
    
    }
}
