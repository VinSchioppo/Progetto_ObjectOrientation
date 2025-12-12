package ClassModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class Partecipante extends Utente {

    private ArrayList<Evento> EventiIscritti = null;
    private ArrayList<Team> TeamUniti = null;

    public Partecipante(String NomeUtente, String PasswordUtente) {
        super(NomeUtente, PasswordUtente);
    }

    public void IscriviEvento(Evento evento) {

        evento.enqueueListaAttesaUtenti(this);

    }

    public void addEvento(Evento evento) {

        if(EventiIscritti == null)
            EventiIscritti = new ArrayList<Evento>();

        EventiIscritti.add(evento);

    }

    public Team creaTeam(String Nome) {

        Team newTeam = new Team(Nome);
        addTeam(newTeam);
        newTeam.MembriTeam = new ArrayList<Partecipante>();
        newTeam.MembriTeam.add(this);
        return newTeam;

    }

    public void joinTeam(Team team) {

        team.enqueueListaAttesa(this);

    }

    public void addTeam(Team team) {

        if(TeamUniti == null)
            TeamUniti = new ArrayList<Team>();

        TeamUniti.add(team);

    }

}