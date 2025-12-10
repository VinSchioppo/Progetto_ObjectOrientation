package ClassModel;

import java.time.LocalDate;
import java.util.LinkedList;

public class Partecipante extends Utente {

    private LinkedList<Evento> EventiIscritti = null;
    private LinkedList<Team> TeamUniti = null;

    public Partecipante(String NomeUtente, String PasswordUtente) {
        super(NomeUtente, PasswordUtente);
    }

    public void IscriviEvento(Evento evento) {

        evento.enqueueListaAttesaUtenti(this);

    }

    public void addEvento(Evento evento) {

        if(EventiIscritti == null)
            EventiIscritti = new LinkedList<Evento>();

        EventiIscritti.add(evento);

    }

    public Team creaTeam(String Nome) {

        Team new_team = new Team(Nome);
        addTeam(new_team);
        new_team.MembriTeam = new LinkedList<>();
        new_team.MembriTeam.add(this);
        return new_team;

    }

    public void joinTeam(Team team) {

        team.enqueueListaAttesa(this);

    }

    public void addTeam(Team team) {

        if(TeamUniti == null)
            TeamUniti = new LinkedList<Team>();

        TeamUniti.add(team);

    }

}