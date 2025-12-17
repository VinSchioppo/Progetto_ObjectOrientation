package ClassModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class Utente {

    protected String NomeUtente;
    protected String PasswordUtente;
    protected String FNome = null;
    protected String MNome = null;
    protected String LNome = null;
    protected LocalDate DataNascita = null;

    private int currentEvento = -1;
    private ArrayList<Evento> Eventi = null;

    public Utente(){}

    public Utente(String NomeUtente, String PasswordUtente) {

        this.NomeUtente = NomeUtente;
        this.PasswordUtente = PasswordUtente;

    }

    public void setDati(String FNome, String MNome, String LNome, LocalDate DataNascita) {

        this.FNome = FNome;
        this.MNome = MNome;
        this.LNome = LNome;
        this.DataNascita = DataNascita;

    }

    public void setNomeUtente(String NomeUtente) {this.NomeUtente = NomeUtente;}
    public void setPasswordUtente(String PasswordUtente) {this.PasswordUtente = PasswordUtente;}
    public void setFnome(String FNome) {this.FNome = FNome;}
    public void setMnome(String MNome) {this.MNome = MNome;}
    public void setLnome(String LNome) {this.LNome = LNome;}
    public void setDataNascita(LocalDate DataNascita) {this.DataNascita = DataNascita;}

    public String getNomeUtente() {return this.NomeUtente;}
    public String getPasswordUtente() {return this.PasswordUtente;}
    public String getFNome() {return this.FNome;}
    public String getMNome() {return this.MNome;}
    public String getLNome() {return this.LNome;}
    public LocalDate getDataNascita() {return this.DataNascita;}

    public void addEvento(Evento evento) {
        if(Eventi == null) {
            Eventi = new ArrayList<>();
            currentEvento = 0;
        }
        Eventi.add(evento);
    }

    public void removeEvento(int idEvento){
        Evento evento = firstEvento();
        while(evento != null){
            if(evento.getIdEvento() == idEvento){
                Eventi.remove(currentEvento);
            }
            else evento = nextEvento();
        }
    }

    public int getIdCurrentEvento() {
        Evento evento = getEvento();
        if(evento != null){
            return evento.getIdEvento();
        }
        else return -1;
    }

    public Evento getEvento(){
        if(currentEvento >= 0 && currentEvento < Eventi.size()){
            return Eventi.get(currentEvento);
        }
        return null;
    }

    public Evento firstEvento(){
        currentEvento = 0;
        return getEvento();
    }

    public Evento previousEvento(){
        if(currentEvento >= 0) {
            currentEvento--;
        }
        return getEvento();
    }

    public Evento nextEvento(){
        if(currentEvento < Eventi.size()) {
            currentEvento++;
        }
        return getEvento();
    }

    public Evento lastEvento(){
        currentEvento = Eventi.size() - 1;
        return getEvento();
    }

    public Evento seekEvento(int idEvento) {
        Evento evento = firstEvento();
        while(evento != null){
            if(evento.getIdEvento() == idEvento) {
                return evento;
            }
            else evento = nextEvento();
        }
        return null;
    }


    public void printDati() {

        System.out.println("Nome ClassModel.Utente: " + this.NomeUtente);
        System.out.println("\nNome: " + this.FNome);

        if(!this.MNome.isEmpty())
            System.out.println(" " + this.MNome);

        System.out.println(" " + this.LNome);
        System.out.println("\nData di Nascita: " + this.DataNascita);

    }

}