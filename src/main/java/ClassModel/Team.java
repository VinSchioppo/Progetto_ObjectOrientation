package ClassModel;

import RecordList.*;
import java.util.List;

public class Team {

    private int idTeam;
    private String nome;
    private String teamLeader = null;

    private RecordList<Partecipante> membriTeam = null;
    private Evento eventoIscritto = null;
    private InviteList<Partecipante> richiestePartecipazione = null;
    private RecordList<Progresso> progressi = null;
    private RecordList<Voto> voti = null;

    public Team(int idTeam, String nome, String teamLeader) {
        this.idTeam = idTeam;
        this.nome = nome;
        this.teamLeader = teamLeader;
    }

    public void setNome(String nome) {this.nome = nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setTeamLeader(String teamLeader) {this.teamLeader = teamLeader;}
    public void setMembriTeam(List<Partecipante> partecipanti) {
        if(membriTeam == null)
            membriTeam = new RecordList<Partecipante>();
        membriTeam.setRecords(partecipanti);
    }

    public void setProgressi(List<Progresso> progressi) {
        if(this.progressi == null)
            this.progressi = new RecordList<Progresso>();
        this.progressi.setRecords(progressi);
    }

    public void setVoti(List<Voto> voti){
        if(this.voti == null)
            this.voti = new RecordList<Voto>();
        this.voti.setRecords(voti);
    }

    public void setEventoIscritto(Evento eventoIscritto) { this.eventoIscritto = eventoIscritto;}

    public void setRichiestePartecipazione(List<Partecipante> richiestePartecipazione) {
        if(this.richiestePartecipazione == null)
            this.richiestePartecipazione = new InviteList<Partecipante>();
        this.richiestePartecipazione.setInvites(richiestePartecipazione);
    }

    public int getIdTeam() {return this.idTeam;}
    public String getNome() {return this.nome;}
    public String getTeamLeader() {return this.teamLeader;}
    public Evento getEventoIscritto() {return this.eventoIscritto;}

    public void addMembroTeam(Partecipante part) {

        if(membriTeam == null)
            membriTeam = new RecordList<Partecipante>();
        membriTeam.addRecord(part);
    }

    public boolean removeMembroTeam(){
        if(membriTeam != null){
            membriTeam.removeRecord();
            return true;
        }
        return false;
    }

    public boolean removeMembroTeam(String nomeUtente){
        seekMembroTeam(nomeUtente);
        return removeMembroTeam();
    }

    public Partecipante getMembroTeam(){
        if(membriTeam != null){
            return membriTeam.getRecord();
        }
        return null;
    }

    public Partecipante firstMembroTeam(){
        if(membriTeam != null){
            return membriTeam.firstRecord();
        }
        return null;
    }

    public Partecipante previousMembroTeam(){
        if(membriTeam != null){
            return membriTeam.previousRecord();
        }
        return null;
    }

    public Partecipante nextMembroTeam(){
        if(membriTeam != null){
            return membriTeam.nextRecord();
        }
        return null;
    }

    public Partecipante lastMembroTeam(){
        if(membriTeam != null){
            return membriTeam.lastRecord();
        }
        return null;
    }

    public Partecipante seekMembroTeam(String nomeUtente){
        Partecipante membro = firstMembroTeam();
        while(membro != null){
            if(membro.getNomeUtente().equals(nomeUtente)) {
                return membro;
            }
            else membro = nextMembroTeam();
        }
        return null;
    }

    public Partecipante seekAndRemoveMembroTeam(String nomeUtente){
        Partecipante membro = seekMembroTeam(nomeUtente);
        removeMembroTeam();
        return membro;
    }

    public int giveNumMembri() {
        if(membriTeam != null) {
            return membriTeam.size();
        }
        return 0;
    }

    public void addRichiesta(Partecipante partecipante, Boolean answer) {
        if(richiestePartecipazione == null)
            richiestePartecipazione = new InviteList<Partecipante>();
        richiestePartecipazione.addInvite(partecipante, answer);
    }

    public void addRichiesta(Partecipante partecipante) {
        if(richiestePartecipazione == null)
            richiestePartecipazione = new InviteList<Partecipante>();
        richiestePartecipazione.addInvite(partecipante);
    }

    public boolean removeRichiesta(){
        if(richiestePartecipazione != null){
            richiestePartecipazione.removeInvite();
            return true;
        }
        return false;
    }

    public void setRichiestaAnswer(Boolean answer) {

        if(richiestePartecipazione != null)
            richiestePartecipazione.setInviteAnswer(answer);
    }

    @javax.annotation.CheckForNull
    public Boolean getRichiestaAnswer(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.getInviteAnswer();
        }
        return null;
    }

