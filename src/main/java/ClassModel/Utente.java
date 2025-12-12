package ClassModel;

import java.time.LocalDate;

public class Utente {

    protected String NomeUtente;
    protected String PasswordUtente;
    protected String FNome = null;
    protected String MNome = null;
    protected String LNome = null;
    protected LocalDate DataNascita = null;

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

    public void printDati() {

        System.out.println("Nome ClassModel.Utente: " + this.NomeUtente);
        System.out.println("\nNome: " + this.FNome);

        if(!this.MNome.isEmpty())
            System.out.println(" " + this.MNome);

        System.out.println(" " + this.LNome);
        System.out.println("\nData di Nascita: " + this.DataNascita);

    }

}