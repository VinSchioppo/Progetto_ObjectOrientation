import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Team {

    private static int NumTeam = 0;

    int idTeam;
    LinkedList<Utente> MembriTeam = null;
    LinkedList<Evento> EventiIscritti = null;
    private Queue<Utente> RichiestePartecipazione = null;
    LinkedList<Progressi> ProgressReport = null;
    LinkedList<Voto> Votazioni = null;

    public Team()
    {
        this.idTeam = NumTeam;
        NumTeam++;
    }

    public void IscriviEvento(Evento evento) {evento.EnqueueListaAttesaTeam(this);}

    public void AddEvento(Evento evento)
    {
        if(EventiIscritti == null) EventiIscritti = new LinkedList<Evento>();
        EventiIscritti.add(evento);
    }

    public int GiveNumMembri() {return MembriTeam.size();}

    public static int GiveNumTeam() {return NumTeam;}

    public void EnqueueListaAttesa(Utente utente)
    {
        if(RichiestePartecipazione == null) RichiestePartecipazione = new LinkedList<Utente>();
        RichiestePartecipazione.add(utente);
    }

    private void DequeueListaAttesa()
    {
        char answer;
        if(RichiestePartecipazione != null) {
            Utente utente = RichiestePartecipazione.poll();
            utente.PrintDati();
            System.out.println("\n\nAccettare questo utente nel team?\n\n(Y/n)");
            Scanner scan = new Scanner(System.in);
            answer = scan.next().charAt(0);
            if((answer == 'Y') || (answer == 'y')) {
                MembriTeam.add(utente);
                utente.AddTeam(this);
            }
        }

    }

    void Aggiungi_ProgressReport(Progressi progressi)
    {
        if(ProgressReport == null) ProgressReport = new LinkedList<Progressi>();
        ProgressReport.add(progressi);
    }

    void AddVoto(Voto voto)
    {
        if(Votazioni == null) Votazioni = new LinkedList<Voto>();
        Votazioni.add(voto);
    }
}
