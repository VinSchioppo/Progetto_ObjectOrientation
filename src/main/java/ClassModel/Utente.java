package ClassModel;

import RecordList.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Utente {

    protected String NomeUtente;
    protected String PasswordUtente;
    protected String FNome = null;
    protected String MNome = null;
    protected String LNome = null;
    protected LocalDate DataNascita = null;

    protected RecordList<Evento> Eventi = null;
    private InviteList<Evento> InvitiGiudiceEvento = null;

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

    public void setInvitoGiudiceEvento(ArrayList<Evento> inviti) {
        if(InvitiGiudiceEvento == null)
            InvitiGiudiceEvento = new InviteList<Evento>();
        InvitiGiudiceEvento.setInvites(inviti);
    }

    public String getNomeUtente() {return this.NomeUtente;}
    public String getPasswordUtente() {return this.PasswordUtente;}
    public String getFNome() {return this.FNome;}
    public String getMNome() {return this.MNome;}
    public String getLNome() {return this.LNome;}
    public LocalDate getDataNascita() {return this.DataNascita;}


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

    public int sizeEventi() {
        if(Eventi != null)
            return Eventi.size();
        return 0;
    }

    public void addEvento(Evento evento) {
        if(Eventi == null) {
            Eventi = new RecordList<Evento>();
        }
        Eventi.addRecord(evento);
    }

    public boolean removeEvento(){
        if(Eventi != null){
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

    public Evento seekAndRemoveEvento(int idEvento){
        Evento evento = seekEvento(idEvento);
        removeEvento();
        return evento;
    }

    public void addInvitoGiudiceEvento(Evento evento, Boolean answer) {
        if(InvitiGiudiceEvento == null)
            InvitiGiudiceEvento = new InviteList<Evento>();
        InvitiGiudiceEvento.addInvite(evento, answer);
    }

    public void addInvitoGiudiceEvento(Evento evento) {
        if(InvitiGiudiceEvento == null)
            InvitiGiudiceEvento = new InviteList<Evento>();
        InvitiGiudiceEvento.addInvite(evento);
    }

    public boolean removeInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            InvitiGiudiceEvento.removeInvite();
            return true;
        }
        return false;
    }

    public void setInvitoGiudiceEventoAnswer(Boolean answer){
        if(InvitiGiudiceEvento != null)
            InvitiGiudiceEvento.setInviteAnswer(answer);
    }

    public Boolean getInvitoGiudiceEventoAnswer(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.getInviteAnswer();
        }
        return null;
    }

    public Evento getInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.getInvite();
        }
        return null;
    }

    public Boolean firstInvitoGiudiceEventoAnswer(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.firstInviteAnswer();
        }
        return null;
    }

    public Evento firstInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.firstInvite();
        }
        return null;
    }

    public Boolean previousInvitoGiudiceEventoAnswer(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.previousInviteAnswer();
        }
        return null;
    }

    public Evento previousInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.previousInvite();
        }
        return null;
    }

    public Boolean nextInvitoGiudiceEventoAnswer(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.nextInviteAnswer();
        }
        return null;
    }

    public Evento nextInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.nextInvite();
        }
        return null;
    }

    public Boolean lastInvitoGiudiceEventoAnswer(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.lastInviteAnswer();
        }
        return null;
    }

    public Evento lastInvitoGiudiceEvento(){
        if(InvitiGiudiceEvento != null){
            return InvitiGiudiceEvento.lastInvite();
        }
        return null;
    }

    public Boolean seekInvitoGiudiceEventoAnswer(int idEvento){
        Evento evento = firstInvitoGiudiceEvento();
        while(evento != null){
            if(evento.getIdEvento() == idEvento) {
                return getInvitoGiudiceEventoAnswer();
            }
            else evento = nextInvitoGiudiceEvento();
        }
        return null;
    }

    public Evento seekInvitoGiudiceEvento(int idEvento){
        Evento evento = firstInvitoGiudiceEvento();
        while(evento != null){
            if(evento.getIdEvento() == idEvento) {
                return evento;
            }
            else evento = nextInvitoGiudiceEvento();
        }
        return null;
    }

    public Boolean seekAndRemoveInvitoGiudiceEventoAnswer(int idEvento){
        Boolean answer = seekInvitoGiudiceEventoAnswer(idEvento);
        removeInvitoGiudiceEvento();
        return answer;
    }

    public Evento seekAndRemoveInvitoGiudiceEvento(int idEvento){
        Evento evento = seekInvitoGiudiceEvento(idEvento);
        removeInvitoGiudiceEvento();
        return evento;
    }

}