package ClassModel;

import java.util.ArrayList;
import java.time.LocalDate;

public class Progresso {

    private LocalDate DataProgresso;
    private String TestoDocumeto;

    public void setDataProgresso(LocalDate dataProgresso) {this.DataProgresso = dataProgresso;}
    public void setTestoDocumeto(String TestoDocumento) {this.TestoDocumeto = TestoDocumento;}

    public LocalDate getDataProgresso() {return this.DataProgresso;}
    public String getTestoDocumeto() {return this.TestoDocumeto;}

    ArrayList<Commento> Commenti = null;

    Progresso(String testo) {

        DataProgresso = LocalDate.now();
        TestoDocumeto = testo;

    }

    void addCommento(Commento commento) {

        if(Commenti == null)
            Commenti = new ArrayList<Commento>();

        Commenti.add(commento);

    }
}