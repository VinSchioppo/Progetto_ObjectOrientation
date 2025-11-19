package ClassModel;

import java.time.LocalDate;
import java.util.LinkedList;



public class Partecipante extends Utente {

    LinkedList<Evento> EventiIscritti = null;
    LinkedList<Team> TeamUniti = null;

    public Partecipante(String NomeUtente, String PasswordUtente) {
        super(NomeUtente, PasswordUtente);
    }

    public void IscriviEvento(Evento evento) {

        evento.EnqueueListaAttesaUtenti(this);

    }

    public void AddEvento(Evento evento) {

        if(EventiIscritti == null)
            EventiIscritti = new LinkedList<Evento>();

        EventiIscritti.add(evento);

    }

    public Team CreaTeam(String Nome) {

        Team new_team = new Team(Nome);
        AddTeam(new_team);
        new_team.MembriTeam = new LinkedList<>();
        new_team.MembriTeam.add(this);
        return new_team;

    }

    public void JoinTeam(Team team) {

        team.EnqueueListaAttesa(this);

    }

    public void AddTeam(Team team) {

        if(TeamUniti == null)
            TeamUniti = new LinkedList<Team>();

        TeamUniti.add(team);

    }

}
