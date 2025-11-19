package ClassModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Evento {

    String Titolo;
    String Sede;
    Date DataInizio;
    Date DataFine;
    int MaxIscritti;
    int MaxTeam;
    String DescrizioneProblema;
    Date DataInizioReg;
    Date DataFineReg;
    
    Organizzatore organizzatore = null;
    LinkedList<Giudice> Giudici = null;
    Queue<Partecipante> RichiestePartecipazioneUtenti = null;
    Queue<Team> RichiestePartecipazioneTeam = null;
    LinkedList<Partecipante> Partecipanti = null;
    LinkedList<Team> TeamIscritti = null;
    boolean prenotazioni = false;

    public Evento(String Titolo) {
        
        this.Titolo = Titolo;
    
    }

    public void setSede(String sede) {
        
        this.Sede = sede;
    
    }

    public void setDate(Date Inizio, Date Fine) {
        
        this.DataInizio = Inizio;
        this.DataFine = Fine;
    
    }

    public void setMaxIscritti(int MaxIscritti) {
        
        this.MaxIscritti = MaxIscritti;
    
    }

    public void EnqueueListaAttesaUtenti(Partecipante utente) {
        
        if(RichiestePartecipazioneUtenti == null) 
            RichiestePartecipazioneUtenti = new LinkedList<Partecipante>();
        
        RichiestePartecipazioneUtenti.add(utente);
    
    }

    public void AddPartecipante(Partecipante utente) {
        
        if(Partecipanti == null) 
            Partecipanti = new LinkedList<Partecipante>();
        
        Partecipanti.add(utente);
    
    }

    public void DequeueListaAttesaUtenti() {
        
        if(RichiestePartecipazioneUtenti != null) {
            Partecipante utente = RichiestePartecipazioneUtenti.remove();
            if ((Partecipanti == null) || (Partecipanti.size() < MaxIscritti)) {
                AddPartecipante(utente);
                utente.AddEvento(this);
        
            }
        }
    }

    public void EnqueueListaAttesaTeam(Team team) {
        
        if(RichiestePartecipazioneTeam == null) 
            RichiestePartecipazioneTeam = new LinkedList<Team>();
        
        RichiestePartecipazioneTeam.add(team);
    
    }

    public void AddTeam(Team team) {
        
        if( TeamIscritti == null) 
            TeamIscritti = new LinkedList<Team>();
        
        TeamIscritti.add(team);
    
    }

    public void DequeueListaAttesaTeam() {
        
        Team team = RichiestePartecipazioneTeam.remove();
        if((TeamIscritti == null) || (TeamIscritti.size() < MaxTeam)) {
            AddTeam(team);
            team.AddEvento(this);
        
        }
    }
}
