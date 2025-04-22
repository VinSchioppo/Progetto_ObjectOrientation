import java.util.Date;
import java.util.LinkedList;

public class Progressi {

    Date DataProgresso;
    String TestoDocumeto;
    LinkedList<Commento> Commenti = null;

    Progressi(String testo) {
        
        DataProgresso = new Date();
        TestoDocumeto = testo;
    
    }

    void AddCommento(Commento commento) {
        
        if(Commenti == null) Commenti = new LinkedList<Commento>();
        Commenti.add(commento);
    
    }
}
