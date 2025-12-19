package ClassModel;

import RecordList.RecordList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Team {

    private int idTeam;
    private String Nome;

    private RecordList<Partecipante> MembriTeam = null;
    private Evento EventoIscritto = null;
    private Queue<Partecipante> RichiestePartecipazione = null;
    private RecordList<Progresso> Progressi = null;
    private RecordList<Voto> Voti = null;

    public Team(int idTeam) {this.idTeam = idTeam;}

    public Team(String Nome, Evento EventoIscritto) {

        this.Nome = Nome;
    }

    public void setNome(String Nome) {this.Nome = Nome;}
    public void setIdTeam(int idTeam) {this.idTeam = idTeam;}
    public void setMembriTeam(ArrayList<Partecipante> partecipanti) {
        if(MembriTeam == null)
            MembriTeam = new RecordList<Partecipante>();
        MembriTeam.setRecords(partecipanti);
    }

    public void setProgressi(ArrayList<Progresso> progressi) {
        if(Progressi == null)
            Progressi = new RecordList<Progresso>();
        Progressi.setRecords(progressi);
    }

    public void setVoti(ArrayList<Voto> voti){
        if(Voti == null)
            Voti = new RecordList<Voto>();
        Voti.setRecords(voti);
    }

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
            MembriTeam = new RecordList<Partecipante>();
        MembriTeam.addRecord(part);
    }

    public boolean removeMembroTeam(String nomeUtente){
        if(MembriTeam != null){
            seekMembroTeam(nomeUtente);
            MembriTeam.removeRecord();
            return true;
        }
        return false;
    }

    public Partecipante getMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.getRecord();
        }
        return null;
    }

    public Partecipante firstMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.firstRecord();
        }
        return null;
    }

    public Partecipante previousMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.previousRecord();
        }
        return null;
    }

    public Partecipante nextMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.nextRecord();
        }
        return null;
    }

    public Partecipante lastMembroTeam(){
        if(MembriTeam != null){
            return MembriTeam.lastRecord();
        }
        return null;
    }

    public Partecipante seekMembroTeam(String nomeUtente){
        Partecipante membro = firstMembroTeam();
        while(membro != null){
            if(membro.getNomeUtente().equals(nomeUtente)) {
                return membro;
            }
            else membro = nextMembroTeam();
        }
        return null;
    }

    public int giveNumMembri() {
        if(MembriTeam != null) {
            return MembriTeam.size();
        }
        return 0;
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
                MembriTeam.addRecord(utente);
                utente.addTeam(this);
            }
        }
    }

    void addProgresso(Progresso progressi) {

        if(Progressi == null)
            Progressi = new RecordList<Progresso>();

        Progressi.addRecord(progressi);
    }

    public boolean removeProgresso(int idProgresso){
        if(Progressi != null){
            seekProgresso(idProgresso);
            Progressi.removeRecord();
            return true;
        }
        return false;
    }

    public Progresso getProgresso(){
        if(Progressi != null){
            return Progressi.getRecord();
        }
        return null;
    }

    public Progresso firstProgresso(){
        if(Progressi != null){
            return Progressi.firstRecord();
        }
        return null;
    }

    public Progresso previousProgresso(){
        if(Progressi != null){
            return Progressi.previousRecord();
        }
        return null;
    }

    public Progresso nextProgresso(){
        if(Progressi != null){
            return Progressi.nextRecord();
        }
        return null;
    }

    public Progresso lastProgresso(){
        if(Progressi != null){
            return Progressi.lastRecord();
        }
        return null;
    }

    public Progresso seekProgresso(int idProgresso){
        Progresso progresso = firstProgresso();
        while(progresso != null){
            if(progresso.getIdProgresso() == idProgresso) {
                return progresso;
            }
            else progresso = nextProgresso();
        }
        return null;
    }


    void addVoto(Voto voto) {

        if(Voti == null)
            Voti = new RecordList<Voto>();

        Voti.addRecord(voto);
    }

    public boolean removeVoto(String nomeGiudice){
        if(Voti != null){
            seekVoto(nomeGiudice);
            Voti.removeRecord();
            return true;
        }
        return false;
    }

    public Voto getVoto(){
        if(Voti != null){
            return Voti.getRecord();
        }
        return null;
    }

    public Voto firstVoto(){
        if(Voti != null){
            return Voti.firstRecord();
        }
        return null;
    }

    public Voto previousVoto(){
        if(Voti != null){
            return Voti.previousRecord();
        }
        return null;
    }

    public Voto nextVoto(){
        if(Voti != null){
            return Voti.nextRecord();
        }
        return null;
    }

    public Voto lastVoto(){
        if(Voti != null){
            return Voti.lastRecord();
        }
        return null;
    }

    public Voto seekVoto(String nomeGiudice){
        Voto voto = firstVoto();
        while(voto != null){
            if(voto.getGiudice().equals(nomeGiudice)) {
                return voto;
            }
            else voto = nextVoto();
        }
        return null;
    }

    public void printMembri() {
        firstMembroTeam();
        while(getMembroTeam() != null)
        {
            System.out.println(getMembroTeam().getNomeUtente());
            nextMembroTeam();
        }
    }
}