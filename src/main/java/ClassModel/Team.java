package ClassModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Team {

    private static int NumTeam = 0;

    private String Nome;
    private int idTeam;


    ArrayList<Partecipante> MembriTeam = null;
    private Evento EventoIscritto = null;
    private Queue<Partecipante> RichiestePartecipazione = null;
    ArrayList<Progresso> ProgressReport = null;
    ArrayList<Voto> Votazioni = null;

    public Team(String Nome) {

        this.Nome = Nome;
        this.idTeam = NumTeam;
        NumTeam++;

    }

    public void setNome(String Nome) {this.Nome = Nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setEventoIscritto(Evento EventoIscritto) {this.EventoIscritto = EventoIscritto;}


    public String getNome() {return this.Nome;}
    public int getIdTeam() {return this.idTeam;}
    public Evento getEventoIscritto() {return this.EventoIscritto;}
    public int getidEvento() {return EventoIscritto.getIdEvento();}

    public void iscriviEvento(Evento evento) {

        evento.enqueueListaAttesaTeam(this);

    }

    public int giveNumMembri() {

        return MembriTeam.size();

    }

    public static int giveNumTeam() {

        return NumTeam;

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
}