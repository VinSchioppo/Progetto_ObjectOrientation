package classmodel;

import recordlist.*;
import java.time.LocalDate;
import java.util.List;

public class Evento {

    private int idEvento = -1;
    private String titolo = null;
    private String indirizzoSede = null;
    private int nCivicoSede = -1;
    private LocalDate dataInizio = null;
    private LocalDate dataFine = null;
    private int maxIscritti = -1;
    private int maxTeam = -1;
    private LocalDate dataInizioReg = null;
    private LocalDate dataFineReg = null;
    private String descrizioneProblema = null;

    private Organizzatore organizzatore = null;
    private InviteList<Partecipante> invitiGiudici = null;
    private RecordList<Giudice> giudici = null;
    private RecordList<Partecipante> partecipanti = null;
    private RecordList<Team> teamIscritti = null;

    public Evento(int idEvento) {this.idEvento = idEvento;}
    public Evento(int idEvento, String titolo, String indirizzo, int nCivico) {
        this.idEvento = idEvento;
        this.titolo = titolo;
        this.indirizzoSede = indirizzo;
        this.nCivicoSede = nCivico;
    }
    public Evento(String titolo) {this.titolo = titolo;}
    public Evento(String titolo, String indirizzo, int nCivico) {
        this.titolo = titolo;
        this.indirizzoSede = indirizzo;
        this.nCivicoSede = nCivico;
    }

    public void setTitolo(String titolo) {this.titolo = titolo;}
    public void setIndirizzoSede(String sede) {this.indirizzoSede = sede;}
    public void setnCivicoSede(int sede) {this.nCivicoSede = sede;}
    public void setMaxIscritti(int maxIscritti) {this.maxIscritti = maxIscritti;}
    public void setMaxTeam(int maxTeam) {this.maxTeam = maxTeam;}
    public void setDescrizioneProblema(String desctizioneProblema) {this.descrizioneProblema = desctizioneProblema;}
    public void setIdEvento(int idEvento) {this.idEvento = idEvento;}
    public void setOrganizzatore(Organizzatore organizzatore) {this.organizzatore = organizzatore;}

    public void setInvitiGiudici(List<Partecipante> inviti) {
        if(invitiGiudici == null)
            invitiGiudici = new InviteList<Partecipante>();
        invitiGiudici.setInvites(inviti);
    }

    public void setGiudici(List<Giudice> giudici) {
        if(this.giudici == null)
            this.giudici = new RecordList<Giudice>();
        this.giudici.setRecords(giudici);
    }
    public void setPartecipanti(List<Partecipante> partecipanti) {
        if(this.partecipanti == null)
            this.partecipanti = new RecordList<Partecipante>();
        this.partecipanti.setRecords(partecipanti);
    }
    public void setTeamIscritti(List<Team> team){
        if(teamIscritti == null)
            teamIscritti = new RecordList<Team>();
        teamIscritti.setRecords(team);
    }
    public void setDate(LocalDate inizio, LocalDate fine) {

        if(inizio != null && fine != null && inizio.isBefore(fine)) {
            this.dataInizio = inizio;
            this.dataFine = fine;
        }

    }
    public void setDateReg(LocalDate inizio, LocalDate fine) {

        if(inizio != null && fine != null && inizio.isBefore(fine) && fine.isBefore(dataInizio)){
            this.dataInizioReg = inizio;
            this.dataFineReg = fine;
        }
    }


    public int getIdEvento() {return idEvento;}
    public String getTitolo() {return titolo;}
    public String getIndirizzoSede() {return indirizzoSede;}
    public int getnCivicoSede() {return nCivicoSede;}
    public LocalDate getDataInizio() {return dataInizio;}
    public LocalDate getDataFine() {return dataFine;}
    public int getMaxIscritti() {return maxIscritti;}
    public int getMaxTeam() {return maxTeam;}
    public String getDescrizioneProblema() { return descrizioneProblema; }
    public LocalDate getDataInizioReg() {return dataInizioReg;}
    public LocalDate getDataFineReg() {return dataFineReg;}
    public Organizzatore getOrganizzatore() {return organizzatore;}

    public void addInvitoGiudice(Partecipante utente, Boolean answer)
    {
        if(invitiGiudici == null)
            invitiGiudici = new InviteList<Partecipante>();
        invitiGiudici.addInvite(utente, answer);
    }

    public void addInvitoGiudice(Partecipante utente) {
        if(invitiGiudici == null)
            invitiGiudici = new InviteList<Partecipante>();
        invitiGiudici.addInvite(utente);
    }

    public boolean removeInvitoGiudice(){
        if(invitiGiudici != null){
            invitiGiudici.removeInvite();
            return true;
        }
        return false;
    }

    public void setInvitoGiudiceAnswer(Boolean answer){
        if(invitiGiudici != null)
            invitiGiudici.setInviteAnswer(answer);
    }

    @javax.annotation.CheckForNull
    public Boolean getInvitoGiudiceAnswer(){
        if(invitiGiudici != null){
            return invitiGiudici.getInviteAnswer();
        }
        return null;
    }

