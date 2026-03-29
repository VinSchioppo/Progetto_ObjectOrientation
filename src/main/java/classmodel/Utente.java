package classmodel;

import recordlist.*;
import java.time.LocalDate;
import java.util.List;

public class Utente {

    protected String nomeUtente;
    protected String passwordUtente;
    protected String fNome = null;
    protected String mNome = null;
    protected String lNome = null;
    protected LocalDate dataNascita = null;

    protected RecordList<Evento> eventi = null;
    private InviteList<Evento> invitiGiudiceEvento = null;

    public Utente(String nomeUtente, String passwordUtente) {

        this.nomeUtente = nomeUtente;
        this.passwordUtente = passwordUtente;

    }

    public void setDati(String fNome, String mNome, String lNome, LocalDate dataNascita) {

        this.fNome = fNome;
        this.mNome = mNome;
        this.lNome = lNome;
        this.dataNascita = dataNascita;

    }

    public void setNomeUtente(String nomeUtente) {this.nomeUtente = nomeUtente;}
    public void setPasswordUtente(String passwordUtente) {this.passwordUtente = passwordUtente;}
    public void setFNome(String fNome) {this.fNome = fNome;}
    public void setMNome(String mNome) {this.mNome = mNome;}
    public void setLNome(String lNome) {this.lNome = lNome;}
    public void setDataNascita(LocalDate dataNascita) {this.dataNascita = dataNascita;}
    public void setEventi(List<Evento> eventi){
        if(this.eventi == null)
            this.eventi = new RecordList<>();
        this.eventi.setRecords(eventi);
    }

    public String getNomeUtente() {return this.nomeUtente;}
    public String getPasswordUtente() {return this.passwordUtente;}
    public String getFNome() {return this.fNome;}
    public String getMNome() {return this.mNome;}
    public String getLNome() {return this.lNome;}
    public LocalDate getDataNascita() {return this.dataNascita;}


    public Partecipante becomePartecipante(){
        Partecipante partecipante = new Partecipante(nomeUtente, passwordUtente);
        partecipante.setFNome(fNome);
        partecipante.setMNome(mNome);
        partecipante.setLNome(lNome);
        partecipante.setDataNascita(dataNascita);
        return partecipante;
    }

    public Organizzatore becomeOrganizzatore(){
        Organizzatore organizzatore = new Organizzatore(nomeUtente, passwordUtente);
        organizzatore.setFNome(fNome);
        organizzatore.setMNome(mNome);
        organizzatore.setLNome(lNome);
        organizzatore.setDataNascita(dataNascita);
        return organizzatore;
    }

    public Giudice becomeGiudice(){
        Giudice giudice = new Giudice(nomeUtente, passwordUtente);
        giudice.setFNome(fNome);
        giudice.setMNome(mNome);
        giudice.setLNome(lNome);
        giudice.setDataNascita(dataNascita);
        return giudice;
    }

    public int sizeEventi() {
        if(eventi != null)
            return eventi.size();
        return 0;
    }

    public void addEvento(Evento evento) {
        if(eventi == null) {
            eventi = new RecordList<>();
        }
        eventi.addRecord(evento);
    }

    public boolean removeEvento(){
        if(eventi != null){
            eventi.removeRecord();
            return true;
        }
        return false;
    }

    public Evento getEvento(){
        if(eventi != null){
            return eventi.getRecord();
        }
        return null;
    }

    public Evento firstEvento(){
        if(eventi != null){
            return eventi.firstRecord();
        }
        return null;
    }

    public Evento previousEvento(){
        if(eventi != null){
            return eventi.previousRecord();
        }
        return null;
    }

    public Evento nextEvento(){
        if(eventi != null){
            return eventi.nextRecord();
        }
        return null;
    }

    public Evento lastEvento(){
        if(eventi != null){
            return eventi.lastRecord();
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
        if(invitiGiudiceEvento == null)
            invitiGiudiceEvento = new InviteList<>();
        invitiGiudiceEvento.addInvite(evento, answer);
    }

    public void addInvitoGiudiceEvento(Evento evento) {
        if(invitiGiudiceEvento == null)
            invitiGiudiceEvento = new InviteList<>();
        invitiGiudiceEvento.addInvite(evento);
    }

    public boolean removeInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            invitiGiudiceEvento.removeInvite();
            return true;
        }
        return false;
    }

    public void setInvitoGiudiceEventoAnswer(Boolean answer){
        if(invitiGiudiceEvento != null)
            invitiGiudiceEvento.setInviteAnswer(answer);
    }

    @javax.annotation.CheckForNull
    public Boolean getInvitoGiudiceEventoAnswer(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.getInviteAnswer();
        }
        return null;
    }

    public Evento getInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.getInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean firstInvitoGiudiceEventoAnswer(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.firstInviteAnswer();
        }
        return null;
    }

    public Evento firstInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.firstInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean previousInvitoGiudiceEventoAnswer(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.previousInviteAnswer();
        }
        return null;
    }

    public Evento previousInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.previousInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean nextInvitoGiudiceEventoAnswer(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.nextInviteAnswer();
        }
        return null;
    }

    public Evento nextInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.nextInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean lastInvitoGiudiceEventoAnswer(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.lastInviteAnswer();
        }
        return null;
    }

    public Evento lastInvitoGiudiceEvento(){
        if(invitiGiudiceEvento != null){
            return invitiGiudiceEvento.lastInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
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

    @javax.annotation.CheckForNull
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