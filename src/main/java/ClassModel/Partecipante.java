package ClassModel;

import RecordList.*;
import java.util.ArrayList;

public class Partecipante extends Utente {

    private RecordList<Team> TeamUniti = null;

    public Partecipante() {}

    public Partecipante(String NomeUtente, String PasswordUtente) {
        super(NomeUtente, PasswordUtente);
    }

    public void setTeamUniti(ArrayList<Team> teams) {
        if (TeamUniti == null)
            this.TeamUniti = new RecordList<Team>();
        TeamUniti.setRecords(teams);
    }

    public Team creaTeam(String Nome, Evento EventoIscritto) {

        Team newTeam = new Team(Nome, EventoIscritto);
        addTeam(newTeam);
        newTeam.addMembroTeam(this);
        return newTeam;
    }

    public void addTeam(Team team) {

        if(TeamUniti == null)
            TeamUniti = new RecordList<Team>();

        TeamUniti.addRecord(team);

    }

    public boolean removeTeam(int idTeam) {
        if(TeamUniti != null){
            seekTeam(idTeam);
            TeamUniti.removeRecord();
            return true;
        }
        return false;
    }

    public Team getTeam(){
        if(TeamUniti != null){
            return TeamUniti.getRecord();
        }
        return null;
    }

    public Team firstTeam(){
        if(TeamUniti != null){
            return TeamUniti.firstRecord();
        }
        return null;
    }

    public Team previousTeam(){
        if(TeamUniti != null){
            return TeamUniti.previousRecord();
        }
        return null;
    }

    public Team nextTeam(){
        if(TeamUniti != null){
            return TeamUniti.nextRecord();
        }
        return null;
    }

    public Team lastTeam(){
        if(TeamUniti != null){
            return TeamUniti.lastRecord();
        }
        return null;
    }

    public Team seekTeam(int idTeam){
        Team team = firstTeam();
        while(team != null){
            if(team.getIdTeam() == idTeam) {
                return team;
            }
            else team = nextTeam();
        }
        return null;
    }

    public Team seekTeamEvento(int idEvento){
        Team team = firstTeam();
        while(team != null){
            if(team.getEventoIscritto().getIdEvento() == idEvento) {
                return team;
            }
            else team = nextTeam();
        }
        return null;
    }

}