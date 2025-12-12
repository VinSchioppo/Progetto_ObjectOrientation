package ClassModel;

import java.util.ArrayList;
import java.time.LocalDate;

public class Progresso {

    private int idProgresso;
    private int idTeam;
    private LocalDate DataProgresso;
    private String TestoDocumeto;

    private ArrayList<Commento> Commenti = null;

    Progresso(String testo, int idTeam) {

        DataProgresso = LocalDate.now();
        TestoDocumeto = testo;
        this.idTeam = idTeam;

    }

    public void setDataProgresso(LocalDate dataProgresso) {this.DataProgresso = dataProgresso;}
    public void setTestoDocumeto(String TestoDocumento) {this.TestoDocumeto = TestoDocumento;}

    public int getIdProgresso() {return idProgresso;}
    public LocalDate getDataProgresso() {return this.DataProgresso;}
    public String getTestoDocumeto() {return this.TestoDocumeto;}
    public int getIdTeam() {return this.idTeam;}

    public void addCommento(Commento commento) {

        if(Commenti == null)
            Commenti = new ArrayList<Commento>();

        Commenti.add(commento);
    }
}