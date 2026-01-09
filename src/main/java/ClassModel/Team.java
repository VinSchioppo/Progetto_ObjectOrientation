package ClassModel;

import RecordList.*;
import java.util.ArrayList;

public class Team {

    private int idTeam;
    private String Nome;
    private String TeamLeader = null;

    private RecordList<Partecipante> MembriTeam = null;
    private Evento EventoIscritto = null;
    private InviteList<Partecipante> RichiestePartecipazione = null;
    private RecordList<Progresso> Progressi = null;
    private RecordList<Voto> Voti = null;

    public Team(int idTeam) {this.idTeam = idTeam;}

    public Team(int idTeam, String Nome, String TeamLeader) {
        this.idTeam = idTeam;
        this.Nome = Nome;
        this.TeamLeader = TeamLeader;
    }

    public Team(String Nome, Evento EventoIscritto) {

        this.Nome = Nome;
    }

    public void setNome(String Nome) {this.Nome = Nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setMembriTeam(ArrayList<Partecipante> partecipanti) {
        if(MembriTeam == null)
            MembriTeam = new RecordList<Partecipante>();
        MembriTeam.setRecords(partecipanti);
    }

    public void setProgressi(ArrayList<Progresso> progressi) {
        if(Progressi == null)
            Progressi = new RecordList<Progresso>();
        Progressi.setRecords(progressi);
    }

    public void setVoti(ArrayList<Voto> voti){
        if(Voti == null)
            Voti = new RecordList<Voto>();
        Voti.setRecords(voti);
    }

    public void setEventoIscritto(Evento EventoIscritto) {
        this.EventoIscritto = EventoIscritto;
        EventoIscritto.addTeam(this);
    }

    public void setRichiestePartecipazione(ArrayList<Partecipante> richiestePartecipazione) {
        if(RichiestePartecipazione == null)
            RichiestePartecipazione = new InviteList<Partecipante>();
        RichiestePartecipazione.setInvites(richiestePartecipazione);
    }
    public void setRichiestaAnswer(Boolean answer) {

        if(RichiestePartecipazione != null)
            RichiestePartecipazione.setInviteAnswer(answer);
    }

    public int getIdTeam() {return this.idTeam;}
    public String getNome() {return this.Nome;}
    public String getTeamLeader() {return this.TeamLeader;}
    public Evento getEventoIscritto() {return this.EventoIscritto;}

    public void addMembroTeam(Partecipante part) {

        if(MembriTeam == null)
            MembriTeam = new RecordList<Partecipante>();
        MembriTeam.addRecord(part);
    }

    public boolean removeMembroTeam(String nomeUtente){
        if(MembriTeam != null){
            seekMembroTeam(nomeUtente);
            MembriTeam.removeRecord();
            return true;
        }
        return false;
    }

    public Partecipante getMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.getRecord();
        }
        return null;
    }

    public Partecipante firstMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.firstRecord();
        }
        return null;
    }

    public Partecipante previousMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.previousRecord();
        }
        return null;
    }

    public Partecipante nextMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.nextRecord();
        }
        return null;
    }

    public Partecipante lastMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.lastRecord();
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
        if(MembriTeam != null){
            Partecipante membro = seekMembroTeam(nomeUtente);
            MembriTeam.removeRecord();
            return membro;
        }
        return null;
    }

    public int giveNumMembri() {
        if(MembriTeam != null) {
            return MembriTeam.size();
        }
        return 0;
    }

    public void addRichiesta(Partecipante part) {

        if(RichiestePartecipazione == null)
            RichiestePartecipazione = new InviteList<Partecipante>();
        RichiestePartecipazione.addInvite(part);
    }

    public boolean removeRichiesta(String nomeUtente){
        if(RichiestePartecipazione != null){
            seekRichiesta(nomeUtente);
            RichiestePartecipazione.removeInvite();
            return true;
        }
        return false;
    }

    public Boolean getRichiestaAnswer(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.getInviteAnswer();
        }
        return null;
    }

    public Partecipante getRichiesta(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.getInvite();
        }
        return null;
    }

    public Boolean firstRichiestaAnswer(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.firstInviteAnswer();
        }
        return null;
    }

    public Partecipante firstRichiesta(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.firstInvite();
        }
        return null;
    }

    public Boolean previousRichiestaAnswer(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.previousInviteAnswer();
        }
        return null;
    }

    public Partecipante previousRichiesta(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.previousInvite();
        }
        return null;
    }

    public Boolean nextRichiestaAnswer(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.nextInviteAnswer();
        }
        return null;
    }

    public Partecipante nextRichiesta(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.nextInvite();
        }
        return null;
    }

    public Boolean lastRichiestaAnswer(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.lastInviteAnswer();
        }
        return null;
    }

    public Partecipante lastRichiesta(){
        if(RichiestePartecipazione != null){
            return RichiestePartecipazione.lastInvite();
        }
        return null;
    }

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

    public Boolean seekAndRemoveRichiestaAnswer(String nomeUtente){
        if(RichiestePartecipazione != null){
            Partecipante richiesta = seekRichiesta(nomeUtente);
            RichiestePartecipazione.removeInvite();
            return getRichiestaAnswer();
        }
        return null;
    }

    public Partecipante seekAndRemoveRichiesta(String nomeUtente){
        if(RichiestePartecipazione != null){
            Partecipante richiesta = seekRichiesta(nomeUtente);
            RichiestePartecipazione.removeInvite();
            return richiesta;
        }
        return null;
    }


    public void addProgresso(Progresso progressi) {

        if(Progressi == null)
            Progressi = new RecordList<Progresso>();

        Progressi.addRecord(progressi);
    }

    public boolean removeProgresso(int idProgresso){
        if(Progressi != null){
            seekProgresso(idProgresso);
            Progressi.removeRecord();
            return true;
        }
        return false;
    }

    public Progresso getProgresso(){
        if(Progressi != null){
            return Progressi.getRecord();
        }
        return null;
    }

    public Progresso firstProgresso(){
        if(Progressi != null){
            return Progressi.firstRecord();
        }
        return null;
    }

    public Progresso previousProgresso(){
        if(Progressi != null){
            return Progressi.previousRecord();
        }
        return null;
    }

    public Progresso nextProgresso(){
        if(Progressi != null){
            return Progressi.nextRecord();
        }
        return null;
    }

    public Progresso lastProgresso(){
        if(Progressi != null){
            return Progressi.lastRecord();
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


    public void addVoto(Voto voto) {

        if(Voti == null)
            Voti = new RecordList<Voto>();

        Voti.addRecord(voto);
    }

    public boolean removeVoto(String nomeGiudice){
        if(Voti != null){
            seekVoto(nomeGiudice);
            Voti.removeRecord();
            return true;
        }
        return false;
    }

    public Voto getVoto(){
        if(Voti != null){
            return Voti.getRecord();
        }
        return null;
    }

    public Voto firstVoto(){
        if(Voti != null){
            return Voti.firstRecord();
        }
        return null;
    }

    public Voto previousVoto(){
        if(Voti != null){
            return Voti.previousRecord();
        }
        return null;
    }

    public Voto nextVoto(){
        if(Voti != null){
            return Voti.nextRecord();
        }
        return null;
    }

    public Voto lastVoto(){
        if(Voti != null){
            return Voti.lastRecord();
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

    public void printMembri() {
        firstMembroTeam();
        while(getMembroTeam() != null)
        {
            System.out.println(getMembroTeam().getNomeUtente());
            nextMembroTeam();
        }
    }
}