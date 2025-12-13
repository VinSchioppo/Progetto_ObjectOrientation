package ClassModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Team {

    private int idTeam;
    private String Nome;

    private ArrayList<Partecipante> MembriTeam = null;
    private Evento EventoIscritto = null;
    private Queue<Partecipante> RichiestePartecipazione = null;
    private ArrayList<Progresso> ProgressReport = null;
    private ArrayList<Voto> Votazioni = null;

    public Team(int idTeam) {this.idTeam = idTeam;}

    public Team(String Nome, Evento EventoIscritto) {

        this.Nome = Nome;
    }

    public void setNome(String Nome) {this.Nome = Nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setEventoIscritto(Evento EventoIscritto) {
        this.EventoIscritto = EventoIscritto;
        EventoIscritto.addTeam(this);
    }


    public String getNome() {return this.Nome;}
    public int getIdTeam() {return this.idTeam;}
    public int getidEvento() {return EventoIscritto.getIdEvento();}
    public Evento getEventoIscritto() {return this.EventoIscritto;}

    public void iscriviEvento(Evento evento) {

        evento.enqueueListaAttesaTeam(this);

    }

    public void addMembroTeam(Partecipante part) {

        if(MembriTeam == null)
            MembriTeam = new ArrayList<Partecipante>();
        MembriTeam.add(part);
    }

    public int giveNumMembri() {

        return MembriTeam.size();

    }

    public void enqueueListaAttesa(Partecipante utente)
    {

        if(RichiestePartecipazione == null)
            RichiestePartecipazione = new LinkedList<Partecipante>();

        RichiestePartecipazione.add(utente);

    }

    private void dequeueListaAttesa()
    {

        char answer;

        if(RichiestePartecipazione != null) {

            Partecipante utente = RichiestePartecipazione.poll();
            utente.printDati();
            System.out.println("\n\nAccettare questo utente nel team?\n\n(Y/n)");
            Scanner scan = new Scanner(System.in);
            answer = scan.next().charAt(0);

            if((answer == 'Y') || (answer == 'y')) {
                MembriTeam.add(utente);
                utente.addTeam(this);
            }
        }
    }

    void aggiungi_ProgressReport(Progresso progressi) {

        if(ProgressReport == null)
            ProgressReport = new ArrayList<Progresso>();

        ProgressReport.add(progressi);
    }

    void addVoto(Voto voto) {

        if(Votazioni == null)
            Votazioni = new ArrayList<Voto>();

        Votazioni.add(voto);
    }


    public void printMembri() {
        for(Partecipante p : MembriTeam)
        {
            System.out.println(p.getNomeUtente());
        }
    }
}