package ClassModel;

import RecordList.RecordList;
import java.time.LocalDate;
import java.util.ArrayList;

public class Utente {

    protected String NomeUtente;
    protected String PasswordUtente;
    protected String FNome = null;
    protected String MNome = null;
    protected String LNome = null;
    protected LocalDate DataNascita = null;

    private RecordList<Evento> Eventi = null;

    public Utente(){}

    public Utente(String NomeUtente, String PasswordUtente) {

        this.NomeUtente = NomeUtente;
        this.PasswordUtente = PasswordUtente;

    }

    public void setDati(String FNome, String MNome, String LNome, LocalDate DataNascita) {

        this.FNome = FNome;
        this.MNome = MNome;
        this.LNome = LNome;
        this.DataNascita = DataNascita;

    }

    public void setNomeUtente(String NomeUtente) {this.NomeUtente = NomeUtente;}
    public void setPasswordUtente(String PasswordUtente) {this.PasswordUtente = PasswordUtente;}
    public void setFNome(String FNome) {this.FNome = FNome;}
    public void setMNome(String MNome) {this.MNome = MNome;}
    public void setLNome(String LNome) {this.LNome = LNome;}
    public void setDataNascita(LocalDate DataNascita) {this.DataNascita = DataNascita;}
    public void setEventi(ArrayList<Evento> eventi){
        if(Eventi == null)
            Eventi = new RecordList<Evento>();
        Eventi.setRecords(eventi);
    }

    public String getNomeUtente() {return this.NomeUtente;}
    public String getPasswordUtente() {return this.PasswordUtente;}
    public String getFNome() {return this.FNome;}
    public String getMNome() {return this.MNome;}
    public String getLNome() {return this.LNome;}
    public LocalDate getDataNascita() {return this.DataNascita;}

    public void addEvento(Evento evento) {
        if(Eventi == null) {
            Eventi = new RecordList<Evento>();
        }
        Eventi.addRecord(evento);
    }

    public boolean removeEvento(int idEvento){
        if(Eventi != null){
            seekEvento(idEvento);
            Eventi.removeRecord();
            return true;
        }
        return false;
    }

    public Evento getEvento(){
        if(Eventi != null){
            return Eventi.getRecord();
        }
        return null;
    }

    public Evento firstEvento(){
        if(Eventi != null){
            return Eventi.firstRecord();
        }
        return null;
    }

    public Evento previousEvento(){
        if(Eventi != null){
            return Eventi.previousRecord();
        }
        return null;
    }

    public Evento nextEvento(){
        if(Eventi != null){
            return Eventi.nextRecord();
        }
        return null;
    }

    public Evento lastEvento(){
        if(Eventi != null){
            return Eventi.lastRecord();
        }
        return null;
    }

    public Evento seekEvento(int idEvento) {
        Evento evento = firstEvento();
        while(evento != null){
            if(evento.getIdEvento() == idEvento) {
                return evento;
            }
            else evento = nextEvento();
        }
        return null;
    }

    public void printDati() {

        System.out.println("Nome ClassModel.Utente: " + this.NomeUtente);
        System.out.println("\nNome: " + this.FNome);

        if(!this.MNome.isEmpty())
            System.out.println(" " + this.MNome);

        System.out.println(" " + this.LNome);
        System.out.println("\nData di Nascita: " + this.DataNascita);

    }

    public Partecipante becomePartecipante(){
        Partecipante partecipante = new Partecipante(NomeUtente, PasswordUtente);
        partecipante.setFNome(FNome);
        partecipante.setMNome(MNome);
        partecipante.setLNome(LNome);
        partecipante.setDataNascita(DataNascita);
        return partecipante;
    }

    public Organizzatore becomeOrganizzatore(){
        Organizzatore organizzatore = new Organizzatore(NomeUtente, PasswordUtente);
        organizzatore.setFNome(FNome);
        organizzatore.setMNome(MNome);
        organizzatore.setLNome(LNome);
        organizzatore.setDataNascita(DataNascita);
        return organizzatore;
    }

    public Giudice becomeGiudice(){
        Giudice giudice = new Giudice(NomeUtente, PasswordUtente);
        giudice.setFNome(FNome);
        giudice.setMNome(MNome);
        giudice.setLNome(LNome);
        giudice.setDataNascita(DataNascita);
        return giudice;
    }

}