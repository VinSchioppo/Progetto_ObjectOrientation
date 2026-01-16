package ClassModel;

import java.util.ArrayList;
import RecordList.*;
import java.time.LocalDate;

public class Evento {

    private int IdEvento = -1;
    private String Titolo = null;
    private String IndirizzoSede = null;
    private int NCivicoSede = -1;
    private LocalDate DataInizio = null;
    private LocalDate DataFine = null;
    private int MaxIscritti = -1;
    private int MaxTeam = -1;
    private LocalDate DataInizioReg = null;
    private LocalDate DataFineReg = null;
    private String DescrizioneProblema = null;

    private Organizzatore organizzatore = null;
    private InviteList<Partecipante> InvitiGiudici = null;
    private RecordList<Giudice> Giudici = null;
    private RecordList<Partecipante> Partecipanti = null;
    private RecordList<Team> TeamIscritti = null;

    public Evento(int IdEvento) {this.IdEvento = IdEvento;}
    public Evento(int IdEvento, String Titolo, String Indirizzo, int NCivico) {
        this.IdEvento = IdEvento;
        this.Titolo = Titolo;
        this.IndirizzoSede = Indirizzo;
        this.NCivicoSede = NCivico;
    }
    public Evento(String Titolo) {this.Titolo = Titolo;}
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
    public void setOrganizzatore(Organizzatore organizzatore) {this.organizzatore = organizzatore;}

    public void setInvitiGiudici(ArrayList<Partecipante> inviti) {
        if(InvitiGiudici == null)
            InvitiGiudici = new InviteList<Partecipante>();
        InvitiGiudici.setInvites(inviti);
    }

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

        if(Inizio != null && Fine != null) {
            if (Inizio.isBefore(Fine)) {
                this.DataInizio = Inizio;
                this.DataFine = Fine;
            }
        }

    }
    public void setDateReg(LocalDate Inizio, LocalDate Fine) {

        if(Inizio != null && Fine != null){
            if (Inizio.isBefore(Fine) && Fine.isBefore(DataInizio)) {
                this.DataInizioReg = Inizio;
                this.DataFineReg = Fine;
            }
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

    public void addInvitoGiudice(Partecipante utente, Boolean answer)
    {
        if(InvitiGiudici == null)
            InvitiGiudici = new InviteList<Partecipante>();
        InvitiGiudici.addInvite(utente, answer);
    }

    public void addInvitoGiudice(Partecipante utente) {
        if(InvitiGiudici == null)
            InvitiGiudici = new InviteList<Partecipante>();
        InvitiGiudici.addInvite(utente);
    }

    public boolean removeInvitoGiudice(){
        if(InvitiGiudici != null){
            InvitiGiudici.removeInvite();
            return true;
        }
        return false;
    }

    public void setInvitoGiudiceAnswer(Boolean answer){
        if(InvitiGiudici != null)
            InvitiGiudici.setInviteAnswer(answer);
    }

    public Boolean getInvitoGiudiceAnswer(){
        if(InvitiGiudici != null){
            return InvitiGiudici.getInviteAnswer();
        }
        return null;
    }

    public Partecipante getInvitoGiudice(){
        if(InvitiGiudici != null){
            return InvitiGiudici.getInvite();
        }
        return null;
    }

    public Boolean firstInvitoGiudiceAnswer(){
        if(InvitiGiudici != null){
            return InvitiGiudici.firstInviteAnswer();
        }
        return null;
    }

    public Partecipante firstInvitoGiudice(){
        if(InvitiGiudici != null){
            return InvitiGiudici.firstInvite();
        }
        return null;
    }

    public Boolean previousInvitoGiudiceAnswer(){
        if(InvitiGiudici != null){
            return InvitiGiudici.previousInviteAnswer();
        }
        return null;
    }

    public Partecipante previousInvitoGiudice(){
        if(InvitiGiudici != null){
            return InvitiGiudici.previousInvite();
        }
        return null;
    }

    public Boolean nextInvitoGiudiceAnswer(){
        if(InvitiGiudici != null){
            return InvitiGiudici.nextInviteAnswer();
        }
        return null;
    }

    public Partecipante nextInvitoGiudice(){
        if(InvitiGiudici != null){
            return InvitiGiudici.nextInvite();
        }
        return null;
    }

    public Boolean lastInvitoGiudiceAnswer(){
        if(InvitiGiudici != null){
            return InvitiGiudici.lastInviteAnswer();
        }
        return null;
    }

    public Partecipante lastInvitoGiudice(){
        if(InvitiGiudici != null){
            return InvitiGiudici.lastInvite();
        }
        return null;
    }

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
        if(Giudici == null)
            Giudici = new RecordList<Giudice>();
        Giudici.addRecord(giudice);
    }

    public boolean removeGiudice(){
        if(Giudici != null){
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

    public Giudice seekAndRemoveGiudice(String nomeUtente){
        Giudice giudice = seekGiudice(nomeUtente);
        removeInvitoGiudice();
        return giudice;
    }

    public int sizePartecipanti(){
        if(Partecipanti != null)
            return Partecipanti.size();
        return 0;
    }

    public void addPartecipante(Partecipante utente) {
        if(Partecipanti == null)
            Partecipanti = new RecordList<Partecipante>();
        Partecipanti.addRecord(utente);
    }

    public boolean removePartecipante(){
        if(Partecipanti != null){
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

    public Partecipante seekAndRemovePartecipante(String nomeUtente){
        Partecipante partecipante = seekPartecipante(nomeUtente);
        removePartecipante();
        return partecipante;
    }

    public void addTeam(Team team) {

        if(TeamIscritti == null)
            TeamIscritti = new RecordList<Team>();
        TeamIscritti.addRecord(team);

    }

    public boolean removeTeam() {
        if(TeamIscritti != null){
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

    public Team seekAndRemoveTeam(int idTeam) {
        Team team = seekTeam(idTeam);
        removeTeam();
        return team;
    }

}