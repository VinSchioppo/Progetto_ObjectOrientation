package ClassModel;

import java.util.Date;
import java.util.LinkedList;

public class Progresso {

    private Date DataProgresso;
    private String TestoDocumeto;

    public void setDataProgresso(Date dataProgresso) {this.DataProgresso = dataProgresso;}
    public void setTestoDocumeto(String TestoDocumento) {this.TestoDocumeto = TestoDocumento;}

    public Date getDataProgresso() {return this.DataProgresso;}
    public String getTestoDocumeto() {return this.TestoDocumeto;}

    LinkedList<Commento> Commenti = null;

    Progresso(String testo) {

        DataProgresso = new Date();
        TestoDocumeto = testo;

    }

    void addCommento(Commento commento) {

        if(Commenti == null)
            Commenti = new LinkedList<Commento>();

        Commenti.add(commento);

    }
}