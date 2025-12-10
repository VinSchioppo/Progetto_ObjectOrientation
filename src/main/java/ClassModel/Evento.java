package ClassModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Evento {

    private int IdEvento;
    private String Titolo;
    private String IndirizzoSede;
    private int NCivicoSede;
    private Date DataInizio;
    private Date DataFine;
    private int MaxIscritti;
    private int MaxTeam;
    private String DescrizioneProblema;
    private Date DataInizioReg;
    private Date DataFineReg;

    Organizzatore organizzatore = null;
    LinkedList<Giudice> Giudici = null;
    Queue<Partecipante> RichiestePartecipazioneUtenti = null;
    Queue<Team> RichiestePartecipazioneTeam = null;
    LinkedList<Partecipante> Partecipanti = null;
    LinkedList<Team> TeamIscritti = null;
    boolean prenotazioni = false;

    public Evento(String Titolo) {this.Titolo = Titolo;}
    public void setIdEvento(int idEvento) {IdEvento = idEvento;}
    public void setIndirizzoSede(String sede) {this.IndirizzoSede = sede;}
    public void setNCivicoSede(int sede) {this.NCivicoSede = sede;}
    public void setMaxIscritti(int MaxIscritti) {this.MaxIscritti = MaxIscritti;}
    public void setMaxTeam(int MaxTeam) {this.MaxTeam = MaxTeam;}
    public void setDescrizioneProblema(String DesctizioneProblema) {this.DescrizioneProblema = DesctizioneProblema;}
    public void setDate(Date Inizio, Date Fine) {

        if(Inizio.before(Fine)) {
            this.DataInizio = Inizio;
            this.DataFine = Fine;
        }

    }
    public void setDateReg(Date Inizio, Date Fine) {

        if (Inizio.before(Fine) && Fine.before(DataInizio)) {
            this.DataInizioReg = Inizio;
            this.DataFineReg = Fine;
        }
    }

    public int getIdEvento() {return IdEvento;}
    public String getTitolo() {return Titolo;}
    public String getIndirizzoSede() {return IndirizzoSede;}
    public int getNCivicoSede() {return NCivicoSede;}
    public Date getDataInizio() {return DataInizio;}
    public Date getDataFine() {return DataFine;}
    public int getMaxIscritti() {return MaxIscritti;}
    public int getMaxTeam() {return MaxTeam;}
    public String getDescrizioneProblema() { return DescrizioneProblema; }
    public Date getDateReg() {return DataInizioReg;}
    public Date getDateFineReg() {return DataFineReg;}

    public void enqueueListaAttesaUtenti(Partecipante utente) {

        if(RichiestePartecipazioneUtenti == null)
            RichiestePartecipazioneUtenti = new LinkedList<Partecipante>();

        RichiestePartecipazioneUtenti.add(utente);

    }

    public void addPartecipante(Partecipante utente) {

        if(Partecipanti == null)
            Partecipanti = new LinkedList<Partecipante>();

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
            TeamIscritti = new LinkedList<Team>();

        TeamIscritti.add(team);

    }

    public void dequeueListaAttesaTeam() {

        Team team = RichiestePartecipazioneTeam.remove();
        if((TeamIscritti == null) || (TeamIscritti.size() < MaxTeam)) {
            addTeam(team);
            team.addEvento(this);

        }
    }
}