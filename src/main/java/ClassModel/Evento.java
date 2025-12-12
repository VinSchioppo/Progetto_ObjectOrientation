package ClassModel;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

public class Evento {

    private int IdEvento = -1;
    private String Titolo;
    private String IndirizzoSede = null;
    private int NCivicoSede = -1;
    private LocalDate DataInizio = null;
    private LocalDate DataFine = null;
    private int MaxIscritti = -1;
    private int MaxTeam = -1;
    private String DescrizioneProblema = null;
    private LocalDate DataInizioReg = null;
    private LocalDate DataFineReg = null;

    private Organizzatore organizzatore = null;
    protected ArrayList<Giudice> Giudici = null;
    private Queue<Partecipante> RichiestePartecipazioneUtenti = null;
    private Queue<Team> RichiestePartecipazioneTeam = null;
    private ArrayList<Partecipante> Partecipanti = null;
    private ArrayList<Team> TeamIscritti = null;
    boolean prenotazioni = false;

    public Evento(String Titolo) {this.Titolo = Titolo;}
    public void setIndirizzoSede(String sede) {this.IndirizzoSede = sede;}
    public void setNCivicoSede(int sede) {this.NCivicoSede = sede;}
    public void setMaxIscritti(int MaxIscritti) {this.MaxIscritti = MaxIscritti;}
    public void setMaxTeam(int MaxTeam) {this.MaxTeam = MaxTeam;}
    public void setDescrizioneProblema(String DesctizioneProblema) {this.DescrizioneProblema = DesctizioneProblema;}
    public void setIdEvento(int IdEvento) {this.IdEvento = IdEvento;}
    public void setOrganizzatore(Organizzatore organizzatore) {this.organizzatore = organizzatore;}
    public void setDate(LocalDate Inizio, LocalDate Fine) {

        if(Inizio.isBefore(Fine)) {
            this.DataInizio = Inizio;
            this.DataFine = Fine;
        }

    }
    public void setDateReg(LocalDate Inizio, LocalDate Fine) {

        if (Inizio.isBefore(Fine) && Fine.isBefore(DataInizio)) {
            this.DataInizioReg = Inizio;
            this.DataFineReg = Fine;
        }
    }

    public int getIdEvento() {return IdEvento;}
    public String getTitolo() {return Titolo;}
    public String getIndirizzoSede() {return IndirizzoSede;}
    public int getNCivicoSede() {return NCivicoSede;}
    public LocalDate getDataInizio() {return DataInizio;}
    public LocalDate getDataFine() {return DataFine;}
    public int getMaxIscritti() {return MaxIscritti;}
    public int getMaxTeam() {return MaxTeam;}
    public String getDescrizioneProblema() { return DescrizioneProblema; }
    public LocalDate getDataInizioReg() {return DataInizioReg;}
    public LocalDate getDataFineReg() {return DataFineReg;}
    public Organizzatore getOrganizzatore() {return organizzatore;}

    public void enqueueListaAttesaUtenti(Partecipante utente) {

        if(RichiestePartecipazioneUtenti == null)
            RichiestePartecipazioneUtenti = new LinkedList<Partecipante>();

        RichiestePartecipazioneUtenti.add(utente);

    }

    public void addPartecipante(Partecipante utente) {

        if(Partecipanti == null)
            Partecipanti = new ArrayList<Partecipante>();

        Partecipanti.add(utente);

    }

    public void DequeueListaAttesaUtenti() {

        if(RichiestePartecipazioneUtenti != null) {
            Partecipante utente = RichiestePartecipazioneUtenti.remove();
            if ((Partecipanti == null) || (Partecipanti.size() < MaxIscritti)) {
                addPartecipante(utente);
                utente.addEvento(this);

            }
        }
    }

    public void enqueueListaAttesaTeam(Team team) {

        if(RichiestePartecipazioneTeam == null)
            RichiestePartecipazioneTeam = new LinkedList<Team>();

        RichiestePartecipazioneTeam.add(team);

    }

    public void addTeam(Team team) {

        if( TeamIscritti == null)
            TeamIscritti = new ArrayList<Team>();

        TeamIscritti.add(team);

    }

    public void dequeueListaAttesaTeam() {

        Team team = RichiestePartecipazioneTeam.remove();
        if((TeamIscritti == null) || (TeamIscritti.size() < MaxTeam)) {
            addTeam(team);

        }
    }
}