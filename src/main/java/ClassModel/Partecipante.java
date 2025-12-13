package ClassModel;

import java.util.ArrayList;

public class Partecipante extends Utente {

    private ArrayList<Evento> EventiIscritti = null;
    private ArrayList<Team> TeamUniti = null;

    public Partecipante() {}

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

    public Team creaTeam(String Nome, Evento EventoIscritto) {

        Team newTeam = new Team(Nome, EventoIscritto);
        addTeam(newTeam);
        newTeam.addMembroTeam(this);
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