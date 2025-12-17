package ClassModel;

import java.util.ArrayList;
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
    private boolean prenotazioni = false;

    private Organizzatore organizzatore = null;
    private int currentGiudice = -1;
    private ArrayList<Giudice> Giudici = null;
    private int currentPartecipante = -1;
    private ArrayList<Partecipante> Partecipanti = null;
    private ArrayList<Team> TeamIscritti = null;
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
    public void setPrenotazioni(boolean prenotazioni){this.prenotazioni = prenotazioni;}
    public void setOrganizzatore(Organizzatore organizzatore) {this.organizzatore = organizzatore;}
    public void setGiudici(ArrayList<Giudice> giudici) {Giudici = giudici;}
    public void setPartecipanti(ArrayList<Partecipante> partecipanti) {Partecipanti = partecipanti;}
    public void setTeamIscritti(ArrayList<Team> team){TeamIscritti = team;}
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
    public boolean getPrenotazioni() {return prenotazioni;}

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

    public void removePartecipante(String nomeUtente){
        Partecipante partecipante = firstPartecipante();
        while(partecipante != null){
            if(partecipante.getNomeUtente().equals(nomeUtente)){
                Partecipanti.remove(currentPartecipante);
            }
            else partecipante = nextPartecipante();
        }
    }

    public String getNomePartecipanteCorrente() {
        Partecipante partecipante = getPartecipante();
        if(partecipante != null){
            return partecipante.getNomeUtente();
        }
        else return null;
    }

    public Partecipante getPartecipante(){
        if(currentPartecipante >= 0 && currentPartecipante < Partecipanti.size()){
            return Partecipanti.get(currentPartecipante);
        }
        return null;
    }

    public Partecipante firstPartecipante(){
        currentPartecipante = 0;
        return getPartecipante();
    }

    public Partecipante previousPartecipante(){
        if(currentPartecipante >= 0) {
            currentPartecipante--;
        }
        return getPartecipante();
    }

    public Partecipante nextPartecipante(){
        if(currentPartecipante < Partecipanti.size()) {
            currentPartecipante++;
        }
        return getPartecipante();
    }

    public Partecipante lastPartecipante(){
        currentPartecipante = Partecipanti.size() - 1;
        return getPartecipante();
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

    public void addGiudice(Giudice giudice) {

        if( Giudici == null)
            Giudici = new ArrayList<Giudice>();

        Giudici.add(giudice);
    }

    public void removeGiudice(String nomeUtente){
        Giudice giudice = firstGiudice();
        while(giudice != null){
            if(giudice.getNomeUtente().equals(nomeUtente)){
                Giudici.remove(currentGiudice);
            }
            else giudice = nextGiudice();
        }
    }

    public String getNomeGiudiceCorrente() {
        Giudice giudice = getGiudice();
        if(giudice != null){
            return giudice.getNomeUtente();
        }
        else return null;
    }

    public Giudice getGiudice(){
        if(currentGiudice >= 0 && currentGiudice < Giudici.size()){
            return Giudici.get(currentGiudice);
        }
        return null;
    }

    public Giudice firstGiudice(){
        currentGiudice = 0;
        return getGiudice();
    }

    public Giudice previousGiudice(){
        if(currentGiudice >= 0) {
            currentGiudice--;
        }
        return getGiudice();
    }

    public Giudice nextGiudice(){
        if(currentGiudice < Giudici.size()) {
            currentGiudice++;
        }
        return getGiudice();
    }

    public Giudice lastGiudice(){
        currentGiudice = Giudici.size() - 1;
        return getGiudice();
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

}