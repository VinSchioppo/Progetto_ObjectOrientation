package ClassModel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Team {

    private static int NumTeam = 0;

    private String Nome;
    private int idTeam;

    LinkedList<Utente> MembriTeam = null;
    LinkedList<Evento> EventiIscritti = null;
    private Queue<Partecipante> RichiestePartecipazione = null;
    LinkedList<Progresso> ProgressReport = null;
    LinkedList<Voto> Votazioni = null;

    public void setNome(String Nome) {this.Nome = Nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}

    public String getNome() {return this.Nome;}
    public int getIdTeam() {return this.idTeam;}

    public Team(String Nome) {

        this.Nome = Nome;
        this.idTeam = NumTeam;
        NumTeam++;

    }

    public void iscriviEvento(Evento evento) {

        evento.enqueueListaAttesaTeam(this);

    }

    public void addEvento(Evento evento) {

        if(EventiIscritti == null)
            EventiIscritti = new LinkedList<Evento>();

        EventiIscritti.add(evento);

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
            ProgressReport = new LinkedList<Progresso>();

        ProgressReport.add(progressi);

    }

    void addVoto(Voto voto) {

        if(Votazioni == null)
            Votazioni = new LinkedList<Voto>();

        Votazioni.add(voto);

    }
}