    public Partecipante getInvitoGiudice(){
        if(invitiGiudici != null){
            return invitiGiudici.getInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean firstInvitoGiudiceAnswer(){
        if(invitiGiudici != null){
            return invitiGiudici.firstInviteAnswer();
        }
        return null;
    }

    public Partecipante firstInvitoGiudice(){
        if(invitiGiudici != null){
            return invitiGiudici.firstInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean previousInvitoGiudiceAnswer(){
        if(invitiGiudici != null){
            return invitiGiudici.previousInviteAnswer();
        }
        return null;
    }

    public Partecipante previousInvitoGiudice(){
        if(invitiGiudici != null){
            return invitiGiudici.previousInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean nextInvitoGiudiceAnswer(){
        if(invitiGiudici != null){
            return invitiGiudici.nextInviteAnswer();
        }
        return null;
    }

    public Partecipante nextInvitoGiudice(){
        if(invitiGiudici != null){
            return invitiGiudici.nextInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean lastInvitoGiudiceAnswer(){
        if(invitiGiudici != null){
            return invitiGiudici.lastInviteAnswer();
        }
        return null;
    }

    public Partecipante lastInvitoGiudice(){
        if(invitiGiudici != null){
            return invitiGiudici.lastInvite();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean seekInvitoGiudiceAnswer(String nomeUtente){
        Partecipante partecipante = firstInvitoGiudice();
        while(partecipante != null){
            if(partecipante.getNomeUtente().equals(nomeUtente)) {
                return getInvitoGiudiceAnswer();
            }
            else partecipante = nextInvitoGiudice();
        }
        return null;
    }

    public Partecipante seekInvitoGiudice(String nomeUtente){
        Partecipante partecipante = firstInvitoGiudice();
        while(partecipante != null){
            if(partecipante.getNomeUtente().equals(nomeUtente)) {
                return partecipante;
            }
            else partecipante = nextInvitoGiudice();
        }
        return null;
    }

    @javax.annotation.CheckForNull
    public Boolean seekAndRemoveInvitoGiudiceAnswer(String nomeUtente){
        Boolean answer = seekInvitoGiudiceAnswer(nomeUtente);
        removeInvitoGiudice();
        return answer;
    }

    public Partecipante seekAndRemoveInvitoGiudice(String nomeUtente){
        Partecipante partecipante = seekInvitoGiudice(nomeUtente);
        removeInvitoGiudice();
        return partecipante;
    }

    public void addGiudice(Giudice giudice) {
        if(giudici == null)
            giudici = new RecordList<Giudice>();
        giudici.addRecord(giudice);
    }

    public boolean removeGiudice(){
        if(giudici != null){
            giudici.removeRecord();
            return true;
        }
        return false;
    }

    public Giudice getGiudice(){
        if(giudici != null){
            return giudici.getRecord();
        }
        return null;
    }

    public Giudice firstGiudice(){
        if(giudici != null){
            return giudici.firstRecord();
        }
        return null;
    }

    public Giudice previousGiudice(){
        if(giudici != null){
            return giudici.previousRecord();
        }
        return null;
    }

    public Giudice nextGiudice(){
        if(giudici != null){
            return giudici.nextRecord();
        }
        return null;
    }

    public Giudice lastGiudice(){
        if(giudici != null){
            return giudici.lastRecord();
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

    public Giudice seekAndRemoveGiudice(String nomeUtente){
        Giudice giudice = seekGiudice(nomeUtente);
        removeInvitoGiudice();
        return giudice;
    }

    public int sizePartecipanti(){
        if(partecipanti != null)
            return partecipanti.size();
        return 0;
    }

    public void addPartecipante(Partecipante utente) {
        if(partecipanti == null)
            partecipanti = new RecordList<Partecipante>();
        partecipanti.addRecord(utente);
    }

    public boolean removePartecipante(){
        if(partecipanti != null){
            partecipanti.removeRecord();
            return true;
        }
        return false;
    }

    public Partecipante getPartecipante(){
        if(partecipanti != null){
            return partecipanti.getRecord();
        }
        return null;
    }

    public Partecipante firstPartecipante(){
        if(partecipanti != null){
            return partecipanti.firstRecord();
        }
        return null;
    }

    public Partecipante previousPartecipante(){
        if(partecipanti != null){
            return partecipanti.previousRecord();
        }
        return null;
    }

    public Partecipante nextPartecipante(){
        if(partecipanti != null){
            return partecipanti.nextRecord();
        }
        return null;
    }

    public Partecipante lastPartecipante(){
        if(partecipanti != null){
            return partecipanti.lastRecord();
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

    public Partecipante seekAndRemovePartecipante(String nomeUtente){
        Partecipante partecipante = seekPartecipante(nomeUtente);
        removePartecipante();
        return partecipante;
    }

    public int sizeTeamIscritti()
    {
        if(teamIscritti != null) return teamIscritti.size();
        return 0;
    }

    public void addTeam(Team team) {

        if(teamIscritti == null)
            teamIscritti = new RecordList<Team>();
        teamIscritti.addRecord(team);

    }

    public boolean removeTeam() {
        if(teamIscritti != null){
            teamIscritti.removeRecord();
            return true;
        }
        return false;
    }

    public Team getTeam(){
        if(teamIscritti != null){
            return teamIscritti.getRecord();
        }
        return null;
    }

    public Team firstTeam(){
        if(teamIscritti != null){
            return teamIscritti.firstRecord();
        }
        return null;
    }

    public Team previousTeam(){
        if(teamIscritti != null){
            return teamIscritti.previousRecord();
        }
        return null;
    }

    public Team nextTeam(){
        if(teamIscritti != null){
            return teamIscritti.nextRecord();
        }
        return null;
    }

    public Team lastTeam(){
        if(teamIscritti != null){
            return teamIscritti.lastRecord();
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

    public Team seekAndRemoveTeam(int idTeam) {
        Team team = seekTeam(idTeam);
        removeTeam();
        return team;
    }

}