    public Partecipante getRichiesta(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.getInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean firstRichiestaAnswer(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.firstInviteAnswer();
        }
        return null;
    }

    public Partecipante firstRichiesta(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.firstInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean previousRichiestaAnswer(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.previousInviteAnswer();
        }
        return null;
    }

    public Partecipante previousRichiesta(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.previousInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean nextRichiestaAnswer(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.nextInviteAnswer();
        }
        return null;
    }

    public Partecipante nextRichiesta(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.nextInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean lastRichiestaAnswer(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.lastInviteAnswer();
        }
        return null;
    }

    public Partecipante lastRichiesta(){
        if(richiestePartecipazione != null){
            return richiestePartecipazione.lastInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean seekRichiestaAnswer(String nomeUtente){
        Partecipante richiesta = firstRichiesta();
        while(richiesta != null){
            if(richiesta.getNomeUtente().equals(nomeUtente)) {
                return getRichiestaAnswer();
            }
            else richiesta = nextRichiesta();
        }
        return null;
    }

    public Partecipante seekRichiesta(String nomeUtente){
        Partecipante richiesta = firstRichiesta();
        while(richiesta != null){
            if(richiesta.getNomeUtente().equals(nomeUtente)) {
                return richiesta;
            }
            else richiesta = nextRichiesta();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean seekAndRemoveRichiestaAnswer(String nomeUtente){
        seekRichiestaAnswer(nomeUtente);
        removeRichiesta();
        return getRichiestaAnswer();
    }

    public Partecipante seekAndRemoveRichiesta(String nomeUtente){

        Partecipante richiesta = seekRichiesta(nomeUtente);
        removeRichiesta();
        return richiesta;
    }


    public void addProgresso(Progresso progressi) {

        if(this.progressi == null)
            this.progressi = new RecordList<Progresso>();

        this.progressi.addRecord(progressi);
    }

    public boolean removeProgresso(){
        if(progressi != null){
            progressi.removeRecord();
            return true;
        }
        return false;
    }

    public Progresso getProgresso(){
        if(progressi != null){
            return progressi.getRecord();
        }
        return null;
    }

    public Progresso firstProgresso(){
        if(progressi != null){
            return progressi.firstRecord();
        }
        return null;
    }

    public Progresso previousProgresso(){
        if(progressi != null){
            return progressi.previousRecord();
        }
        return null;
    }

    public Progresso nextProgresso(){
        if(progressi != null){
            return progressi.nextRecord();
        }
        return null;
    }

    public Progresso lastProgresso(){
        if(progressi != null){
            return progressi.lastRecord();
        }
        return null;
    }

    public Progresso seekProgresso(int idProgresso){
        Progresso progresso = firstProgresso();
        while(progresso != null){
            if(progresso.getIdProgresso() == idProgresso) {
                return progresso;
            }
            else progresso = nextProgresso();
        }
        return null;
    }

    public Progresso seekAndRemoveProgresso(int idProgresso){
        Progresso progresso = seekProgresso(idProgresso);
        removeProgresso();
        return progresso;
    }


    public void addVoto(Voto voto) {

        if(voti == null)
            voti = new RecordList<Voto>();

        voti.addRecord(voto);
    }

    public boolean removeVoto(){
        if(voti != null){
            voti.removeRecord();
            return true;
        }
        return false;
    }

    public Voto getVoto(){
        if(voti != null){
            return voti.getRecord();
        }
        return null;
    }

    public Voto firstVoto(){
        if(voti != null){
            return voti.firstRecord();
        }
        return null;
    }

    public Voto previousVoto(){
        if(voti != null){
            return voti.previousRecord();
        }
        return null;
    }

    public Voto nextVoto(){
        if(voti != null){
            return voti.nextRecord();
        }
        return null;
    }

    public Voto lastVoto(){
        if(voti != null){
            return voti.lastRecord();
        }
        return null;
    }

    public Voto seekVoto(String nomeGiudice){
        Voto voto = firstVoto();
        while(voto != null){
            if(voto.getGiudice().equals(nomeGiudice)) {
                return voto;
            }
            else voto = nextVoto();
        }
        return null;
    }

    public Voto seekAndRemoveVoto(String nomeGiudice){
        Voto voto = seekVoto(nomeGiudice);
        removeVoto();
        return voto;
    }

    public int mediaVoti()
    {
        int media = 0;
        if(voti.size() > 0) {
            Voto voto = firstVoto();
            while (voto != null) {
                media += voto.getValore();
                voto = nextVoto();
            }
            media = media / voti.size();
        }
        return media;
    }
}