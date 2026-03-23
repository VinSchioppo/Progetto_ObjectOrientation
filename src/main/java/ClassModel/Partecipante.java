package ClassModel;

import RecordList.*;
import java.util.List;

public class Partecipante extends Utente {

    private RecordList<Team> teamUniti = null;

    public Partecipante(String nomeUtente, String passwordUtente) {
        super(nomeUtente, passwordUtente);
    }

    public void setTeamUniti(List<Team> teams) {
        if (teamUniti == null)
            this.teamUniti = new RecordList<Team>();
        teamUniti.setRecords(teams);
    }

    public void addTeam(Team team) {

        if(teamUniti == null)
            teamUniti = new RecordList<Team>();

        teamUniti.addRecord(team);

    }

    public boolean removeTeam(int idTeam) {
        if(teamUniti != null){
            seekTeam(idTeam);
            teamUniti.removeRecord();
            return true;
        }
        return false;
    }

    public Team getTeam(){
        if(teamUniti != null){
            return teamUniti.getRecord();
        }
        return null;
    }

    public Team firstTeam(){
        if(teamUniti != null){
            return teamUniti.firstRecord();
        }
        return null;
    }

    public Team previousTeam(){
        if(teamUniti != null){
            return teamUniti.previousRecord();
        }
        return null;
    }

    public Team nextTeam(){
        if(teamUniti != null){
            return teamUniti.nextRecord();
        }
        return null;
    }

    public Team lastTeam(){
        if(teamUniti != null){
            return teamUniti.lastRecord();
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