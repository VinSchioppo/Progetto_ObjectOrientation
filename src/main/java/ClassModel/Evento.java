package ClassModel;

import java.util.ArrayList;
import RecordList.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

public class Evento {

    private int IdEvento = -1;
    private String Titolo = null;
    private String IndirizzoSede = null;
    private int NCivicoSede = -1;
    private LocalDate DataInizio = null;
    private LocalDate DataFine = null;
    private int MaxIscritti = -1;
    private int MaxTeam = -1;
    private String DescrizioneProblema = null;
    private LocalDate DataInizioReg = null;
    private LocalDate DataFineReg = null;
    private boolean Prenotazioni = false;

    private Organizzatore organizzatore = null;
    private RecordList<Giudice> Giudici = null;
    private RecordList<Partecipante> Partecipanti = null;
    private RecordList<Team> TeamIscritti = null;
    private Queue<Partecipante> RichiestePartecipazioneUtenti = null;
    private Queue<Team> RichiestePartecipazioneTeam = null;

    public Evento(int IdEvento) {this.IdEvento = IdEvento;}
    public Evento(String Titolo, String Indirizzo, int NCivico) {
        this.Titolo = Titolo;
        this.IndirizzoSede = Indirizzo;
        this.NCivicoSede = NCivico;
    }

    public void setTitolo(String Titolo) {this.Titolo = Titolo;}
    public void setIndirizzoSede(String sede) {this.IndirizzoSede = sede;}
    public void setNCivicoSede(int sede) {this.NCivicoSede = sede;}
    public void setMaxIscritti(int MaxIscritti) {this.MaxIscritti = MaxIscritti;}
    public void setMaxTeam(int MaxTeam) {this.MaxTeam = MaxTeam;}
    public void setDescrizioneProblema(String DesctizioneProblema) {this.DescrizioneProblema = DesctizioneProblema;}
    public void setIdEvento(int IdEvento) {this.IdEvento = IdEvento;}
    public void setPrenotazioni(boolean prenotazioni){this.Prenotazioni = prenotazioni;}
    public void setOrganizzatore(Organizzatore organizzatore) {this.organizzatore = organizzatore;}
    public void setGiudici(ArrayList<Giudice> giudici) {
        if(Giudici == null)
            Giudici = new RecordList<Giudice>();
        Giudici.setRecords(giudici);
    }
    public void setPartecipanti(ArrayList<Partecipante> partecipanti) {
        if(Partecipanti == null)
            Partecipanti = new RecordList<Partecipante>();
        Partecipanti.setRecords(partecipanti);
    }
    public void setTeamIscritti(ArrayList<Team> team){
        if(TeamIscritti == null)
            TeamIscritti = new RecordList<Team>();
        TeamIscritti.setRecords(team);
    }
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
    public boolean getPrenotazioni() {return Prenotazioni;}

    public void enqueueListaAttesaUtenti(Partecipante utente) {

        if(RichiestePartecipazioneUtenti == null)
            RichiestePartecipazioneUtenti = new LinkedList<Partecipante>();

        RichiestePartecipazioneUtenti.add(utente);

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

    public void addPartecipante(Partecipante utente) {
        if(Partecipanti == null)
            Partecipanti = new RecordList<Partecipante>();
        Partecipanti.addRecord(utente);
    }

    public boolean removePartecipante(String nomeUtente){
        if(Partecipanti != null){
            seekPartecipante(nomeUtente);
            Partecipanti.removeRecord();
            return true;
        }
        return false;
    }

    public Partecipante getPartecipante(){
        if(Partecipanti != null){
            return Partecipanti.getRecord();
        }
        return null;
    }

    public Partecipante firstPartecipante(){
        if(Partecipanti != null){
            return Partecipanti.firstRecord();
        }
        return null;
    }

    public Partecipante previousPartecipante(){
        if(Partecipanti != null){
            return Partecipanti.previousRecord();
        }
        return null;
    }

    public Partecipante nextPartecipante(){
        if(Partecipanti != null){
            return Partecipanti.nextRecord();
        }
        return null;
    }

    public Partecipante lastPartecipante(){
        if(Partecipanti != null){
            return Partecipanti.lastRecord();
        }
        return null;
    }

    public Partecipante seekPartecipante(String nomeUtente){
        Partecipante partecipante = firstPartecipante();
        while(partecipante != null){
            if(partecipante.getNomeUtente().equals(nomeUtente)) {
                return partecipante;
            }
            else partecipante = nextPartecipante();
        }
        return null;
    }

    public void addGiudice(Giudice giudice) {
        if(Giudici == null)
            Giudici = new RecordList<Giudice>();
        Giudici.addRecord(giudice);
    }

    public boolean removeGiudice(String nomeUtente){
        if(Giudici != null){
            seekGiudice(nomeUtente);
            Giudici.removeRecord();
            return true;
        }
        return false;
    }

    public Giudice getGiudice(){
        if(Giudici != null){
            return Giudici.getRecord();
        }
        return null;
    }

    public Giudice firstGiudice(){
        if(Giudici != null){
            return Giudici.firstRecord();
        }
        return null;
    }

    public Giudice previousGiudice(){
        if(Giudici != null){
            return Giudici.previousRecord();
        }
        return null;
    }

    public Giudice nextGiudice(){
        if(Giudici != null){
            return Giudici.nextRecord();
        }
        return null;
    }

    public Giudice lastGiudice(){
        if(Giudici != null){
            return Giudici.lastRecord();
        }
        return null;
    }

    public Giudice seekGiudice(String nomeUtente){
        Giudice giudice = firstGiudice();
        while(giudice != null){
            if(giudice.getNomeUtente().equals(nomeUtente)) {
                return giudice;
            }
            else giudice = nextGiudice();
        }
        return null;
    }

    public void enqueueListaAttesaTeam(Team team) {

        if(RichiestePartecipazioneTeam == null)
            RichiestePartecipazioneTeam = new LinkedList<Team>();

        RichiestePartecipazioneTeam.add(team);

    }

    public void dequeueListaAttesaTeam() {

        Team team = RichiestePartecipazioneTeam.remove();
        if((TeamIscritti == null) || (TeamIscritti.size() < MaxTeam)) {
            addTeam(team);

        }
    }

    public void addTeam(Team team) {

        if(TeamIscritti == null)
            TeamIscritti = new RecordList<Team>();
        TeamIscritti.addRecord(team);

    }

    public boolean removeTeam(int idTeam) {
        if(TeamIscritti != null){
            seekTeam(idTeam);
            TeamIscritti.removeRecord();
            return true;
        }
        return false;
    }

    public Team getTeam(){
        if(TeamIscritti != null){
            return TeamIscritti.getRecord();
        }
        return null;
    }

    public Team firstTeam(){
        if(TeamIscritti != null){
            return TeamIscritti.firstRecord();
        }
        return null;
    }

    public Team previousTeam(){
        if(TeamIscritti != null){
            return TeamIscritti.previousRecord();
        }
        return null;
    }

    public Team nextTeam(){
        if(TeamIscritti != null){
            return TeamIscritti.nextRecord();
        }
        return null;
    }

    public Team lastTeam(){
        if(TeamIscritti != null){
            return TeamIscritti.lastRecord();
        }
        return null;
    }

    public Team seekTeam(int idTeam) {
        Team team = firstTeam();
        while(team != null){
            if(team.getIdTeam() == idTeam) {
                return team;
            }
            else team = nextTeam();
        }
        return null;
    }

}